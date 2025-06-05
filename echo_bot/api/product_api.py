import requests
from config import DefaultConfig

class ProductAPI:
    def __init__(self):
        self.config = DefaultConfig()
        self.base_url = f"{self.config.URL_PREFIX}/products"  # Direto na rota de produtos

    def get_products(self):
        """
        Obtém todos os produtos (útil para listagens gerais)
        """
        try:
            response = requests.get(self.base_url)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            return {"error": str(e)}

    def search_product(self, product_name: str):
        """
        Busca um produto pelo nome usando a rota /products/search
        Ideal para interação com chatbot
        """
        try:
            response = requests.get(
                f"{self.config.URL_PREFIX}/products/search",
                params={"productName": product_name}
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            return {"error": str(e)}
