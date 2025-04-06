package ibmec_ecommerce.ecommerce.Request;

public class TransacaoRequest {
    private String numero;
    private String cvv;
    private Double valor;

    // Construtor vazio (necessário para deserialização JSON)
    public TransacaoRequest() {}

    // Construtor com parâmetros
    public TransacaoRequest(String numero, String cvv, Double valor) {
        this.numero = numero;
        this.cvv = cvv;
        this.valor = valor;
    }

    // Getters e Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
