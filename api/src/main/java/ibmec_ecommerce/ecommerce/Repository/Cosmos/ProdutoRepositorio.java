package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import ibmec_ecommerce.ecommerce.Model.Produto;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepositorio extends CosmosRepository<Produto, String> {
    // Métodos personalizados podem ser adicionados aqui
}
