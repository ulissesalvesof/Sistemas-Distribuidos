package tb2.client;

import tb2.common.RemoteInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

/**
 * Cliente RMI para o sistema de gerenciamento de telefonia
 */
public class Client {
    
    private static final int RMI_PORT = 1099;
    private static final String SERVICE_NAME = "ServicoTelefonia";
    private static final String SERVER_HOST = "localhost";
    
    private RemoteInterface servidor;
    private Scanner scanner;
    
    public Client() {
        this.scanner = new Scanner(System.in);
    }
    
    public void conectar() {
        try {
            System.out.println("========================================");
            System.out.println("   CLIENTE RMI - SISTEMA DE TELEFONIA   ");
            System.out.println("========================================\n");
            
            System.out.println("[CLIENTE] Conectando ao servidor RMI...");
            System.out.println("  Host: " + SERVER_HOST);
            System.out.println("  Porta: " + RMI_PORT);
            System.out.println("  Serviço: " + SERVICE_NAME + "\n");
            
            // Localiza o registro RMI
            Registry registry = LocateRegistry.getRegistry(SERVER_HOST, RMI_PORT);
            
            // Obtém a referência para o serviço remoto
            servidor = (RemoteInterface) registry.lookup(SERVICE_NAME);
            
            System.out.println("[CLIENTE] Conectado com sucesso ao servidor!\n");
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro ao conectar ao servidor:");
            System.err.println("  Mensagem: " + e.getMessage());
            System.err.println("\nVerifique se o servidor está rodando!");
            System.exit(1);
        }
    }
    
    public void executarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            try {
                exibirMenu();
                int opcao = lerOpcao();
                
                switch (opcao) {
                    case 1:
                        adicionarCliente();
                        break;
                    case 2:
                        removerCliente();
                        break;
                    case 3:
                        consultarCliente();
                        break;
                    case 4:
                        listarClientes();
                        break;
                    case 5:
                        adicionarLinha();
                        break;
                    case 6:
                        removerLinha();
                        break;
                    case 7:
                        registrarChamada();
                        break;
                    case 8:
                        gerarFatura();
                        break;
                    case 9:
                        listarFaturas();
                        break;
                    case 10:
                        obterEstatisticas();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\n[CLIENTE] Encerrando conexão com o servidor...");
                        break;
                    default:
                        System.out.println("\n[CLIENTE] Opção inválida! Tente novamente.\n");
                }
                
                if (continuar && opcao != 0) {
                    pausar();
                }
                
            } catch (Exception e) {
                System.err.println("\n[CLIENTE] Erro ao executar operação:");
                System.err.println("  " + e.getMessage());
                pausar();
            }
        }
        
        System.out.println("[CLIENTE] Cliente encerrado com sucesso!");
        scanner.close();
    }
    
    private void exibirMenu() {
        System.out.println("\n========================================");
        System.out.println("             MENU PRINCIPAL              ");
        System.out.println("========================================");
        System.out.println(" 1 - Adicionar Cliente");
        System.out.println(" 2 - Remover Cliente");
        System.out.println(" 3 - Consultar Cliente");
        System.out.println(" 4 - Listar Todos os Clientes");
        System.out.println(" 5 - Adicionar Linha Telefônica");
        System.out.println(" 6 - Remover Linha Telefônica");
        System.out.println(" 7 - Registrar Chamada");
        System.out.println(" 8 - Gerar Fatura");
        System.out.println(" 9 - Listar Faturas de Cliente");
        System.out.println("10 - Ver Estatísticas do Sistema");
        System.out.println(" 0 - Sair");
        System.out.println("========================================");
        System.out.print("Escolha uma opção: ");
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void adicionarCliente() {
        try {
            System.out.println("\n=== ADICIONAR CLIENTE ===");
            System.out.print("Nome: ");
            String nome = scanner.nextLine().trim();
            
            System.out.print("CPF (apenas números): ");
            String cpf = scanner.nextLine().trim();
            
            if (nome.isEmpty() || cpf.isEmpty()) {
                System.out.println("[CLIENTE] Nome e CPF não podem ser vazios!");
                return;
            }
            
            String resultado = servidor.adicionarCliente(nome, cpf);
            System.out.println("\n" + resultado);
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void removerCliente() {
        try {
            System.out.println("\n=== REMOVER CLIENTE ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            System.out.print("Confirma a remoção? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacao.equals("S")) {
                boolean resultado = servidor.removerCliente(cpf);
                if (resultado) {
                    System.out.println("\nSUCESSO: Cliente removido com sucesso!");
                } else {
                    System.out.println("\nERRO: Cliente não encontrado!");
                }
            } else {
                System.out.println("\nOperação cancelada.");
            }
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void consultarCliente() {
        try {
            System.out.println("\n=== CONSULTAR CLIENTE ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            String resultado = servidor.consultarCliente(cpf);
            System.out.println("\n" + resultado);
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void listarClientes() {
        try {
            System.out.println();
            List<String> resultado = servidor.listarClientes();
            for (String linha : resultado) {
                System.out.println(linha);
            }
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void adicionarLinha() {
        try {
            System.out.println("\n=== ADICIONAR LINHA TELEFÔNICA ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            System.out.print("Número de telefone: ");
            String numero = scanner.nextLine().trim();
            
            String resultado = servidor.adicionarLinha(cpf, numero);
            System.out.println("\n" + resultado);
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void removerLinha() {
        try {
            System.out.println("\n=== REMOVER LINHA TELEFÔNICA ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            System.out.print("Número de telefone: ");
            String numero = scanner.nextLine().trim();
            
            System.out.print("Confirma a remoção? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacao.equals("S")) {
                boolean resultado = servidor.removerLinha(cpf, numero);
                if (resultado) {
                    System.out.println("\nSUCESSO: Linha removida com sucesso!");
                } else {
                    System.out.println("\nERRO: Linha não encontrada ou não pertence ao cliente!");
                }
            } else {
                System.out.println("\nOperação cancelada.");
            }
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void registrarChamada() {
        try {
            System.out.println("\n=== REGISTRAR CHAMADA ===");
            System.out.print("Número de origem: ");
            String origem = scanner.nextLine().trim();
            
            System.out.print("Número de destino: ");
            String destino = scanner.nextLine().trim();
            
            System.out.print("Duração (minutos): ");
            int duracao = Integer.parseInt(scanner.nextLine().trim());
            
            boolean resultado = servidor.registrarChamada(origem, destino, duracao);
            if (resultado) {
                System.out.println("\nSUCESSO: Chamada registrada com sucesso!");
            } else {
                System.out.println("\nERRO: Não foi possível registrar a chamada!");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("[CLIENTE] Duração inválida!");
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void gerarFatura() {
        try {
            System.out.println("\n=== GERAR FATURA ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            String resultado = servidor.gerarFatura(cpf);
            System.out.println("\n" + resultado);
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void listarFaturas() {
        try {
            System.out.println("\n=== LISTAR FATURAS ===");
            System.out.print("CPF do cliente: ");
            String cpf = scanner.nextLine().trim();
            
            System.out.println();
            List<String> resultado = servidor.listarFaturas(cpf);
            for (String linha : resultado) {
                System.out.println(linha);
            }
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void obterEstatisticas() {
        try {
            System.out.println();
            String resultado = servidor.obterEstatisticas();
            System.out.println(resultado);
            
        } catch (Exception e) {
            System.err.println("[CLIENTE] Erro: " + e.getMessage());
        }
    }
    
    private void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    public static void main(String[] args) {
        Client cliente = new Client();
        cliente.conectar();
        cliente.executarMenu();
    }
}
