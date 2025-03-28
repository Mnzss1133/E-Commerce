package bigdata.ecommerce.controller;

import bigdata.ecommerce.model.Pedido;
import bigdata.ecommerce.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        pedido.setId(UUID.randomUUID().toString());

        if (pedido.getItens() != null) {
            double total = pedido.getItens().stream()
                    .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                    .sum();
            pedido.setTotal(total);
        }

        repository.save(pedido);
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Pedido> obterPorId(@PathVariable String id) {
        Optional<Pedido> pedido = repository.findById(id);
        return pedido.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = repository.findAll();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}