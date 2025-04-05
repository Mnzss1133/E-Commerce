package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Produto;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepositorio repositorio;

    // Criar novo produto
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        produto.setId(UUID.randomUUID().toString());
        return repositorio.save(produto);
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable String id) {
        Optional<Produto> produto = repositorio.findById(id);

        return produto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos os produtos
    @GetMapping
    public List<Produto> listarProdutos() {
        return (List<Produto>) repositorio.findAll();
    }

    // Deletar produto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) {
        Optional<Produto> produto = repositorio.findById(id);

        if (produto.isPresent()) {
            repositorio.delete(produto.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
