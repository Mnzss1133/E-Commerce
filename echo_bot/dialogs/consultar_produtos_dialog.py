from botbuilder.dialogs import ComponentDialog, WaterfallDialog, WaterfallStepContext
from botbuilder.core import MessageFactory
from botbuilder.dialogs.prompts import TextPrompt, PromptOptions
from api.product_api import ProductAPI
from botbuilder.schema import HeroCard, CardImage, Attachment, CardAction, ActionTypes
from datetime import datetime, timezone
import logging

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
            
            # Processar URLs de imagem de forma mais robusta
            image_urls = produto.get("imageUrl", [])
            
            # Normalizar imageUrl para lista
            if isinstance(image_urls, str):
                # Se for string, pode conter múltiplas URLs separadas por vírgula, ponto e vírgula, ou espaço
                separadores = [',', ';', '|', ' ']
                urls_separadas = [image_urls]
                
                # Tentar separar por diferentes delimitadores
                for sep in separadores:
                    if sep in image_urls:
                        urls_separadas = [url.strip() for url in image_urls.split(sep) if url.strip()]
                        break
                
                image_urls = urls_separadas
            elif not isinstance(image_urls, list):
                image_urls = []
            
            # Processar e validar URLs de imagem
            valid_images = []
            for i, url in enumerate(image_urls):
                if url and isinstance(url, str):
                    # Limpar e validar URL
                    clean_url = url.strip()
                    if self._is_valid_image_url(clean_url):
                        valid_images.append(CardImage(url=clean_url))
                        logging.info(f"Imagem {i+1} adicionada: {clean_url}")
                
                # Limitar a 5 imagens para não sobrecarregar o card
                if len(valid_images) >= 5:
                    logging.info(f"Limitando a 5 imagens (total encontradas: {len(image_urls)})")
                    break
            
            # Se não tiver imagens válidas, usar placeholder
            if not valid_images:
                placeholder_url = "https://via.placeholder.com/300x200/007bff/ffffff?text=Sem+Imagem"
                valid_images = [CardImage(url=placeholder_url)]
                logging.info("Usando imagem placeholder")
            
            # Criar HeroCard com informações mais detalhadas
            total_images = len(valid_images)
           
            
            card = HeroCard(
                title=produto.get("nome", produto.get("productName", "Produto Sem Nome")),
                subtitle=f"ID: {produto.get('id', 'N/A')} | Código: {produto.get('codigo', 'N/A')}",
                text=self._format_product_description(produto),
                images=valid_images,
                buttons=[
                    CardAction(
                        type=ActionTypes.open_url,
                        title="Ver Detalhes",
                        value=f"http://localhost:8080/produtos/{produto.get('id', '')}"
                    )
                ] if produto.get('id') else []
            )
            
            # Criar attachment do card
            card_attachment = Attachment(
                content_type="application/vnd.microsoft.card.hero",
                content=card
            )
            
            # Enviar card
            try:
                await step_context.context.send_activity(
                    MessageFactory.attachment(card_attachment)
                )
                logging.info(f"Card enviado com sucesso para produto: {produto.get('nome', 'N/A')}")
            except Exception as e:
                logging.error(f"Erro ao enviar card: {str(e)}")
                # Fallback para mensagem de texto
                await self._send_text_fallback(step_context, produto)
        else:
            await step_context.context.send_activity(
                MessageFactory.text("❌ Nenhum produto encontrado com esse nome.")
            )
        
        return await step_context.end_dialog()
    
    def _is_valid_image_url(self, url):
        """Valida se a URL é válida para imagem"""
        if not url:
            return False
        
        # Verificar se começa com http/https
        if not (url.startswith('http://') or url.startswith('https://')):
            return False
        
        # Verificar extensões de imagem comuns
        image_extensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp']
        url_lower = url.lower()
        
        # Se termina com extensão de imagem, é válida
        if any(url_lower.endswith(ext) for ext in image_extensions):
            return True
        
        # Se contém parâmetros de query mas pode ser uma imagem (como placeholders)
        if 'placeholder' in url_lower or 'image' in url_lower:
            return True
        
        # URLs que não terminam com extensão específica também podem ser válidas
        # (muitas APIs retornam URLs dinâmicas)
        return True
    
    def _format_product_description(self, produto):
        """Formata a descrição do produto de forma mais legível"""
        descricao = produto.get('descricao', produto.get('productDescription', ''))
        preco = produto.get('preco', produto.get('price', ''))
        categoria = produto.get('categoria', produto.get('category', ''))
        
        text_parts = []
        
        if descricao:
            text_parts.append(f" **Descrição:** {descricao}")
        
        if preco:
            text_parts.append(f" **Preço:** R$ {preco}")
        
        if categoria:
            text_parts.append(f" **Categoria:** {categoria}")
        
        return "\n\n".join(text_parts) if text_parts else "Informações não disponíveis"
    
    async def _send_text_fallback(self, step_context, produto):
        """Enviar informações do produto como texto quando o card falha"""
        texto = f"""
    **{produto.get('nome', produto.get('productName', 'Produto'))}**
    **ID:** {produto.get('id', 'N/A')}
    **Descrição:** {produto.get('descricao', produto.get('productDescription', 'Sem descrição'))}
"""
        if produto.get('preco'):
            texto += f"\n💰 **Preço:** R$ {produto.get('preco')}"
        
        await step_context.context.send_activity(MessageFactory.text(texto))