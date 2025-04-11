import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Administrador {

    public static Empresa empresa;
    public static Departamento depto;
    private static final int passwd = 0x000C589D;

    public static void visualizarPedidos(Empresa empresa) {
        List<Pedido> pedidos = empresa.getTodosPedidos();

        if (pedidos.isEmpty()){
            System.out.println("Não há pedidos cadastrados ainda");
        } else {
            for (Pedido pedido : empresa.getTodosPedidos()) {
                if (pedido.getStatus() == Pedido.Status.EM_ANALISE) {
                    System.out.println(pedido);
                    System.out.println("-".repeat(10));
                }
            }
        }
    }

    public static void visualizarPorData(Empresa empresa, Scanner sc) {
        List<Pedido> pedidos = empresa.getTodosPedidos();

        System.out.println("Informe o intervalo de datas no format dd/mm/aaaa\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate data1 = null;

        while (data1 == null) {
            try {
                System.out.print("\nPrimeira data: ");
                String input1 = sc.nextLine().trim();
                sc.nextLine();

                if (input1.isEmpty()) {
                    throw new DateTimeParseException("Data vazia", input1, 0);
                }
                data1 = LocalDate.parse(input1, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Formato correto: dd/mm/aaaa");
            }
        }

        LocalDate data2 = null;
        while (data2 == null) {
            try {
                System.out.print("\nSegunda data: ");

                String input2 = sc.nextLine().trim();
                sc.nextLine();
                if (input2.isEmpty()) {
                    throw new DateTimeParseException("Data vazia", input2, 0);
                }
                data2 = LocalDate.parse(input2, formatter);

                if (data2.isBefore(data1)) {
                    System.out.println("A segunda data deve ser posterior ou igual à primeira!");
                    data2 = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Formato correto: dd/mm/aaaa");
            }
        }

        if (pedidos.isEmpty()){
            System.out.println("Não há pedidos cadastrados ainda");
        } else {
            for (Pedido pedido : pedidos) {
                boolean maiorOuIgual = pedido.getData().isAfter(data1) || pedido.getData().isEqual(data1);
                boolean menorOuIgual = pedido.getData().isBefore(data2) || pedido.getData().isEqual(data2);
                if (maiorOuIgual && menorOuIgual) {
                    System.out.println(pedido);
                    System.out.println("-".repeat(10) + "\n");
                }
            }
        }
    }

    public static void concluirPedidos(int id, Empresa empresa, Scanner sc) {
        List<Pedido> pedidos = empresa.getTodosPedidos();

        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id && pedido.getStatus() == Pedido.Status.EM_ANALISE) {
                System.out.print("Concluir o pedido: +" +
                        "\nDigite 1 para APROVAR o pedido" +
                        "\nDigite 2 para REJEITAR o pedido" +
                        "\nAção: ");
                int escolha = sc.nextInt();

                if (escolha == 1) {
                    System.out.print("\nConfirme a APROVAÇÃO do pedido digitando 1: ");
                    int confirmacao = sc.nextInt();
                    if (confirmacao == 1) {
                        pedido.aprovarPedido();
                        break;
                    } else {
                        System.out.println("Ações não convergem!");
                        break;
                    }
                } else if (escolha == 2) {
                    System.out.print("\nConfirme a REJEIÇÃO do pedido: ");
                    int confirmacao = sc.nextInt();
                    if (confirmacao == 2) {
                        pedido.rejeitarPedido();
                        break;
                    } else {
                        System.out.println("Ações não convergem!");
                        break;
                    }
                }
            }
        }
    }

    public static void buscarPorFuncionario(Empresa empresa, Scanner sc) {
        List<Pedido> pedidos = empresa.getTodosPedidos();
        sc.nextLine();

        System.out.print("\nNome do funcionário: ");
        String nome = sc.nextLine().toLowerCase();

        boolean encontrou = false;

        for (Pedido pedido : pedidos) {
            if(pedido.getFunc() != null && pedido.getFunc().getName() != null) {
                String nomeFuncionario = pedido.getFunc().getName().trim().toLowerCase();
                if(nomeFuncionario.contains(nome)) {
                    System.out.println(pedido);
                    System.out.println("-".repeat(10));
                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("Não há pedidos encontrados para " + nome);
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

//          TESTE DO MÉTODO ABAIXO
//        public static void main(String[] args) {
//        LocalDate data1 = LocalDate.now();
//        LocalDate data2 = LocalDate.of(2025, Month.APRIL, 3);
//        System.out.println(calcularDias(data1, data2));
    }
}
