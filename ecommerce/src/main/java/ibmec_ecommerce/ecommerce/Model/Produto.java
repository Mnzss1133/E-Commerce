package ibmec_ecommerce.ecommerce.Model;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.Data;


@Data
@Container(containerName = "products")
public class Produto {


        @Id
        private String id;

        @PartitionKey
        private String productCategory;

        private String productName;

        private double price;

        private List<String> imageUrl;

        private String productDescription;



}

