from botbuilder.core import MessageFactory
from botbuilder.dialogs import (
    ComponentDialog,
    WaterfallDialog,
    WaterfallStepContext,
    DialogTurnResult,
)
from botbuilder.dialogs.prompts import (
    TextPrompt,
    ChoicePrompt,
    PromptOptions,
)
from botbuilder.dialogs.choices import Choice

# Importando as suas classes de API
from api.product_api import ProductAPI
from api.pedido_api import PedidoAPI


class CompraProdutoDialog(ComponentDialog):
    def __init__(self, dialog_id: str = "CompraProdutoDialog"):
        super(CompraProdutoDialog, self).__init__(dialog_id)

        self.product_api = ProductAPI()
        self.pedido_api = PedidoAPI()

        self.add_dialog(TextPrompt("TextPrompt"))
        self.add_dialog(ChoicePrompt("ChoicePrompt"))

        self.add_dialog(
            WaterfallDialog(
                "CompraProdutoWaterfall",
                [
                    self.prompt_user_id_step,
                    self.prompt_product_name_step,
                    self.select_product_step,
                    self.prompt_card_number_step,
                    self.prompt_cvv_step,
                    self.process_purchase_step,
                ],
            )
        )
        self.initial_dialog_id = "CompraProdutoWaterfall"

    async def prompt_user_id_step(self, step_context: WaterfallStepContext):
        return await step_context.prompt(
            "TextPrompt", PromptOptions(prompt=MessageFactory.text("Por favor, informe o seu ID de utilizador."))
        )

    async def prompt_product_name_step(self, step_context: WaterfallStepContext):
        try:
            step_context.values["user_id"] = int(step_context.result)
        except ValueError:
            await step_context.context.send_activity("O ID do utilizador deve ser um número. Por favor, comece de novo.")
            return await step_context.end_dialog()

        return await step_context.prompt(
            "TextPrompt", PromptOptions(prompt=MessageFactory.text("Qual produto deseja comprar?"))
        )

    async def select_product_step(self, step_context: WaterfallStepContext):
        product_name = step_context.result
        await step_context.context.send_activity(f"A procurar por '{product_name}'...")
        
        products = self.product_api.consultar_produtos(product_name)

        if not products:
            await step_context.context.send_activity("Não encontrei nenhum produto com esse nome.")
            return await step_context.end_dialog()
        
        step_context.values["found_products"] = products

        if len(products) == 1:
            selected_product = products[0]
            step_context.values["selected_product"] = selected_product
            await step_context.context.send_activity(f"Encontrei: {selected_product['productName']} por R$ {selected_product['price']:.2f}.")
            return await step_context.next(None)
        else:
            await step_context.context.send_activity("Encontrei alguns produtos. Qual deles quer?")
            choices = [Choice(f"{p['id']} - {p['productName']} (R$ {p['price']:.2f})") for p in products]
            return await step_context.prompt("ChoicePrompt", PromptOptions(choices=choices))

    async def prompt_card_number_step(self, step_context: WaterfallStepContext):
        # **INÍCIO DA CORREÇÃO**
        # Esta função é chamada após o produto ser identificado.
        # A lógica foi refeita para ser mais segura e evitar o erro.

        # CASO 1: O usuário acabou de escolher um produto de uma lista.
        # step_context.result terá um valor (o objeto da escolha).
        if step_context.result is not None and hasattr(step_context.result, "value"):
            chosen_id = step_context.result.value.split(" - ")[0]
            found_products = step_context.values.get("found_products", [])
            selected_product = next((p for p in found_products if p['id'] == chosen_id), None)
            
            if not selected_product:
                await step_context.context.send_activity("Ocorreu um erro ao processar sua escolha. Tente novamente.")
                return await step_context.end_dialog()

            # Armazena o produto final escolhido.
            step_context.values["selected_product"] = selected_product
        
     

        # Verificação final para garantir que temos um produto antes de prosseguir.
        if "selected_product" not in step_context.values:
            await step_context.context.send_activity("Não foi possível identificar o produto. Por favor, inicie a compra novamente.")
            return await step_context.end_dialog()
            
        return await step_context.prompt(
            "TextPrompt", PromptOptions(prompt=MessageFactory.text("Digite o número do seu cartão de crédito."))
        )
        # **FIM DA CORREÇÃO**

    async def prompt_cvv_step(self, step_context: WaterfallStepContext):
        step_context.values["card_number"] = step_context.result
        return await step_context.prompt(
            "TextPrompt", PromptOptions(prompt=MessageFactory.text("Agora, digite o código de segurança (CVV)."))
        )

    async def process_purchase_step(self, step_context: WaterfallStepContext):
        step_context.values["cvv"] = step_context.result
        await step_context.context.send_activity("A processar a sua compra...")

        user_id = step_context.values["user_id"]
        selected_product = step_context.values["selected_product"]
        card_number = step_context.values["card_number"]
        cvv = step_context.values["cvv"]
        
        result = self.pedido_api.criar_pedido(
            user_id=user_id,
            product_ids=[selected_product["id"]],
            card_number=card_number,
            cvv=cvv
        )

        if result.get("success"):
            pedido = result["data"]
            valor_total = pedido.get('valorTotal', 0)
            await step_context.context.send_activity(f"✅ Compra realizada com sucesso! ID do pedido: {pedido['id']}. Valor: R$ {valor_total:.2f}.")
        else:
            await step_context.context.send_activity(f"❌ Erro ao processar a compra: {result.get('message', 'Erro desconhecido')}")

        return await step_context.end_dialog()
