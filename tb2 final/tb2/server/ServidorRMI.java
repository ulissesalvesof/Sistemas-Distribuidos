package tb2.server;

import tb2.model.*;
import tb2.protocol.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Servidor RMI Manual com Protocolo Requisição-Resposta
 * Implementa passagem por referência (objeto remoto) e passagem por valor (objetos locais)
 */
public class ServidorRMI {
    private static final int PORTA = 5000;
    private static final Gson gson = new Gson();
    
    // OBJETOS REMOTOS (Passagem por Referência)
    private static ServicoTelefonia servicoTelefonia = new ServicoTelefonia();
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   SERVIDOR RMI MANUAL - PROTOCOLO REQUISIÇÃO-RESPOSTA  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("🔌 Porta: " + PORTA);
        System.out.println("📦 Objeto Remoto: ServicoTelefonia");
        System.out.println("🔄 Protocolo: Requisição-Resposta (Seção 5.2)");
        System.out.println("📄 Serialização: JSON (Representação Externa)");
        System.out.println("\n🖥️  Aguardando conexões...\n");
        
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("\n✅ Cliente conectado: " + clientSocket.getInetAddress());
                    
                    // Processar cliente em nova thread
                    new Thread(() -> processarCliente(clientSocket)).start();
                    
                } catch (IOException e) {
                    System.err.println("❌ Erro ao aceitar cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao iniciar servidor: " + e.getMessage());
        }
    }
    
    /**
     * Processa requisição do cliente usando protocolo requisição-resposta
     */
    private static void processarCliente(Socket clientSocket) {
        try {
            // 1️⃣ getRequest(): Obter requisição do cliente
            MensagemRequest request = RequestHandler.getRequest(clientSocket);
            
            // 2️⃣ Processar requisição
            MensagemReply reply = processarRequisicao(request);
            
            // 3️⃣ sendReply(): Enviar resposta ao cliente
            RequestHandler.sendReply(reply, clientSocket);
            
            clientSocket.close();
            System.out.println("✅ Cliente desconectado\n");
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao processar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Processa a requisição e invoca o método no objeto remoto (PASSAGEM POR REFERÊNCIA)
     */
    private static MensagemReply processarRequisicao(MensagemRequest request) {
        try {
            String objectRef = request.getObjectReference();
            String methodId = request.getMethodId();
            Map<String, Object> args = request.getArgumentsAsMap();
            
            System.out.println("📋 Processando: objectRef=" + objectRef + ", method=" + methodId);
            
            // Verificar objeto remoto
            if (!"ServicoTelefonia".equals(objectRef)) {
                return new MensagemReply(request.getRequestId(), "Objeto remoto não encontrado: " + objectRef);
            }
            
            // Invocar método no objeto remoto (PASSAGEM POR REFERÊNCIA)
            Object resultado = invocarMetodo(methodId, args);
            
            // Retornar resposta com sucesso
            return new MensagemReply(request.getRequestId(), resultado);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar requisição: " + e.getMessage());
            return new MensagemReply(request.getRequestId(), "Erro: " + e.getMessage());
        }
    }
    
    /**
     * Invoca método no objeto remoto usando PASSAGEM POR REFERÊNCIA
     * Os objetos são manipulados no servidor (não enviados inteiros ao cliente)
     */
    private static Object invocarMetodo(String methodId, Map<String, Object> args) throws Exception {
        switch (methodId) {
            case "adicionarCliente":
                // PASSAGEM POR VALOR: Parâmetros simples (String)
                String nome = (String) args.get("nome");
                String cpf = (String) args.get("cpf");
                String telefone = (String) args.get("telefone");
                String email = (String) args.get("email");
                return servicoTelefonia.adicionarCliente(nome, cpf, telefone, email);
                
            case "removerCliente":
                cpf = (String) args.get("cpf");
                return servicoTelefonia.removerCliente(cpf);
                
            case "consultarCliente":
                cpf = (String) args.get("cpf");
                return servicoTelefonia.consultarCliente(cpf);
                
            case "listarClientes":
                return servicoTelefonia.listarClientes();
                
            case "adicionarLinha":
                cpf = (String) args.get("cpf");
                String numero = (String) args.get("numero");
                return servicoTelefonia.adicionarLinha(cpf, numero);
                
            case "removerLinha":
                cpf = (String) args.get("cpf");
                numero = (String) args.get("numero");
                return servicoTelefonia.removerLinha(cpf, numero);
                
            case "registrarChamada":
                String origem = (String) args.get("origem");
                String destino = (String) args.get("destino");
                int duracao = ((Double) args.get("duracao")).intValue();
                return servicoTelefonia.registrarChamada(origem, destino, duracao);
                
            case "gerarFatura":
                cpf = (String) args.get("cpf");
                return servicoTelefonia.gerarFatura(cpf);
                
            case "listarFaturas":
                cpf = (String) args.get("cpf");
                return servicoTelefonia.listarFaturas(cpf);
                
            case "obterEstatisticas":
                return servicoTelefonia.obterEstatisticas();
                
            default:
                throw new Exception("Método não encontrado: " + methodId);
        }
    }
}
