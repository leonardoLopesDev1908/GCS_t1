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

        boolean rodando = true;

        while(rodando) {
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

            System.out.print("\nFaça login com seu Id "+
                        "\n(ou use '99' para encerrar o programa): ");
            int escolha = sc.nextInt();
            sc.nextLine();

            if (escolha == 99){
                System.out.println("Encerrando o sistema...");
                break;
            } else if (buscarFuncionario(empresa.getTodosFuncionarios(), escolha) != null) {
                limparTerminal();
                menuFuncionario(buscarFuncionario(empresa.getTodosFuncionarios(), escolha));
                pausa();

                //LOGAR COMO ADMINISTRADOR
//            } else if (escolha == 0) {
//                System.out.print("Ainda não desenvolvido...");
//                pausa();
//                //String senha = sc.nextLine();

            } else {
                System.out.println("Opção inválida. Tente novamente");
                pausa();
            }
        }

    }

    private static void menuFuncionario(Funcionario func) {

        boolean rodando = true;

        while (rodando) {

            System.out.println("=== " + func.getName() + " ===");

            System.out.println("\nBarra de Funcionalidades: " +
                    "\n(1)Realizar novo pedido" +
                    "\n(2)Minhas informações" +
                    "\n(3)Retornar ao menu de funcionários");

            System.out.print("\nEscolha: ");

            int escolha = sc.nextInt();
            switch (escolha) {
                case 1:
                    limparTerminal();
                    realizarPedido(func);
                    break;
                case 2:
                    limparTerminal();
                    mostrarInformacoes(func);
                    break;
                case 3:
                    limparTerminal();
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    pausa();
                    limparTerminal();
            }
        }
    }

    private static void mostrarInformacoes(Funcionario func) {
        System.out.println("SUAS INFORMAÇÕES\n");
        System.out.println("Id: " + func.getId());
        System.out.println("Nome: " + func.getName());
        System.out.println("Cargo: " + func.getCargo());
        System.out.println("Departamento: " + func.getDepartamento().getName());
        pausa();
        limparTerminal();
    }

    public static void realizarPedido(Funcionario func){
        System.out.println("Funcionalidade ainda não feita");
        pausa();
        limparTerminal();
    }


//MÉTODOS DE APOIO PARA O FUNCIONAMENTO
    private static Funcionario buscarFuncionario(List<Funcionario> list, int id){
        for (Funcionario func : list) {
            if (func.getId() == id) {
                return func;
            }
        }
        return null;
    }

    private static void limparTerminal() {
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }

    private static void pausa() {
        System.out.println("Pressione enter para continuar...");
        sc.nextLine();
        sc.nextLine();
    }

}
