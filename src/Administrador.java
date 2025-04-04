import java.util.List;
import java.util.Scanner;

public class Administrador {

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

//    public static void visualizarPorData(Empresa empresa, LocalDate data1, LocalDate data2) {
//        List<Pedido> pedidos = empresa.getTodosPedidos();
//
//        if (pedidos.isEmpty()){
//            System.out.println("Não há pedidos cadastrados ainda");
//        } else {
//            pedidos.sort()
//            for (Pedido pedido : empresa.getTodosPedidos()) {
//                System.out.println(pedido);
//            }
//        }
//    }

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

    //METODOS PARA IMPLEMENTAR
    public static void buscarPorFuncionario(Empresa empresa, Scanner sc) {
        System.out.print("\nNome do Funcionário: ");
        String nome = sc.nextLine();

        List<Pedido> pedidos = empresa.getTodosPedidos();

        for (Pedido pedido : pedidos) {
            if (pedido.getFunc().getName().equalsIgnoreCase(nome)) {
                System.out.println(pedido);
                System.out.println("-".repeat(10));
            }
        }
    }

    public static void buscarPorDescricao() {

    }

    public static void valorTotalPedidos(){

    }

    public static void pedidosRecentes(){

    }

    public static void pedidoMaisCaro(){

    }

    public static boolean verificarSenha(String senha){
        int checkedSenha = Integer.parseInt(senha);

        String criptSenha = String.valueOf(passwd);
        int descriptSenha = Integer.parseInt(criptSenha);

        return checkedSenha == descriptSenha;
    }

}
