from botbuilder.dialogs import ComponentDialog, WaterfallDialog, WaterfallStepContext, DialogTurnStatus, DialogTurnResult
from botbuilder.core import MessageFactory, CardFactory
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from botbuilder.schema import HeroCard, CardAction, CardImage, ActionTypes
from botbuilder.core import UserState

# Supondo que ProductAPI e ComprarProdutoDialog estão implementados
from api.product_api import ProductAPI
from dialogs.comprar_produto_dialog import ComprarProdutoDialog


class ConsultarProdutosDialog(ComponentDialog):
    def __init__(self, user_state: UserState):
        super(ConsultarProdutosDialog, self).__init__("ConsultarProdutosDialog")

        # Registrar prompts utilizados
        self.add_dialog(TextPrompt(TextPrompt.__name__))
        
        # Registrar subdiálogo para compra
        self.add_dialog(ComprarProdutoDialog(user_state))

        # Configurar WaterfallDialog com 3 etapas
        self.add_dialog(
            WaterfallDialog(
                "consultarProdutoWaterfall",
                [
                    self.product_name_step,
                    self.product_name_search_step,
                    self.buy_product_step
                ],
            )
        )

        self.initial_dialog_id = "consultarProdutoWaterfall"

    async def product_name_step(self, step_context: WaterfallStepContext):
        """Solicita o nome do produto"""
        prompt_message = MessageFactory.text("Por favor, digite o nome do produto que você deseja consultar.")
        prompt_options = PromptOptions(
            prompt=prompt_message,
            retry_prompt=MessageFactory.text("Desculpe, não consegui entender. Por favor, digite o nome do produto novamente."),
        )
        return await step_context.prompt(TextPrompt.__name__, prompt_options)

    async def product_name_search_step(self, step_context: WaterfallStepContext):
        """Busca o produto pela API e mostra os resultados em cards"""
        product_name = step_context.result
        await self.show_card_results(product_name, step_context)
        return DialogTurnResult(status=DialogTurnStatus.Waiting)

    async def buy_product_step(self, step_context: WaterfallStepContext):
        """Processa a escolha do usuário (compra ou cancela)"""
        result_action = step_context.context.activity.value

        if not result_action or result_action.get("acao") != "comprar":
            return await step_context.end_dialog()

        product_id = result_action["productId"]
        return await step_context.begin_dialog("ComprarProdutoDialog", {"productId": product_id})

    async def show_card_results(self, product_name: str, step_context: WaterfallStepContext):
        """Exibe os produtos encontrados como cartões interativos"""
        produto_api = ProductAPI()
        response = produto_api.search_product(product_name)

        if not response:
            await step_context.context.send_activity(MessageFactory.text("Nenhum produto encontrado."))
            return

        for produto in response:
            card = CardFactory.hero_card(
                HeroCard(
                    title=produto["productName"],
                    subtitle=produto["productDescription"],
                    text=f"Preço: R$ {produto['price']}",
                    images=[CardImage(url=img) for img in produto.get("imageUrl", [])],
                    buttons=[
                        CardAction(
                            type=ActionTypes.post_back,
                            title=f"Comprar {produto['productName']}",
                            value={"acao": "comprar", "productId": produto["id"]},
                        )
                    ],
                )
            )
            await step_context.context.send_activity(MessageFactory.attachment(card))
