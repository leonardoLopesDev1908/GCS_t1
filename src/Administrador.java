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
                imprimirPedido(rs, i);
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
                imprimirPedido(rs, i);
                i++;
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
                novoStatus = "REJEITADO";
                statusAlterado = true;
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
                imprimirPedido(rs, i);
                i++;
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
        String sql = "SELECT SUM(valor) AS Total_pedidos FROM pedidos";

        try (Connection con = ConnectionFactory.getConnection()) {
            PreparedStatement st = con.prepareStatement(sql);   
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                double totalPedidos = rs.getDouble("Total_pedidos");
                
                System.out.printf("""
                    Total: $%.2f
                    """, totalPedidos);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void pedidosRecentes(Empresa empresa, Scanner sc){
        String sql = "SELECT * FROM pedidos WHERE data BETWEEN ? AND ?";

        try(Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            
            LocalDate data1 = LocalDate.now().minusDays(30);
            LocalDate data2 = LocalDate.now();

            st.setDate(1, java.sql.Date.valueOf(data1));
            st.setDate(2, java.sql.Date.valueOf(data2));
            ResultSet rs = st.executeQuery();

            int i = 1;
            while(rs.next()){
                imprimirPedido(rs, i);
                i++;
            }
            if(i == 1){
                System.out.println("Nenhum pedido nos últimos 30 dias");
            }

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

    public static void pedidoMaisCaro(Empresa empresa, Scanner sc){
        String sql = "SELECT * FROM pedidos ORDER BY valor DESC";
        boolean encontrou = false;

        try (Connection con = ConnectionFactory.getConnection()){
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
        
            int i = 1;
            while(rs.next()){
                imprimirPedido(rs, i);
                encontrou = true;
                i++;
            }

            if (!encontrou) {
                System.out.println("Nenhum pedido encontrado");
            }

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
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

            return rs.next();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void imprimirPedido(ResultSet rs, int i) throws SQLException{
        String status = rs.getString("status");
        int id = rs.getInt("id");
        Date data = rs.getDate("data");
        double valor = rs.getDouble("valor");
        String itens = rs.getString("itens");
        String departamento = rs.getString("departamento");
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
                    departamento, func);
        System.out.println("-=".repeat(15));
    }
    
}
