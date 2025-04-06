package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Pedido;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.PedidoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido pedidoCriado = pedidoRepositorio.save(pedido);
        return new ResponseEntity<>(pedidoCriado, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable String id) {
        Optional<Pedido> pedido = pedidoRepositorio.findById(id);
        return pedido.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = (List<Pedido>) pedidoRepositorio.findAll();
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }
}
