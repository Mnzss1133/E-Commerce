package bigdata.ecommerce.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoRequest {
    private String numero;
    private String cvv;
    private Double valor;
}

