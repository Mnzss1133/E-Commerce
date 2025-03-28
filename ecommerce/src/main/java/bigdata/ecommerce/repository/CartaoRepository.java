package bigdata.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import bigdata.ecommerce.model.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Integer> {
}
