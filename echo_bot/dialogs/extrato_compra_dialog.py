from botbuilder.core import MessageFactory, CardFactory
from botbuilder.dialogs import (
    ComponentDialog,
    WaterfallDialog,
    WaterfallStepContext,
    DialogTurnResult,
)
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from botbuilder.schema import Attachment
from api.extrato_compra_api import ExtratoCompraAPI

class ExtratoCompraDialog(ComponentDialog):
    def __init__(self):
        super(ExtratoCompraDialog, self).__init__(self.__class__.__name__)

        # Adiciona o TextPrompt para pedir o ID do usuário
        self.add_dialog(TextPrompt("idUsuarioPrompt"))

        # A cascata agora terá dois passos: pedir o ID e depois mostrar o extrato
        self.add_dialog(
            WaterfallDialog(
                "ExtratoCompraWaterfall",
                [
                    self.prompt_usuario_id_step,
                    self.mostrar_extrato_consolidado_step,
                ],
            )
        )
        self.initial_dialog_id = "ExtratoCompraWaterfall"

    async def prompt_usuario_id_step(self, step_context: WaterfallStepContext):
        """Este passo foi reinserido para perguntar o ID do usuário."""
        prompt_message = MessageFactory.text("Por favor, digite o ID do usuário para consultar o extrato de compras.")
        
        return await step_context.prompt(
            "idUsuarioPrompt",
            PromptOptions(prompt=prompt_message)
        )

    async def mostrar_extrato_consolidado_step(self, step_context: WaterfallStepContext):
        # Pega o ID do usuário que foi digitado no passo anterior
        usuario_id = step_context.result
        
        await step_context.context.send_activity(f"Vou preparar o extrato de compras para o usuário {usuario_id}. Só um momento...")

        extrato_api = ExtratoCompraAPI()
        try:
            # A lógica de chamar a API e processar os dados continua a mesma
            pedidos = extrato_api.consultar_extrato_compras(usuario_id)

            if not pedidos:
                await step_context.context.send_activity(f"Não encontrei um histórico de compras para o usuário ID {usuario_id}.")
                return await step_context.end_dialog()

            # Processamento e agregação dos dados
            numero_de_pedidos = len(pedidos)
            valor_total_gasto = sum(pedido.get('valorTotal', 0) for pedido in pedidos)
            todos_os_produtos = []
            for pedido in pedidos:
                todos_os_produtos.extend(pedido.get('produtos', []))

            # Cria e envia o Cartão Adaptativo
            card = self._criar_cartao_extrato(
                usuario_id, numero_de_pedidos, valor_total_gasto, todos_os_produtos
            )
            await step_context.context.send_activity(MessageFactory.attachment(card))

        except Exception as e:
            print(f"Erro no diálogo de extrato: {e}")
            await step_context.context.send_activity("Desculpe, não consegui gerar o extrato agora. Tente novamente mais tarde.")

        return await step_context.end_dialog()


    def _criar_cartao_extrato(
        self, usuario_id: str, num_pedidos: int, valor_total: float, produtos: list
    ) -> Attachment:
        """Cria um Cartão Adaptativo para exibir o extrato consolidado."""
        # A função que cria o cartão continua exatamente a mesma
        card_body = [
            {
                "type": "TextBlock",
                "text": "Extrato de Compras",
                "weight": "Bolder",
                "size": "ExtraLarge"
            },
            {
                "type": "TextBlock",
                "text": f"Resumo para o usuário ID: {usuario_id}",
                "isSubtle": True,
                "spacing": "None"
            },
            {
                "type": "FactSet",
                "facts": [
                    {"title": "Total de Pedidos:", "value": str(num_pedidos)},
                    {"title": "Valor Total Gasto:", "value": f"R$ {valor_total:.2f}"},
                ],
                "separator": True,
                "spacing": "Large"
            },
            {
                "type": "TextBlock",
                "text": "HISTÓRICO DE ITENS COMPRADOS",
                "weight": "Bolder",
                "separator": True,
                "spacing": "Large"
            }
        ]
        if not produtos:
            card_body.append({"type": "TextBlock", "text": "Nenhum item encontrado."})
        else:
            for produto in produtos:
                card_body.append({
                    "type": "TextBlock",
                    "text": f"• {produto.get('productName', 'N/A')} - R$ {produto.get('price', 0.0):.2f}",
                    "wrap": True,
                    "spacing": "Small"
                })
        card_json = {
            "type": "AdaptiveCard", "version": "1.5", "body": card_body,
            "$schema": "http://adaptivecards.io/schemas/adaptive-card.json"
        }
        return CardFactory.adaptive_card(card_json)