package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Pedido;
import ibmec_ecommerce.ecommerce.Repository.Cosmos.PedidoRepositorio;
import ibmec_ecommerce.ecommerce.Request.RelatorioVendas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/relatorio-vendas")
public class RelatorioVendasController {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    // Método para gerar o relatório de vendas
    @GetMapping
    public ResponseEntity<RelatorioVendas> gerarRelatorioVendas() {
        // Buscar todos os pedidos no banco de dados
        Iterable<Pedido> pedidos = pedidoRepositorio.findAll();

        // Converter o Iterable para uma lista
        List<Pedido> pedidosList = StreamSupport.stream(pedidos.spliterator(), false)
                .collect(Collectors.toList());

        // Agregar dados para o relatório
        Long totalPedidos = (long) pedidosList.size();
        Double totalVendas = pedidosList.stream().mapToDouble(Pedido::getValorTotal).sum();

        // Filtrando os pedidos concluídos
        Long totalPedidosConcluidos = pedidosList.stream()
                .filter(pedido -> pedido.getStatus().equals("Concluído"))
                .count();
        Double totalVendasConcluidas = pedidosList.stream()
                .filter(pedido -> pedido.getStatus().equals("Concluído"))
                .mapToDouble(Pedido::getValorTotal)
                .sum();

        // Criar o objeto de relatório
        RelatorioVendas relatorio = new RelatorioVendas(totalPedidos, totalVendas, totalPedidosConcluidos, totalVendasConcluidas);

        // Retornar o relatório como resposta
        return new ResponseEntity<>(relatorio, HttpStatus.OK);
    }
}
