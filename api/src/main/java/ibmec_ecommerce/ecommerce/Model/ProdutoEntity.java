package ibmec_ecommerce.ecommerce.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "produto")
public class ProdutoEntity {

    @Id
    private String id;

    @Column
    private String productName;

    @Column
    private double price;
}

