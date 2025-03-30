public class Funcionario {

    private String name;
    private String cargo;
    private Departamento departamento;

    public Funcionario(String name, String cargo) {
        this.name = name;
        this.cargo = cargo;
    }

    public void vincularDepto(Departamento depto) {
        this.departamento = depto;
    }

    public String getName() {
        return name;
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.cargo;
    }
}
