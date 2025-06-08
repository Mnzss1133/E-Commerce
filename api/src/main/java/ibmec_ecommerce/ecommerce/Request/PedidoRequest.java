package ibmec_ecommerce.ecommerce.Request;

import java.util.List;

public class PedidoRequest {
    private List<String> produtosIds;
    private String numeroCartao;
    private String cvv;

    // Getters e Setters
    public List<String> getProdutosIds() {
        return produtosIds;
    }

    public void setProdutosIds(List<String> produtosIds) {
        this.produtosIds = produtosIds;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
