import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner sc = new Scanner(System.in);

    public static void executar() {
        Empresa empresa = inicializar();

        menu(empresa);

    }

    private static Empresa inicializar() {

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
            empresa.getTodosFuncionarios().add(func);
        }
        return depto;
    }

    private static void menu(Empresa empresa) {

        while(true) {
            limparTerminal();
            System.out.println("====== TECH SOLUÇÕES ======");

            System.out.println("\n0. Administrador");

            for (Departamento depto : empresa.getDepartamentos()) {
                System.out.println("\nDepartamento: " + depto.getName());
                System.out.println("-".repeat(45));


                for (Funcionario func : depto.getFuncionarios()) {
                    System.out.printf("%2d. %-15s %s%n",
                            func.getId(),
                            func.getName(),
                            "(" + func.getCargo() + ")");
                }
            }


            System.out.print("\nFazer login com o funcionário número: ");
            int escolha = sc.nextInt();
            sc.nextLine();

            Funcionario escolhido;
            for (Funcionario func : empresa.getTodosFuncionarios()) {
                if (func.getId() == escolha) {
                    menuFuncionario(func);
                }
            }
        }

    }

    private static void menuFuncionario(Funcionario func) {

        while (true) {
            limparTerminal();

            System.out.println("=== " + func.getName() + " ===");

            System.out.println("\nBarra de Funcionalidades: " +
                    "\n(1)Realizar novo pedido" +
                    "\n(2)Minhas informações" +
                    "\n(3)Retornar ao menu de funcionários");

            System.out.print("\nEscolha: ");

            int escolha = sc.nextInt();
            switch (escolha) {
                case 1 -> mostrarInformacoes(func);
                case 3 -> menu(func.getDepartamento().empresa);

            }
        }
    }

    private static void mostrarInformacoes(Funcionario func) {
        limparTerminal();
        System.out.println("SUAS INFORMAÇÕES\n");
        System.out.println("Id: " + func.getId());
        System.out.println("Nome: " + func.getName());
        System.out.println("Cargo: " + func.getCargo());
        System.out.println("Departamento: " + func.getDepartamento().getName());
        pausa();
    }


    private static void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pausa(){
        System.out.println("Pressione enter para continuar...");
        sc.nextLine();
        sc.nextLine();
    }

}
