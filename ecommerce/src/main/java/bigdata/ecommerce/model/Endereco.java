package bigdata.ecommerce.model;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String cep;

    @Column
    private String logradouro;

    @Column
    private Integer numero;

    @Column
    private String complemento;

    @Column
    private String cidade;

    @Column
    private String estado;
}