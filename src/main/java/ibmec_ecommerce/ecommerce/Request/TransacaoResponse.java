package ibmec_ecommerce.ecommerce.Request;


import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class TransacaoResponse {

    private String status;        // Status da transação (ex: "SUCESSO", "FALHA")
    private String mensagem;      // Mensagem informando detalhes da transação
    private Double valor;         // Valor da transação
    private String numeroCartao;  // Últimos 4 dígitos do cartão de crédito (para segurança)
    private LocalDateTime dataHora;      // Data e hora da transação
    private Long idTransacao;     // ID único da transação para referência
    private UUID codigoAutorizacao; // Código único de autorização gerado com UUID

    // Construtor
    public TransacaoResponse(String status, String mensagem, Double valor, String numeroCartao, LocalDateTime dataHora, Long idTransacao) {
        this.status = status;
        this.mensagem = mensagem;
        this.valor = valor;
        this.numeroCartao = numeroCartao;
        this.dataHora = dataHora;
        this.idTransacao = idTransacao;
        this.codigoAutorizacao = UUID.randomUUID();
    }

// Construtor sem Parametros
    public TransacaoResponse() {
        // Inicializa os campos, se necessário, com valores padrão
    }
}

