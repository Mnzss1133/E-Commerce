from botbuilder.dialogs import ComponentDialog, WaterfallDialog, WaterfallStepContext
from botbuilder.core import MessageFactory
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from api.pedido_api import PedidoAPI  # Importando PedidoAPI

class ConsultarPedidoDialog(ComponentDialog):
    def __init__(self):
        super(ConsultarPedidoDialog, self).__init__("ConsultarPedidoDialog")

        self.add_dialog(TextPrompt("idUsuarioPrompt"))

        self.add_dialog(
            WaterfallDialog(
                "consultarPedidoWaterfallDialog",
                [
                    self.prompt_usuario_id_step,  # Solicitar o ID do usuário
                    self.consultar_pedidos_step,  # Consultar pedidos com o ID do usuário
                ],
            )
        )

        self.initial_dialog_id = "consultarPedidoWaterfallDialog"

    async def prompt_usuario_id_step(self, step_context: WaterfallStepContext):
        # Solicita o ID do usuário
        prompt_message = MessageFactory.text("Por favor, digite o ID do usuário para consultar os pedidos.")
        
        return await step_context.prompt(
            "idUsuarioPrompt",
            PromptOptions(prompt=prompt_message)
        )
    
    async def consultar_pedidos_step(self, step_context: WaterfallStepContext):
        usuario_id = step_context.result  # O ID do usuário fornecido pelo usuário
        
        # Agora, vamos fazer a requisição para o backend Java usando o ID do usuário
        pedido_api = PedidoAPI()  # Instanciando PedidoAPI
        pedidos = pedido_api.consultar_pedidos(usuario_id)  # Chamando o método para buscar os pedidos
        
        if pedidos:
            # Aqui, processamos os pedidos e produtos retornados
            produtos_info = ""
            for pedido in pedidos:
                produtos_info += f"Pedido ID: {pedido['id']}\n"
                for produto in pedido['produtos']:
                    produtos_info += f"- Produto: {produto['productName']} | Preço: {produto['price']}\n"
            # Envia a resposta com os produtos do pedido
            await step_context.context.send_activity(MessageFactory.text(produtos_info))
        else:
            await step_context.context.send_activity(MessageFactory.text(f"Não foram encontrados pedidos para o usuário ID {usuario_id}"))

        return await step_context.end_dialog()
