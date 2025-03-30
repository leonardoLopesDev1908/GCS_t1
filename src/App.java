import java.util.List;
import java.util.Scanner;

public class App {

    public static void executar() {
        Scanner sc = new Scanner(System.in);
        Empresa empresa = inicializar();

        menu(empresa);

    }

    public static Empresa inicializar() {

        Empresa empresa = new Empresa("Tech Soluções");

        Departamento recursosHumanos = criarDepartamento("Recursos Humanos", empresa,
                List.of(
                        new Funcionario("Julio Gomes", "Analista de contratações"),
                        new Funcionario("Marcia Lopes", "Gerente"),
                        new Funcionario("Paulo César", "Estagiário")
                ));

        Departamento informatica = criarDepartamento("Informática", empresa,
                List.of(
                        new Funcionario("Mauro Yamaguchi", "Técnico em informática"),
                        new Funcionario("Leoanrdo Lopes", "Gerente de TI"),
                        new Funcionario("Marcos Silva", "Estagiario"),
                        new Funcionario("Julia Espindonla", "Engenheira de Software")
                ));

        Departamento financeiro = criarDepartamento("Financeiro", empresa, List.of(
                        new Funcionario("Patricia Camargo", "Analista Contábil"),
                        new Funcionario("Paulo Omar", "Analista Contábil"),
                        new Funcionario("Lucas Guedes", "Gerente do Financeiro"),
                        new Funcionario("Vanessa da Mata", "Auditora de Vendas")
        ));

        Departamento comercial = criarDepartamento("Comercial", empresa, List.of(
                        new Funcionario("Neymar Jr.", "Vendedor"),
                        new Funcionario("Marta da Silva", "Vendedora"),
                        new Funcionario("Alan Patrick", "Gerente de Marketing"),
                        new Funcionario("Gabriela Maciel", "Estagiaria de Marketing")
        ));

        return empresa;
    }

    private static Departamento criarDepartamento(String nome, Empresa empresa, List<Funcionario> funcionarios){

        Departamento depto = new Departamento(nome);
        empresa.adicionarDepartamento(depto);

        for (Funcionario func : funcionarios) {
            depto.getFuncionarios().add(func);
            func.vincularDepto(depto);
        }
        return depto;
    }

    private static void menu(Empresa empresa) {

        Scanner sc = new Scanner(System.in);
        System.out.println("====== TECH SOLUÇÕES ======");

        System.out.println("\n0. Administrador");
        int i = 1;
        for(Departamento depto : empresa.getDepartamentos()) {
            System.out.println("\nDepartamento: " + depto.getName());
            System.out.println("-".repeat(45));


            for(Funcionario func : depto.getFuncionarios()) {
                System.out.printf("%2d. %-15s %s%n",
                        i++,
                        func.getName(),
                        "(" + func.getCargo() + ")");
            }
        }


        System.out.print("\nFazer login com o funcionário número: ");
        int escolha = sc.nextInt();

//        switch(escolha) {
//            case 1 -> menuFuncionario();
//        }

    }

    private static void menuFuncionario(Funcionario func) {

    }

}
