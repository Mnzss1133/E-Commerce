import requests

class ProductAPI:
    def consultar_produtos(self, product_name):
        response = requests.get("http://localhost:8080/products/search", params={"productName": product_name})
        if response.status_code == 200:
            return response.json()
        else:
            return None

