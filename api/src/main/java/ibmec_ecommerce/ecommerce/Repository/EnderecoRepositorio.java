package ibmec_ecommerce.ecommerce.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ibmec_ecommerce.ecommerce.Model.Endereco;
@Repository
public interface EnderecoRepositorio extends JpaRepository<Endereco, Integer> {

}
