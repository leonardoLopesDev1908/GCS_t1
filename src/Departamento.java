import java.util.ArrayList;
import java.util.List;

public class Departamento {

    String name;
    List<Funcionario> funcionarios = new ArrayList<>();
    Empresa empresa;

    public Departamento(String name){
        this.name = name;
    }

    public Departamento(String name, Empresa empresa) {
        this.name = name;
        this.empresa = empresa;
    }

    public List<Funcionario> getFuncionarios(){
        return funcionarios;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

}
