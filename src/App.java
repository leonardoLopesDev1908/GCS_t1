import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class App {

    static DataSource ds = null;
    private static final int OPCAO_SAIDA = 99;
    private static final int OPCAO_ADMIN = 0;

    public static void executar() {
        ds = inicializarBanco();
        Scanner sc = new Scanner(System.in);
        Empresa empresa = inicializarEmpresa();

        menu(empresa, sc);
    }

    private static DataSource inicializarBanco(){
        String jdbcUrl = "jdbc:postgresql://localhost:5432/gcs";
        String userName = "leona";

        var ds = new PGSimpleDataSource(); 
        
        ds.setPassword(System.getenv("DB_PASSWORD"));
        ds.setURL(jdbcUrl);
        ds.setUser(userName);

        return ds;
    }

    private static Empresa inicializarEmpresa() {

        Empresa empresa = new Empresa("Tech Soluções");

        Departamento administrativo = new Departamento("Administrativo", empresa);
        Administrador.depto = administrativo;
        Administrador.empresa = empresa;

        criarDepartamento("Recursos Humanos", empresa,
                List.of(
                        new Funcionario("Julio Gomes", "Analista de contratações"),
                        new Funcionario("Marcia Lopes", "Gerente"),
                        new Funcionario("Paulo César", "Estagiário")
                ));

        criarDepartamento("Informática", empresa,
                List.of(
                        new Funcionario("Mauro Yamaguchi", "Técnico em informática"),
                        new Funcionario("Leonardo Lopes", "Gerente de TI"),
                        new Funcionario("Marcos Silva", "Estagiario"),
                        new Funcionario("Julia Espindonla", "Engenheira de Software")
                ));

        criarDepartamento("Financeiro", empresa, List.of(
                        new Funcionario("Patricia Camargo", "Analista Contábil"),
                        new Funcionario("Paulo Omar", "Analista Contábil"),
                        new Funcionario("Lucas Guedes", "Gerente do Financeiro"),
                        new Funcionario("Vanessa da Mata", "Auditora de Vendas")
                ));

        criarDepartamento("Comercial", empresa, List.of(
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

            try {
                System.out.print("""
                                 Faça login com seu Id 
                                 (ou use '99' para encerrar o programa): """);
                int escolha = sc.nextInt();
                sc.nextLine();

                if (escolha == OPCAO_SAIDA) {
                    System.out.println("Encerrando o sistema...");
                    break;
                } else if (escolha == OPCAO_ADMIN) {
                    System.out.print("\nSenha de administrador: ");
                    String form = sc.nextLine();
                    if (Administrador.verificarSenha(form)) {
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
            } catch (InputMismatchException e){
                System.out.println("ERRO - Digite apenas números: " + e.getMessage());
                sc.nextLine();
                pausa(sc);
            }
        }

    }

    private static void menuFuncionario(Funcionario func, Scanner sc) {

        boolean rodando = true;

        while (rodando) {

            System.out.println("=== " + func.getName() + " ===");

            System.out.println("""
                               Barra de Funcionalidades: 
                               (1)Realizar novo pedido
                               (2)Minhas informa\u00e7\u00f5es
                               (3)Retornar ao menu principal""");

            try {
                System.out.print("\nEscolha: ");

                int escolha = sc.nextInt();
                switch (escolha) {
                    case 1 -> {
                        limparTerminal();
                        realizarPedido(func, sc);
                    }
                    case 2 -> {
                        limparTerminal();
                        mostrarInformacoes(func, sc);
                    }
                    case 3 -> {
                        limparTerminal();
                        rodando = false;
                    }
                    default -> {
                        System.out.println("Opção inválida. Tente novamente.");
                        pausa(sc);
                        limparTerminal();
                    }
                }
            } catch (InputMismatchException e){
                System.out.println("ERRO - Digite apenas número: " + e.getMessage());
                sc.nextLine();
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

        if (numItens <= 0) {
            System.out.println("Número de itens inválido");
            pausa(sc);
            limparTerminal();
        }

        List<Item> itens = new ArrayList<>();

        try {
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

            Pedido pedido = new Pedido(func.getDepartamento(), func, itens, descricao);
            func.getDepartamento().empresa.adicionarPedido(pedido);

            String sql = "INSERT INTO pedidos (status, id, data, valor, itens, departamento, funcionario) "+
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = ConnectionFactory.getConnection()){
                PreparedStatement st = con.prepareStatement(sql);
                
                st.setString(1, String.valueOf(pedido.getStatus()));
                st.setInt(2, pedido.getId());
                st.setDate(3, java.sql.Date.valueOf(pedido.getData()));
                st.setDouble(4, pedido.getValor());
                st.setString(5, pedido.getItens());
                st.setString(6, String.valueOf(pedido.getDepartamento()));
                st.setString(7, String.valueOf(pedido.getFunc()));
                st.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            pausa(sc);
            limparTerminal();
        } catch (InputMismatchException e) {
            System.out.println("ERRO - Entrada inválida");
            sc.nextLine();
            pausa(sc);
            limparTerminal();
        }
    }

    public static void realizarPedido(Departamento depto, Scanner sc){

        System.out.println("Insira abaixo as informações do seu pedido");
        System.out.print("\nNúmero de itens do pedido: ");
        int numItens = sc.nextInt();
        sc.nextLine();

        List<Item> itens = new ArrayList<>();

        try {
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

            depto.empresa.adicionarPedido(new Pedido(depto, itens, descricao));

            pausa(sc);
            limparTerminal();
        }catch (InputMismatchException e){
            System.out.println("ERRO - Entrada inválida");
            sc.nextLine();
            pausa(sc);
            limparTerminal();
        }
    }

    private static void menuAdministrador(Empresa empresa, Scanner sc) {
        boolean rodando = true;

        while (rodando) {

            System.out.println("==== ADMINISTRADOR ====");

            System.out.println("""
                                Barra de Funcionalidades: 
                               (1)Listar pedidos
                               (2)Listar pedidos por data
                               (3)Concluir pedidos
                               (4)Buscar pedidos por Funcion\u00e1rio
                               (5)Buscar pedidos por descri\u00e7\u00e3o
                               (6)Valor total de pedidos
                               (7)Pedidos dos \u00faltimos 30 dias
                               (8)Pedido mais caro em aberto
                               (9)Fazer pedido
                               (0)Retornar ao menu""");

            System.out.print("\nEscolha: ");

            try {
                int escolha = sc.nextInt();
                switch (escolha) {
                    case 1 -> {
                        limparTerminal();
                        Administrador.visualizarPedidos(empresa);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 2 -> {
                        Administrador.visualizarPorData(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 3 -> {
                        limparTerminal();
                        System.out.print("Informe o id do pedido: ");
                        int id = sc.nextInt();
                        Administrador.concluirPedidos(id, empresa, sc);
                        limparTerminal();
                    }
                    case 4 -> {
                        Administrador.buscarPorFuncionario(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 5 -> {
                        Administrador.buscarPorDescricao(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 6 -> {
                        Administrador.valorTotalPedidos(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 7 -> {
                        Administrador.pedidosRecentes(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 8 -> {
                        Administrador.pedidoMaisCaro(empresa, sc);
                        pausa(sc);
                        limparTerminal();
                    }
                    case 9 -> {
                        limparTerminal();
                        realizarPedido(Administrador.depto, sc);
                    }
                    case 0 -> {
                        rodando = false;
                        limparTerminal();
                    }
                    default -> {
                        System.out.println("Opção inválida. Tente novamente.");
                        pausa(sc);
                        limparTerminal();
                    }
                }
            } catch (InputMismatchException e){
                System.out.println("Escolha inválida");
                sc.nextLine();
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
