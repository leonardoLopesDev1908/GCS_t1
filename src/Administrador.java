import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Administrador {

    public static Empresa empresa;
    public static Departamento depto;
    private static final int passwd = 0x000C589D;

    public static void visualizarPedidos(Empresa empresa) {

        String sql = "SELECT * FROM pedidos WHERE status = 'EM_ANALISE'";
        try (Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            int i = 1;

            while(rs.next()){
                String status = rs.getString("status");
                int id = rs.getInt("id");
                Date data = rs.getDate("data");
                double valor = rs.getDouble("valor");
                String itens = rs.getString("itens");
                String depto = rs.getString("departamento");
                String func = rs.getString("funcionario");

                System.out.printf("""
                        %dº PEDIDO
                        Status : %s;
                        Id: %d;
                        Data: %s;
                        Valor: $%.2f;
                        Itens: %s;
                        Departamento: %s;
                        Funcionário: %s.
                        """, i, status, id, data, valor, itens,
                            depto, func);
                System.out.println("-=".repeat(15));
                i++;
            }


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void visualizarPorData(Empresa empresa, Scanner sc){
        String sql = "SELECT * FROM pedidos WHERE data BETWEEN ? AND ?";
        System.out.println("Informe a data no formato aaaa-mm-dd");
        
        LocalDate data1 = null;
        while (data1 == null) {
            try {
                System.out.print("\nPrimeira data: ");
                String input1 = sc.nextLine().trim();
                sc.nextLine();

                if (input1.isEmpty()) {
                    throw new DateTimeParseException("Data vazia", input1, 0);
                }
                data1 = LocalDate.parse(input1);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Formato correto: aaaa-mm-dd");
            }
        }
        java.sql.Date date1 = java.sql.Date.valueOf(data1);


        LocalDate data2 = null;
        while (data2 == null) {
            try {
                System.out.print("\nSegunda data: ");
                String input2 = sc.nextLine().trim();
                sc.nextLine();

                if (input2.isEmpty()) {
                    throw new DateTimeParseException("Data vazia", input2, 0);
                }
                data2 = LocalDate.parse(input2);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Formato correto: aaaa-mm-dd");
            }
        }
        java.sql.Date date2 = java.sql.Date.valueOf(data2);


        try (Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            st.setDate(1, date1);
            st.setDate(2, date2);
            
            ResultSet rs = st.executeQuery();
            int i = 1;
            while (rs.next()){
                String status = rs.getString("status");
                int id = rs.getInt("id");
                Date data = rs.getDate("data");
                double valor = rs.getDouble("valor");
                String itens = rs.getString("itens");
                String depto = rs.getString("departamento");
                String func = rs.getString("funcionario");

                System.out.printf("""
                        %dº PEDIDO
                        Status : %s;
                        Id: %d;
                        Data: %s;
                        Valor: $%.2f;
                        Itens: %s;
                        Departamento: %s;
                        Funcionário: %s.
                        """, i, status, id, data, valor, itens,
                            depto, func);
                i++;
                System.out.println("-=".repeat(15));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    
    public static void concluirPedidos(int id, Empresa empresa, Scanner sc) {
        if(!buscarPedido(id)){
            System.out.println("Não há pedidos com esse id!");
            return;
        } 
        String novoStatus = null;
        boolean statusAlterado = false;

        System.out.print("""
                        Concluir o pedido: +
                        Digite 1 para APROVAR o pedido
                        Digite 2 para REJEITAR o pedido
                        Ação: """);
        int escolha = sc.nextInt();

        if (escolha == 1) {
            System.out.print("\nConfirme a APROVAÇÃO do pedido digitando 1: ");
            int confirmacao = sc.nextInt();
            if (confirmacao == 1) {
                novoStatus = "APROVADO";
                statusAlterado = true;
            } else {
                System.out.println("Ações não convergem!");
            }
        } else if (escolha == 2) {
            System.out.print("\nConfirme a REJEIÇÃO do pedido: ");
            int confirmacao = sc.nextInt();
            if (confirmacao == 2) {
            } else {
                System.out.println("Ações não convergem!");
            }
        }

        if (statusAlterado && novoStatus != null) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";

            try (Connection con = ConnectionFactory.getConnection()) {  
                PreparedStatement st = con.prepareStatement(sql);

                st.setString(1, novoStatus);
                st.setInt(2, id);
                st.executeUpdate();

            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void buscarPorFuncionario(Empresa empresa, Scanner sc) {

        System.out.print("\nNome do funcionário: ");
        sc.nextLine();
        String nome = sc.nextLine();
        String sql = "SELECT * FROM pedidos WHERE funcionario LIKE ?";
        boolean encontrou = false;

        try (Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, "%"+ nome + "%");
            
            ResultSet rs = st.executeQuery();

            int i = 1;

 
            while (rs.next()){
                String status = rs.getString("status");
                int id = rs.getInt("id");
                Date data = rs.getDate("data");
                double valor = rs.getDouble("valor");
                String itens = rs.getString("itens");
                String depto = rs.getString("departamento");
                String func = rs.getString("funcionario");

                System.out.printf("""
                        %dº PEDIDO
                        Status : %s;
                        Id: %d;
                        Data: %s;
                        Valor: $%.2f;
                        Itens: %s;
                        Departamento: %s;
                        Funcionário: %s.
                        """, i, status, id, data, valor, itens,
                            depto, func);
                i++;
                    System.out.println("-=".repeat(15));
            }
            encontrou = true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }   
        if (!encontrou){
            System.out.println("Não há pedidos encontrados para "+ nome);
        }    
    }
    
    public static void buscarPorDescricao(Empresa empresa, Scanner sc) {

        sc.nextLine();
        System.out.print("\nBusca: ");
        try {
            String busca = sc.nextLine().toLowerCase().trim();

            List<Pedido> pedidos = empresa.getTodosPedidos();

            boolean encontrou = false;
            for (Pedido pedido : pedidos) {
                if (pedido.getDescricao().toLowerCase().contains(busca)) {
                    System.out.println(pedido);
                    System.out.println("-".repeat(10));
                    encontrou = true;
                }
            }
            if (!encontrou) {
                System.out.println("Nenhum pedido encontrado com a busca '" + busca + "'");
            }
        } catch (InputMismatchException e) {
            System.out.println("Busca inválida");
            sc.nextLine();
        }
    }

    public static void valorTotalPedidos(Empresa empresa, Scanner sc){
        List<Pedido> pedidos = empresa.getTodosPedidos();

        double valorTotal = 0;
        for(Pedido pedido : pedidos) {
            valorTotal += pedido.getValor();
        }

        System.out.println("Valor total em pedidos: R$" + valorTotal);

    }

    public static void pedidosRecentes(Empresa empresa, Scanner sc){
        List<Pedido> pedidos = empresa.getTodosPedidos();
        LocalDate hoje = LocalDate.now();

        for (Pedido pedido : pedidos){
            if (calcularDias(hoje, pedido.getData()) <= 30){
                System.out.println(pedido);
            }
        }

    }

    public static void pedidoMaisCaro(Empresa empresa, Scanner sc){
        List<Pedido> pedidos = empresa.getTodosPedidos();
        Pedido pedidoMaisCaro = new Pedido(0);

        boolean encontrou = false;
        for(Pedido pedido : pedidos) {
            if (pedido.getValor() > pedidoMaisCaro.getValor() && pedido.getStatus().equals(Pedido.Status.EM_ANALISE)){
                pedidoMaisCaro = pedido;
                encontrou = true;
            }
        }

        System.out.println("Pedido mais caro em análise: \n");
        System.out.println(pedidoMaisCaro);

        if (!encontrou) {
            System.out.println("Nenhum pedido encontrado");
        }
    }

    //AUTENTICAÇÃO
    public static boolean verificarSenha(String senha){
        int checkedSenha = Integer.parseInt(senha);

        String criptSenha = String.valueOf(passwd);
        int descriptSenha = Integer.parseInt(criptSenha);

        return checkedSenha == descriptSenha;
    }

    private static int calcularDias(LocalDate data1, LocalDate data2){
        return (int) (data1.toEpochDay() - data2.toEpochDay());
    }
 
    private static boolean buscarPedido(int id) {
        String sql = "SELECT id FROM pedidos WHERE id = ?";

        try (Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            
            ResultSet rs = st.executeQuery();

            if (rs.next()){
                int retrieve = rs.getInt("id");
                return retrieve == id;
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
    
}
