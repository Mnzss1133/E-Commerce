package ibmec_ecommerce.ecommerce.Request;

import ibmec_ecommerce.ecommerce.Model.Usuario;
import lombok.Data;

@Data
public class TransacaoResponse {

    private String status;        // Status da transação (ex: "SUCESSO", "FALHA")
    private String mensagem;      // Mensagem informando detalhes da transação
    private Double valor;         // Valor da transação
    private String numeroCartao;  // Últimos 4 dígitos do cartão de crédito (para segurança)
    private String dataHora;      // Data e hora da transação
    private Long idTransacao;     // ID único da transação para referência

    // Construtor
    public TransacaoResponse(String status, String mensagem, Double valor, String numeroCartao, String dataHora, Long idTransacao) {
        this.status = status;
        this.mensagem = mensagem;
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.dataHora = dataHora;
        this.idTransacao = idTransacao;
    }
}

