import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Pedido {

    enum Status {
        EM_ANALISE, APROVADO, REJEITADO
    }
    private Status status;
    private static int LAST_ID = 1000;
    private int id;
    private List<Item> itens;
    private LocalDate data;
    private double valor;
    private Departamento depto;
    private Funcionario func;
    private String descricao;

    public Pedido(Departamento depto, Funcionario func, List<Item> itens, String descricao){
        this.itens = itens;
        this.data = LocalDate.now();
        this.valor = getPrecoTotal();
        this.id = ++LAST_ID;
        this.depto = depto;
        this.func = func;
        this.status = Status.EM_ANALISE;
        this.descricao = descricao;
    }

    public Pedido(double valor){
        this.valor = valor;
    }

    public int getId(){
        return id;
    }

    public Status getStatus(){
        return status;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    public Funcionario getFunc(){
        return func;
    }

    public String getDescricao() {
        return descricao;
    }

    public void rejeitarPedido(){
        this.status = Status.REJEITADO;
    }

    public void aprovarPedido(){
        this.status = Status.APROVADO;
    }

    @Override
    public String toString(){
        return String.format("ID do pedido: %d"+
                "\nStatus: %s"+
                "\nData: %s"+
                "\nFuncionário solicitante: %s" +
                "\nDepartamento solicitante: %s" +
                "\nValor total: R$%.2f" +
                "\nDescrição do pedido: %s" +
                "\nLista de itens: %s",
                this.id, this.status, this.data, this.func, this.depto.getName(),
                this.valor, this.descricao,         this.itens.stream()
                        .map(Item::toString)
                        .collect(Collectors.joining("\n- ")));
    }

    private double getPrecoTotal(){
        double total = 0;
        for (Item item : itens) {
            total += item.getPreco();
        }
        return total;
    }

}
