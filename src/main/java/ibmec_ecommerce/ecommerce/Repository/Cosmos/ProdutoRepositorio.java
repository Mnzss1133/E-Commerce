package ibmec_ecommerce.ecommerce.Repository.Cosmos;

import ibmec_ecommerce.ecommerce.Model.Produto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepositorio extends ReactiveCrudRepository<Produto, String> {
    // Métodos personalizados podem ser adicionados aqui
}
