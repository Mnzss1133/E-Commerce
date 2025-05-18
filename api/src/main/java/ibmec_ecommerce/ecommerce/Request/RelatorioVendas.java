package ibmec_ecommerce.ecommerce.Request;

public class RelatorioVendas {

    private Long totalPedidos;       // Número total de pedidos
    private Double totalVendas;      // Valor total das vendas
    private Long totalPedidosConcluidos; // Número de pedidos concluídos
    private Double totalVendasConcluidas; // Valor total das vendas concluídas

    // Construtores
    public RelatorioVendas(Long totalPedidos, Double totalVendas, Long totalPedidosConcluidos, Double totalVendasConcluidas) {
        this.totalPedidos = totalPedidos;
        this.totalVendas = totalVendas;
        this.totalPedidosConcluidos = totalPedidosConcluidos;
        this.totalVendasConcluidas = totalVendasConcluidas;
    }

    // Getters e Setters
    public Long getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Long totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public Double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(Double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public Long getTotalPedidosConcluidos() {
        return totalPedidosConcluidos;
    }

    public void setTotalPedidosConcluidos(Long totalPedidosConcluidos) {
        this.totalPedidosConcluidos = totalPedidosConcluidos;
    }

    public Double getTotalVendasConcluidas() {
        return totalVendasConcluidas;
    }

    public void setTotalVendasConcluidas(Double totalVendasConcluidas) {
        this.totalVendasConcluidas = totalVendasConcluidas;
    }
}
