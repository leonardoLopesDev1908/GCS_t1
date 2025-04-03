import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private String name;
    private List<Departamento> departamentos = new ArrayList<>();
    private List<Funcionario> todosFuncionarios =new ArrayList<>();
    private List<Pedido> todosPedidos = new ArrayList<>();

    public Empresa(String name) {
        this.name = name;
    }

    public void adicionarDepartamento(Departamento dept) {
        this.departamentos.add(dept);
        dept.empresa = this;
    }

    public String getNome() {
        return name;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public List<Funcionario> getTodosFuncionarios(){
        return todosFuncionarios;
    }

    public List<Pedido> getTodosPedidos(){
        return todosPedidos;
    }

    public void adicionarPedido(Pedido pedido){
        todosPedidos.add(pedido);
    }
}
