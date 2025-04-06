package ibmec_ecommerce.ecommerce.Repository;

import ibmec_ecommerce.ecommerce.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}
