from botbuilder.core import CardFactory

from botbuilder.dialogs import (
    ComponentDialog,
    WaterfallDialog,
    WaterfallStepContext,
    DialogTurnResult,
)
from botbuilder.dialogs.prompts import (
    TextPrompt,
    NumberPrompt,
    ChoicePrompt,
    ConfirmPrompt,
    AttachmentPrompt,
    PromptOptions,
    PromptValidatorContext,
)
from botbuilder.schema import (
    ActionTypes,
    HeroCard,
    CardAction,
    CardImage,
)
from botbuilder.dialogs.choices import Choice
from botbuilder.core import MessageFactory, UserState
from api.product_api import ProductAPI
from dialogs.consultar_pedido_dialog import ConsultarPedidoDialog
from dialogs.consultar_produtos_dialog import ConsultarProdutoDialog
from dialogs.extrato_compra_dialog import ExtratoCompraDialog


from dialogs.compra_produto_dialog import CompraProdutoDialog  # Importando o novo dialogo

class MainDialog(ComponentDialog):
    def __init__(self, user_state: UserState):
        super(MainDialog, self).__init__("MainDialog")

        self.user_state = user_state

        # Adicionando o ChoicePrompt para a escolha
        self.add_dialog(ChoicePrompt(ChoicePrompt.__name__))

        # Adicionando o diálogo de compra de produto
        self.add_dialog(CompraProdutoDialog())

        # Adicionando outros diálogos já existentes
        self.add_dialog(ConsultarPedidoDialog())
        self.add_dialog(ConsultarProdutoDialog())
        self.add_dialog(ExtratoCompraDialog())

        # Prompt para escolha das opções
        self.add_dialog(
            WaterfallDialog(
                "MainDialog",
                [
                    self.prompt_option_step,
                    self.process_option_step,
                ],
            )
        )

        self.initial_dialog_id = "MainDialog"

    async def prompt_option_step(self, step_context: WaterfallStepContext):
        return await step_context.prompt(
            ChoicePrompt.__name__,
            PromptOptions(
                prompt=MessageFactory.text("Escolha a opção desejada:"),
                choices=[Choice("Consultar Pedidos"), Choice("Consultar Produtos"), Choice("Extrato de Compras"), Choice("Comprar Produto")],
            ),
        )

    async def process_option_step(self, step_context: WaterfallStepContext):
        choice = step_context.result.value
        
        if choice == "Consultar Pedidos":
            return await step_context.begin_dialog("ConsultarPedidoDialog")
        elif choice == "Consultar Produtos":
            return await step_context.begin_dialog("ConsultarProdutoDialog")
        elif choice == "Extrato de Compras":
            return await step_context.begin_dialog("ExtratoCompraDialog")
        elif choice == "Comprar Produto":
            return await step_context.begin_dialog("CompraProdutoDialog")

        return await step_context.end_dialog()