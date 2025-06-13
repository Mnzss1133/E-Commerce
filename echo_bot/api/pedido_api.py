import requests

class PedidoAPI:
    def consultar_pedidos(self, usuario_id):
        # Define a URL da API para buscar os pedidos do usuário
        url = f"http://localhost:8080/usuario/{usuario_id}/pedidos"  # Altere para a URL correta da sua API
        response = requests.get(url)

        if response.status_code == 200:
            return response.json()  # Retorna os pedidos como um dicionário (json)
        else:
            return None  # Se a resposta for diferente de 200, retorna None
