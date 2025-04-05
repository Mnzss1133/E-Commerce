package ibmec_ecommerce.ecommerce.Model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Container(containerName = "pedidos")  // Define o container para pedidos no Cosmos DB
public class Pedido {

    @Id
    private String id;  // ID único do pedido

    @PartitionKey  // A chave de partição pode ser o ID do usuário, por exemplo
    private String usuarioId;  // ID do usuário que fez o pedido

    private List<String> produtosIds;  // Lista de IDs de produtos no pedido

    private Double valorTotal;  // Valor total do pedido
    private String status;  // Status do pedido (ex: "Pendente", "Concluído")

    private LocalDateTime dataCriacao;  // Data de criação do pedido
    private LocalDateTime dataConclusao;  // Data de conclusão do pedido

    private String cartaoCreditoId;  // ID do cartão de crédito utilizado no pedido

    public Pedido() {
        this.dataCriacao = LocalDateTime.now();  // Inicializa a data de criação com a data e hora atual
        this.status = "Pendente";  // Inicializa o status do pedido como "Pendente"
    }
}
