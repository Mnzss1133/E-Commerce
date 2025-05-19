import requests
import json

class ProductAPI:
    def __init__(self):
        from config import DefaultConfig
        self.config = DefaultConfig()
        self.base_url = f"{self.config.URL_PREFIX}/api"
    
    def get_products(self):
        """
        Obtém a lista de produtos do e-commerce
        """
        try:
            response = requests.get(f"{self.base_url}/products")
            if response.status_code == 200:
                return response.json()
            else:
                return {"error": f"Erro ao buscar produtos: {response.status_code}"}
        except Exception as e:
            return {"error": f"Exceção ao buscar produtos: {str(e)}"}
    
    def get_product_by_id(self, product_id):
        """
        Obtém um produto específico pelo ID
        """
        try:
            response = requests.get(f"{self.base_url}/products/{product_id}")
            if response.status_code == 200:
                return response.json()
            else:
                return {"error": f"Erro ao buscar produto: {response.status_code}"}
        except Exception as e:
            return {"error": f"Exceção ao buscar produto: {str(e)}"}
    
    def get_orders(self):
        """
        Obtém a lista de pedidos do e-commerce
        """
        try:
            response = requests.get(f"{self.base_url}/orders")
            if response.status_code == 200:
                return response.json()
            else:
                return {"error": f"Erro ao buscar pedidos: {response.status_code}"}
        except Exception as e:
            return {"error": f"Exceção ao buscar pedidos: {str(e)}"}
