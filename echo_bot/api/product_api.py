import requests
import logging

class ProductAPI:
    """
    API para interagir com os endpoints de Produtos.
    """
    BASE_URL = "http://localhost:8080"

    def consultar_produtos(self, product_name: str) -> list:
        """Busca produtos por nome."""
        try:
            url = f"{self.BASE_URL}/produtos/search"
            params = {'productName': product_name}
            response = requests.get(url, params=params)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            logging.error(f"Erro ao buscar produto: {e}")
            return []