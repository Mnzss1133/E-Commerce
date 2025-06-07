package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Produto;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.ProdutoRepositorio;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto produtoCriado = produtoRepositorio.save(produto);
        return new ResponseEntity<>(produtoCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable String id) {
        Optional<Produto> produto = produtoRepositorio.findById(id);
        return produto.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = (List<Produto>) produtoRepositorio.findAll();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Produto>> buscarProdutos(@RequestParam String productName) {
        List<Produto> produtos = produtoRepositorio.findByProductNameContaining(productName);
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) {
        try {
            System.out.println("Tentando deletar produto com ID: " + id);

            Optional<Produto> produto = produtoRepositorio.findById(id);

            if (produto.isEmpty()) {
                System.out.println("Produto não encontrado com ID: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            System.out.println("Produto Encontrado " + produto.get());
            produtoRepositorio.delete(produto.get());

            System.out.println("Produto deletado com sucesso!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            System.err.println("ERRO DETALHADO ao deletar produto: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable String id, @RequestBody Produto produtoAtualizado) {
        try {
            System.out.println("Tentando atualizar produto com ID: " + id);
            System.out.println("Dados recebidos: " + produtoAtualizado);

            Optional<Produto> produtoExistente = produtoRepositorio.findById(id);

            if (produtoExistente.isEmpty()) {
                System.out.println("Produto não encontrado com ID: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Produto produtoOriginal = produtoExistente.get();
            System.out.println("Produto  encontrado: " + produtoOriginal);

            // IMPORTANTE: Manter o ID e NÃO alterar a partition key se ela existir
            produtoAtualizado.setId(id);

            // Se productName for partition key, manter o valor original
            if (produtoAtualizado.getProductName() == null || produtoAtualizado.getProductName().isEmpty()) {
                produtoAtualizado.setProductName(produtoOriginal.getProductName());
            }

            System.out.println("Salvando produto atualizado: " + produtoAtualizado);
            Produto produtoSalvo = produtoRepositorio.save(produtoAtualizado);

            System.out.println("Produto atualizado com sucesso: " + produtoSalvo);
            return new ResponseEntity<>(produtoSalvo, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("ERRO DETALHADO ao atualizar produto: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
