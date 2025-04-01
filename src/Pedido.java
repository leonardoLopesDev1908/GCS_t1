import java.util.ArrayList;
import java.util.List;
import java.time.*;

public class Pedido {

    private List<Item> itens;
    private LocalDate data;
    private double valor;

    public Pedido(){
        this.itens = new ArrayList<>();
        this.data = LocalDate.now();;
        this.valor = getPrecoTotal();
    }

    public List<Item> getItens(){
        return itens;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    private double getPrecoTotal(){
        double total = 0;
        for (Item item : itens) {
            total += item.getPreco();
        }
        return total;
    }
}
