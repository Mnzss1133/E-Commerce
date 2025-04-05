package ibmec_ecommerce.ecommerce.Model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;
import lombok.Data;

import java.util.List;

@Data
@Container(containerName = "products")  // Define o container no Cosmos DB
public class Produto {

        @Id
        private String id;  // ID único do produto

        @PartitionKey  // Chave de partição para distribuir dados
        private String productCategory;  // Categoria do produto

        private String productName;  // Nome do produto
        private double price;  // Preço do produto
        private List<String> imageUrl;  // URLs das imagens
        private String productDescription;  // Descrição do produto
}
