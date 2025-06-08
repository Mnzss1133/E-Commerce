package ibmec_ecommerce.ecommerce.Repository;

import ibmec_ecommerce.ecommerce.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {
}

