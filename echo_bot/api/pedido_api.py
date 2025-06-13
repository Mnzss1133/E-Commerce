import requests
import logging

class PedidoAPI:
    """
    API para interagir com os endpoints de Pedidos (Consultar e Criar).
    """
    BASE_URL = "http://localhost:8080"

    def consultar_pedidos(self, usuario_id: int) -> list:
        """Busca os pedidos de um utilizador."""
        try:
            url = f"{self.BASE_URL}/usuario/{usuario_id}/pedidos"
            response = requests.get(url)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            logging.error(f"Erro ao consultar pedidos do utilizador {usuario_id}: {e}")
            return []

    def criar_pedido(self, user_id: int, product_ids: list, card_number: str, cvv: str) -> dict:
        """Cria um novo pedido."""
        try:
            url = f"{self.BASE_URL}/usuario/{user_id}/pedidos"
            payload = {
                "produtosIds": product_ids,
                "numeroCartao": card_number,
                "cvv": cvv
            }
            response = requests.post(url, json=payload)
            response.raise_for_status() # Lança uma exceção para erros 4xx/5xx.
            return {"success": True, "data": response.json()}
        except requests.exceptions.RequestException as e:
            logging.error(f"Erro ao criar pedido: {e}")
            
            # **INÍCIO DA CORREÇÃO: Tratamento de erro melhorado**
            # Agora capturamos mais detalhes do erro da API.
            if e.response is not None:
                status_code = e.response.status_code
                details = e.response.text or "Nenhum detalhe adicional fornecido pela API."
                message = f"API retornou erro {status_code}. Resposta: '{details}'"
            else:
                message = "Não foi possível conectar à API. Verifique se ela está a ser executada."
            
            return {"success": False, "message": message}
           