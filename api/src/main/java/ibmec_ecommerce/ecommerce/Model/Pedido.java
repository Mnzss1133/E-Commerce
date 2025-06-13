package ibmec_ecommerce.ecommerce.Model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import lombok.Data;
import java.util.List;

@Data
@Container(containerName = "pedidos")
public class Pedido {

    @Id
    private String id;  // A chave primária para o pedido

    @PartitionKey  // Defina a chave de partição, por exemplo, pelo id do usuário
    private String usuarioId;  // ID do usuário que fez o pedido

    private List<ProdutoEntity> produtos;  // Alterado para ProdutoEntity

    private Double valorTotal;  // Valor total do pedido
    private String dataHora;  // Data e hora do pedido
}
