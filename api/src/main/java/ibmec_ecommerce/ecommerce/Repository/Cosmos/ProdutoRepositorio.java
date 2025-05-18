package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import ibmec_ecommerce.ecommerce.Model.Produto;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProdutoRepositorio extends CosmosRepository<Produto, String> {
    Optional<List<Produto>> findByProductNameContains(String productName);

}
