# E-Commerce


Endpoints para gerenciamento de usuários, configurações e tickers monitorados.
---

## **Rotas Disponíveis**

### **1. Cadastrar Usuário**
**`POST /usuarios`**  
Cria um novo usuário.

**Request Body (JSON):**
```json
{
    "nome": "string",
    "email": "string",
    "dtNascimento": "string",
    "cpf": "string",
    "telefone":"string"
    
}
```
### **2. Obter Usuário**
**`POST /usuarios/{usuario_id}/cartoes**
Retorna um usuário existente.

### **3. Obter Usuários**
**`GET /usuarios/{usuario_id}`**
Retorna todos os usuários existentes.



### **5. Cadastrar Produtos**
**`POST /produtos`**
Adiciona tracking tickers(moedas de rastreio) para um usuário.

**Request Body (JSON):**
```json
{
  "id": "12345",
  "productCategory": "eletronicos",
  "productName": "Smartphone XYZ",
  "price": 1999.99,
  "imageUrl": [
    "https://exemplo.com/imagem1.jpg",
    "https://exemplo.com/imagem2.jpg"
  ],
  "productDescription": "Um smartphone avançado com câmera de alta resolução"
}
```
### **6. Atualizar Usuário**
**`PUT /usuarios/{user_id}`**
Atualizar usuário
### **7. Deletar Usuário**
**`DELETE /usuarios/{user_id}`**
Deleta um usuário existente.

Integrantes:
João Pedro Menezes, Breno França
