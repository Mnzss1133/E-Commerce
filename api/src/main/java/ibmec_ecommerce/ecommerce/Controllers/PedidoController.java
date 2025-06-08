package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.*;
import ibmec_ecommerce.ecommerce.Repository.*;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.ProdutoRepositorio;
import ibmec_ecommerce.ecommerce.Request.PedidoRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/usuario/{idusuario}/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CartaoRepositorio cartaoRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private ProdutoEntityRepositorio produtoEntityRepositorio;


    @PostMapping
    public ResponseEntity<?> realizarPedido(@PathVariable int idusuario, @RequestBody PedidoRequest pedidoRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(idusuario);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        // Buscar cartão do usuário
        Cartao cartao = usuario.getCartoes().stream()
                .filter(c -> c.getNumero().equals(pedidoRequest.getNumeroCartao()) && c.getCvv().equals(pedidoRequest.getCvv()))
                .findFirst()
                .orElse(null);

        if (cartao == null || cartao.getDtExpiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cartão inválido ou expirado");
        }

        // Buscar produtos
        List<ProdutoEntity> produtos = new ArrayList<>();
        double valorTotal = 0;

        for (String idProduto : pedidoRequest.getProdutosIds()) {
        Optional<Produto> produtoOptional = produtoRepositorio.findById(idProduto);
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto com ID " + idProduto + " não encontrado");
        }
        Produto produto = produtoOptional.get();

        // Converter Produto (Cosmos) para ProdutoEntity (relacional)
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setId(produto.getId());
        produtoEntity.setProductName(produto.getProductName());
        produtoEntity.setPrice(produto.getPrice());
        produtoEntityRepositorio.save(produtoEntity);

        produtos.add(produtoEntity);
        valorTotal += produto.getPrice();
    }

        if (cartao.getSaldo() < valorTotal) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente no cartão");
        }

        // Debitar saldo
        cartao.setSaldo(cartao.getSaldo() - valorTotal);
        cartaoRepositorio.save(cartao);

        // Criar e salvar pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setProdutos(produtos);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setValorTotal(valorTotal);
        pedidoRepositorio.save(pedido);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos(@PathVariable int idusuario) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(idusuario);
        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Pedido> pedidos = pedidoRepositorio.findAll();
        List<Pedido> pedidosUsuario = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (pedido.getUsuario().getId().equals(idusuario)) {
                pedidosUsuario.add(pedido);
            }
        }

        return new ResponseEntity<>(pedidosUsuario, HttpStatus.OK);
    }
}

