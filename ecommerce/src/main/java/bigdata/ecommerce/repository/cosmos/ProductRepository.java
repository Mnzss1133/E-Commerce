package bigdata.ecommerce.repository.cosmos;
import org.springframework.stereotype.Repository;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import bigdata.ecommerce.model.Product;
@Repository
public interface ProductRepository extends CosmosRepository<Product, String> {

}
