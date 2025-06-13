package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import ibmec_ecommerce.ecommerce.Model.Pedido;
import org.springframework.stereotype.Repository;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import java.util.List;

@Repository
public interface PedidoRepositorio extends CosmosRepository<Pedido, String> {
    
    // Método para buscar os pedidos pelo ID do usuário
    List<Pedido> findByUsuarioId(String usuarioId);
}
