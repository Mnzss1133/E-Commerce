package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import ibmec_ecommerce.ecommerce.Model.Pedido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends CrudRepository<Pedido, String> {
    // Métodos personalizados podem ser adicionados aqui
}
