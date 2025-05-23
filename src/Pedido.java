import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Pedido {

    enum Status {
        EM_ANALISE, APROVADO, REJEITADO
    }
    Random rand = new Random();
    private Status status;
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
        this.id = rand.nextInt(100000);
        this.depto = depto;
        this.func = func;
        this.status = Status.EM_ANALISE;
        this.descricao = descricao;
    }

    public Pedido(Departamento depto, List<Item> itens, String descricao) {
        this.itens = itens;
        this.data = LocalDate.now();
        this.valor = getPrecoTotal();
        this.id = rand.nextInt(100000);
        this.depto = depto;
        this.func = new Funcionario("Administrador");
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

    public Departamento getDepartamento(){
        return depto;
    }

    public String getItens(){
        return String.valueOf(itens);
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
        return String.format("""
                             ID do pedido: %d
                             Status: %s
                             Data: %s
                             Funcion\u00e1rio solicitante: %s
                             Departamento solicitante: %s
                             Valor total: R$%.2f
                             Descri\u00e7\u00e3o do pedido: %s
                             Lista de itens: %s""",
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
