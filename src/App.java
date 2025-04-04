import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final int OPCAO_SAIDA = 99;
    private static final int OPCAO_ADMIN = 0;

    public static void executar() {
        Scanner sc = new Scanner(System.in);
        Empresa empresa = inicializar();

        menu(empresa, sc);

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
                        new Funcionario("Leonardo Lopes", "Gerente de TI"),
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

    private static void menu(Empresa empresa, Scanner sc) {

        while(true) {
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

            if (escolha == OPCAO_SAIDA) {
                System.out.println("Encerrando o sistema...");
                break;
            } else if (escolha == OPCAO_ADMIN) {
                System.out.print("\nSenha de administrador: ");
                String form = sc.nextLine();
                if (Administrador.verificarSenha(form)){
                    limparTerminal();
                    menuAdministrador(empresa, sc);
                } else {
                    limparTerminal();
                    break;
                }
            } else {
                Funcionario func = buscarFuncionario(empresa.getTodosFuncionarios(), escolha);
                if (func != null) {
                    limparTerminal();
                    menuFuncionario(func, sc);
                } else {
                    System.out.println("❌ Funcionário não encontrado.");
                    pausa(sc);
                }
            }
        }

    }

    private static void menuFuncionario(Funcionario func, Scanner sc) {

        boolean rodando = true;

        while (rodando) {

            System.out.println("=== " + func.getName() + " ===");

            System.out.println("\nBarra de Funcionalidades: " +
                    "\n(1)Realizar novo pedido" +
                    "\n(2)Minhas informações" +
                    "\n(3)Retornar ao menu principal");

            System.out.print("\nEscolha: ");

            int escolha = sc.nextInt();
            switch (escolha) {
                case 1:
                    limparTerminal();
                    realizarPedido(func, sc);
                    break;
                case 2:
                    limparTerminal();
                    mostrarInformacoes(func, sc);
                    break;
                case 3:
                    limparTerminal();
                    rodando = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    pausa(sc);
                    limparTerminal();
            }
        }
    }

    private static void mostrarInformacoes(Funcionario func, Scanner sc) {
        System.out.println("SUAS INFORMAÇÕES\n");
        System.out.println("Id: " + func.getId());
        System.out.println("Nome: " + func.getName());
        System.out.println("Cargo: " + func.getCargo());
        System.out.println("Departamento: " + func.getDepartamento().getName());
        pausa(sc);
        limparTerminal();
    }

    public static void realizarPedido(Funcionario func, Scanner sc){

        System.out.println("Insira abaixo as informações do seu pedido");
        System.out.print("\nNúmero de itens do pedido: ");
        int numItens = sc.nextInt();
        sc.nextLine();

        List<Item> itens = new ArrayList<>();

        for (int i = 1; i <= numItens; i++) {
            System.out.println(i + "º item ");
            System.out.print("---------");
            System.out.print("\nNome do item: ");
            String nomeItem = sc.nextLine();

            System.out.print("\nPreço do item: ");
            double precoUnidade = sc.nextDouble();

            System.out.print("\nQuantidade desse item que está sendo pedida: ");
            int quantidade = sc.nextInt();
            sc.nextLine();

            double precoItem = quantidade * precoUnidade;
            itens.add(new Item(nomeItem, precoItem));
        }
        System.out.print("\nDescrição do pedido: ");
        String descricao = sc.nextLine();

        func.getDepartamento().empresa.adicionarPedido( new Pedido(func.getDepartamento(), func, itens, descricao));

        pausa(sc);
        limparTerminal();
    }

    private static void menuAdministrador(Empresa empresa, Scanner sc) {
        boolean rodando = true;

        while (rodando) {

            System.out.println("==== ADMINISTRADOR ====");

            System.out.println("\nBarra de Funcionalidades: " +
                    "\n(1)Listar pedidos" +
                    "\n(2)Listar pedidos por data" +
                    "\n(3)Concluir pedidos" +
                    "\n(4)Buscar pedidos por Funcionário" +
                    "\n(5)Buscar pedidos por Departamento" +
                    "\n(6)Valor total de pedidos" +
                    "\n(7)Pedidos dos últimos 30 dias" +
                    "\n(8)Pedido mais caro em aberto" +
                    "\n(9)Retornar ao menu");

            System.out.print("\nEscolha: ");

            int escolha = sc.nextInt();
            switch (escolha) {
                case 1:
                    limparTerminal();
                    Administrador.visualizarPedidos(empresa);
                    pausa(sc);
                    limparTerminal();
                    break;
//                case 2:
//                    Administrador.visualizarPorData(empresa);
//                    pausa(sc);
//                    limparTerminal();
//                    break;
//                    break;
                case 3:
                    limparTerminal();
                    System.out.print("Informe o id do pedido: ");
                    int id = sc.nextInt();
                    Administrador.concluirPedidos(id, empresa, sc);
                    limparTerminal();
                    break;
                case 4:
                    limparTerminal();
                    Administrador.buscarPorFuncionario(empresa, sc);
                    pausa(sc);
                    limparTerminal();
                case 9:
                    rodando = false;
                    limparTerminal();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    pausa(sc);
                    limparTerminal();
            }
        }
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

    private static void pausa(Scanner sc) {
        System.out.println("Pressione enter para continuar...");
        sc.nextLine();
        sc.nextLine();
    }

}
