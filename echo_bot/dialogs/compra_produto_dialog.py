from botbuilder.dialogs import ComponentDialog, WaterfallDialog, WaterfallStepContext
from botbuilder.core import MessageFactory
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from api.compra_produto_api import CompraProdutoAPI  # Importando a API

class CompraProdutoDialog(ComponentDialog):
    def __init__(self):
        super(CompraProdutoDialog, self).__init__("CompraProdutoDialog")

        self.add_dialog(TextPrompt("idUsuarioPrompt"))
        self.add_dialog(TextPrompt("produtoNomePrompt"))

        self.add_dialog(
            WaterfallDialog(
                "compraProdutoWaterfallDialog",
                [
                    self.prompt_usuario_id_step,  # Solicitar o ID do usuário
                    self.prompt_produto_nome_step,  # Solicitar o nome do produto
                    self.comprar_produto_step,  # Efetuar a compra
                ],
            )
        )

        self.initial_dialog_id = "compraProdutoWaterfallDialog"

    async def prompt_usuario_id_step(self, step_context: WaterfallStepContext):
        # Solicita o ID do usuário
        prompt_message = MessageFactory.text("Por favor, digite o seu ID de usuário para realizar a compra.")
        
        return await step_context.prompt(
            "idUsuarioPrompt",
            PromptOptions(prompt=prompt_message)
        )
    
    async def prompt_produto_nome_step(self, step_context: WaterfallStepContext):
        usuario_id = step_context.result  # O ID do usuário fornecido pelo usuário
        
        # Solicita o nome do produto
        prompt_message = MessageFactory.text("Por favor, digite o nome do produto que você deseja comprar.")
        
        return await step_context.prompt(
            "produtoNomePrompt",
            PromptOptions(prompt=prompt_message)
        )
    
    async def comprar_produto_step(self, step_context: WaterfallStepContext):
        usuario_id = step_context._result[0]  # O ID do usuário fornecido
        produto_nome = step_context.result  # O nome do produto fornecido pelo usuário

        # Chama a API para realizar a compra
        produto_api = CompraProdutoAPI()
        produto_preco = 100.00  # Aqui você pode buscar o preço real do produto se necessário

        resultado_compra = produto_api.comprar_produto(usuario_id, produto_nome, produto_preco)

        if resultado_compra:
            await step_context.context.send_activity(
                MessageFactory.text(f"Compra realizada com sucesso! O produto {produto_nome} foi comprado por R${produto_preco}.")
            )
        else:
            await step_context.context.send_activity(
                MessageFactory.text(f"Houve um erro ao tentar realizar a compra do produto {produto_nome}.")
            )

        return await step_context.end_dialog()
