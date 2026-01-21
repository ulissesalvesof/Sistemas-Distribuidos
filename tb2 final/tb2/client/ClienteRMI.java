package tb2.client;

import tb2.protocol.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Cliente RMI Manual
 * Usa o método doOperation() para enviar requisições ao servidor
 */
public class ClienteRMI {
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final String OBJECT_NAME = "ServicoTelefonia";
    
    private static final Gson gson = new Gson();
    private static RemoteObjectRef objetoRemoto;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        
        try {
            // Criar referência ao objeto remoto
            objetoRemoto = new RemoteObjectRef(
                InetAddress.getByName(SERVER_HOST),
                SERVER_PORT,
                OBJECT_NAME
            );
            
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║      CLIENTE RMI MANUAL - SISTEMA DE TELEFONIA         ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            System.out.println("🔌 Conectado a: " + objetoRemoto);
            System.out.println("📦 Usando protocolo doOperation/getRequest/sendReply\n");
            
            executarMenu();
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private static void executarMenu() {
        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();
            
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
                    System.out.println("\n👋 Encerrando cliente...");
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private static void exibirMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         MENU PRINCIPAL              ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║  1 - Adicionar Cliente              ║");
        System.out.println("║  2 - Remover Cliente                ║");
        System.out.println("║  3 - Consultar Cliente              ║");
        System.out.println("║  4 - Listar Clientes                ║");
        System.out.println("║  5 - Adicionar Linha                ║");
        System.out.println("║  6 - Remover Linha                  ║");
        System.out.println("║  7 - Registrar Chamada              ║");
        System.out.println("║  8 - Gerar Fatura                   ║");
        System.out.println("║  9 - Listar Faturas                 ║");
        System.out.println("║ 10 - Ver Estatísticas               ║");
        System.out.println("║  0 - Sair                           ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("Opção: ");
    }
    
    private static int lerOpcao() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        } finally {
            scanner.nextLine();
        }
    }
    
    /**
     * Invoca método remoto usando doOperation() com PASSAGEM POR VALOR
     */
    private static String invocarMetodoRemoto(String methodId, Map<String, Object> args) {
        try {
            // Serializar argumentos para JSON (REPRESENTAÇÃO EXTERNA)
            byte[] argumentosJSON = gson.toJson(args).getBytes();
            
            // Chamar doOperation() - MÉTODO DO PROTOCOLO RMI MANUAL
            byte[] resultadoJSON = RequestHandler.doOperation(objetoRemoto, methodId, argumentosJSON);
            
            // Desserializar resultado
            String resultado = new String(resultadoJSON);
            resultado = gson.fromJson(resultado, String.class);
            
            return resultado;
            
        } catch (IOException e) {
            return "❌ Erro de comunicação: " + e.getMessage();
        }
    }
    
    private static void adicionarCliente() {
        System.out.println("\n=== ADICIONAR CLIENTE ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("nome", nome);
        args.put("cpf", cpf);
        args.put("telefone", telefone);
        args.put("email", email);
        
        String resultado = invocarMetodoRemoto("adicionarCliente", args);
        System.out.println("\n📌 " + resultado);
    }
    
    private static void removerCliente() {
        System.out.println("\n=== REMOVER CLIENTE ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("cpf", cpf);
        
        String resultado = invocarMetodoRemoto("removerCliente", args);
        System.out.println("\n📌 " + resultado);
    }
    
    private static void consultarCliente() {
        System.out.println("\n=== CONSULTAR CLIENTE ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("cpf", cpf);
        
        String resultado = invocarMetodoRemoto("consultarCliente", args);
        System.out.println("\n" + resultado);
    }
    
    private static void listarClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        
        try {
            byte[] argumentosJSON = "{}".getBytes();
            byte[] resultadoJSON = RequestHandler.doOperation(objetoRemoto, "listarClientes", argumentosJSON);
            
            String json = new String(resultadoJSON);
            json = gson.fromJson(json, String.class);
            
            @SuppressWarnings("unchecked")
            List<String> clientes = gson.fromJson(json, List.class);
            
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado");
            } else {
                for (int i = 0; i < clientes.size(); i++) {
                    System.out.println((i + 1) + ". " + clientes.get(i));
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
    
    private static void adicionarLinha() {
        System.out.println("\n=== ADICIONAR LINHA ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("Número da linha: ");
        String numero = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("cpf", cpf);
        args.put("numero", numero);
        
        String resultado = invocarMetodoRemoto("adicionarLinha", args);
        System.out.println("\n📌 " + resultado);
    }
    
    private static void removerLinha() {
        System.out.println("\n=== REMOVER LINHA ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        System.out.print("Número da linha: ");
        String numero = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("cpf", cpf);
        args.put("numero", numero);
        
        String resultado = invocarMetodoRemoto("removerLinha", args);
        System.out.println("\n📌 " + resultado);
    }
    
    private static void registrarChamada() {
        System.out.println("\n=== REGISTRAR CHAMADA ===");
        System.out.print("Número de origem: ");
        String origem = scanner.nextLine();
        System.out.print("Número de destino: ");
        String destino = scanner.nextLine();
        System.out.print("Duração (minutos): ");
        int duracao = scanner.nextInt();
        scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("origem", origem);
        args.put("destino", destino);
        args.put("duracao", duracao);
        
        String resultado = invocarMetodoRemoto("registrarChamada", args);
        System.out.println("\n📌 " + resultado);
    }
    
    private static void gerarFatura() {
        System.out.println("\n=== GERAR FATURA ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        Map<String, Object> args = new HashMap<>();
        args.put("cpf", cpf);
        
        String resultado = invocarMetodoRemoto("gerarFatura", args);
        System.out.println("\n" + resultado);
    }
    
    private static void listarFaturas() {
        System.out.println("\n=== LISTAR FATURAS ===");
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("cpf", cpf);
            
            byte[] argumentosJSON = gson.toJson(args).getBytes();
            byte[] resultadoJSON = RequestHandler.doOperation(objetoRemoto, "listarFaturas", argumentosJSON);
            
            String json = new String(resultadoJSON);
            json = gson.fromJson(json, String.class);
            
            @SuppressWarnings("unchecked")
            List<String> faturas = gson.fromJson(json, List.class);
            
            System.out.println();
            for (String fatura : faturas) {
                System.out.println(fatura);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
    
    private static void obterEstatisticas() {
        System.out.println("\n=== ESTATÍSTICAS DO SISTEMA ===");
        
        Map<String, Object> args = new HashMap<>();
        String resultado = invocarMetodoRemoto("obterEstatisticas", args);
        System.out.println("\n" + resultado);
    }
}
