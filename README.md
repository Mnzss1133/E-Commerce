# E-Commerce


Endpoints para gerenciamento de usuários, configurações e tickers monitorados.
---

## **Rotas Disponíveis**

### **1. Cadastrar Usuário**
**`POST /usuario`**  
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
### **2. Alterar Usuário**
**`PUT /usuario`**  
Alterar um novo usuário.

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

### **3. Obter Usuários**
**`GET /usuario`**
Retorna todos os usuários existentes.



### **5. Cadastrar Produtos**
**`POST /produtos`**
**`GET /produtos/{produtoID}`**
Adiciona produto/Retorna produto.

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

### **6. Deletar Usuário**
**`DELETE /usuario/{user_id}`**
Deleta um usuário existente.

### **7. Criar Cartao**
**`POST /usuario/{user_id}/cartoes`**
```json
{
  
  "numero": "1234-5678-9012-3456",
  "dtExpiracao": "2025-12-31T23:59:59",
  "cvv": "123",
  "saldo": 1500.75
}

```

### **8. Criar Endereco**
**`POST /usuario/{user_id}/cartoes`**
```json
{
  
  "logradouro": "Rua das Flores",
  "complemento": "Apto 202",
  "bairro": "Centro",
  "cidade": "Rio de Janeiro",
  "estado": "RJ",
  "cep": "20000-000"
  
  
}
`
```
Integrantes:
João Pedro Menezes, Breno França
