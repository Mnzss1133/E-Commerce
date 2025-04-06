package ibmec_ecommerce.ecommerce.Repository;
import ibmec_ecommerce.ecommerce.Model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepositorio extends JpaRepository<Cartao, Integer> {
}
