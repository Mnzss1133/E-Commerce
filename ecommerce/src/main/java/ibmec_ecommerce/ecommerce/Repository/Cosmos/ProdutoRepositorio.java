package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import org.springframework.stereotype.Repository;
import ibmec_ecommerce.ecommerce.Model.Produto;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
@Repository

public interface ProdutoRepositorio extends CosmosRepository<Produto, String> {


}
