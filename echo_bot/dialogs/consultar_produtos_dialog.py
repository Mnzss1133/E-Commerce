from botbuilder.dialogs import ComponentDialog, WaterfallDialog, WaterfallStepContext
from botbuilder.core import MessageFactory
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from api.product_api import ProductAPI  # Importa o conector da API


class ConsultarProdutoDialog(ComponentDialog):
    def __init__(self):
        super(ConsultarProdutoDialog, self).__init__("ConsultarProdutoDialog")

        self.add_dialog(TextPrompt(TextPrompt.__name__))

        self.add_dialog(
            WaterfallDialog(
                "consultarProdutoWaterfallDialog",
                [
                    self.product_name_step,
                    self.prompt_process_product_name_step,
                ],
            )
        )

        self.initial_dialog_id = "consultarProdutoWaterfallDialog"

    async def product_name_step(self, step_context: WaterfallStepContext):
        prompt_message = MessageFactory.text("Digite o nome do produto para consultar:")
        prompt_option = PromptOptions(
            prompt=prompt_message,
            retry_prompt=MessageFactory.text("Digite o nome do produto novamente:"),
        )
        return await step_context.prompt(TextPrompt.__name__, prompt_option)

    async def prompt_process_product_name_step(self, step_context: WaterfallStepContext):
        product_name = step_context.result.strip()

        api = ProductAPI()
        produtos = api.consultar_produtos(product_name)

        if produtos and len(produtos) > 0:
            produto = produtos[0]  # Exibe apenas o primeiro produto encontrado
            resposta = (
                f"Produto encontrado:\n"
                f"Nome: {produto.get('productName')}"
            )
        else:
            resposta = "❌ Nenhum produto encontrado com esse nome."

        await step_context.context.send_activity(MessageFactory.text(resposta))
        return await step_context.end_dialog()
