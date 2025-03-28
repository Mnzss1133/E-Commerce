package bigdata.ecommerce.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.Data;

@Data
@Container(containerName = "products")
public class Product {

    @Id
    private String id;

    @PartitionKey
    private String categoryProduct;

    private String nameProduct;

    private double ProductPrice;

    private List<String> URLImages;

    private String ProductDescription;



}