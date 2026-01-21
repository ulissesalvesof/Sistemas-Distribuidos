package tb2.server;

import tb2.common.RemoteInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe principal do servidor RMI
 */
public class Server {
    
    private static final int RMI_PORT = 1099;
    private static final String SERVICE_NAME = "ServicoTelefonia";
    
    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("   SERVIDOR RMI - SISTEMA DE TELEFONIA  ");
            System.out.println("========================================\n");
            
            // Cria a implementação do serviço
            System.out.println("[SERVIDOR] Criando implementação do serviço...");
            ServerImpl servidor = new ServerImpl();
            
            // Cria o registro RMI na porta especificada
            System.out.println("[SERVIDOR] Iniciando registro RMI na porta " + RMI_PORT + "...");
            Registry registry;
            
            try {
                // Tenta criar um novo registro
                registry = LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("[SERVIDOR] Registro RMI criado com sucesso!");
            } catch (Exception e) {
                // Se já existe um registro, usa o existente
                registry = LocateRegistry.getRegistry(RMI_PORT);
                System.out.println("[SERVIDOR] Usando registro RMI existente.");
            }
            
            // Registra o serviço no registro RMI
            System.out.println("[SERVIDOR] Registrando serviço '" + SERVICE_NAME + "'...");
            registry.rebind(SERVICE_NAME, servidor);
            
            System.out.println("\n========================================");
            System.out.println("   SERVIDOR PRONTO E AGUARDANDO CLIENTES");
            System.out.println("========================================");
            System.out.println("Porta: " + RMI_PORT);
            System.out.println("Serviço: " + SERVICE_NAME);
            System.out.println("========================================\n");
            
            // Adiciona shutdown hook para fechar graciosamente
            final Registry finalRegistry = registry;
            final String finalServiceName = SERVICE_NAME;
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[SERVIDOR] Encerrando servidor...");
                try {
                    finalRegistry.unbind(finalServiceName);
                    System.out.println("[SERVIDOR] Serviço desregistrado com sucesso!");
                } catch (Exception e) {
                    System.err.println("[SERVIDOR] Erro ao desregistrar serviço: " + e.getMessage());
                }
            }));
            
            // Mantém o servidor rodando
            System.out.println("[SERVIDOR] Pressione Ctrl+C para encerrar o servidor.\n");
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("[SERVIDOR] Erro fatal ao iniciar o servidor:");
            System.err.println("  Mensagem: " + e.getMessage());
            System.err.println("  Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
