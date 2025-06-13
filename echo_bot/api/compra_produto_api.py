import requests

class CompraProdutoAPI:
    def comprar_produto(self, usuario_id, produto_nome, produto_preco):
        # Defina a URL da API que será chamada para realizar a compra
        url = f"http://localhost:8080/usuario/{usuario_id}/comprar"  # Altere para a URL da sua API de compra

        # Parâmetros necessários para a compra
        params = {
            "produto_nome": produto_nome,
            "produto_preco": produto_preco,
        }

        response = requests.post(url, json=params)

        if response.status_code == 200:
            return response.json()  # Retorna a resposta com o sucesso da compra
        else:
            return None  # Se a resposta for diferente de 200, retorna None
