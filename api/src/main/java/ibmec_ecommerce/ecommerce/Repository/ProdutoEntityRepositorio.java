package ibmec_ecommerce.ecommerce.Repository;

import ibmec_ecommerce.ecommerce.Model.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoEntityRepositorio extends JpaRepository<ProdutoEntity, String> {
}
