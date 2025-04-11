public class Funcionario {

    private static int LAST_ID = 0;
    private int id;
    private final String name;
    private String cargo;
    private Departamento departamento;

    public Funcionario(String name, String cargo) {
        this.name = name;
        this.cargo = cargo;
        this.id = ++LAST_ID;
    }

    public Funcionario(String name){
        this.name = name;
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

    public int getId() {
        return id;
    }

    public Departamento getDepartamento(){
        return departamento;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.cargo;
    }
}
