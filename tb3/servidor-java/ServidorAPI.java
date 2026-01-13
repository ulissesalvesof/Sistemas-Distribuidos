package servidor;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * Servidor REST API para o Sistema de Telefonia
 * Trabalho 3 - Web Service / API REST
 * Porta: 8080
 */
public class ServidorAPI {
    
    private static final int PORTA = 8080;
    private static final Gson gson = new Gson();
    private static ServicoTelefonia servico = new ServicoTelefonia();
    
    public static void main(String[] args) throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress(PORTA), 0);
        
        // Configurar endpoints REST
        servidor.createContext("/api/clientes", new ClienteHandler());
        servidor.createContext("/api/linhas", new LinhaHandler());
        servidor.createContext("/api/chamadas", new ChamadaHandler());
        servidor.createContext("/api/faturas", new FaturaHandler());
        servidor.createContext("/api/estatisticas", new EstatisticasHandler());
        servidor.createContext("/api/health", new HealthHandler());
        
        // Executor para threads
        servidor.setExecutor(Executors.newFixedThreadPool(10));
        servidor.start();
        
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║         SERVIDOR REST API - SISTEMA TELEFONIA          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("🌐 Porta: " + PORTA);
        System.out.println("📡 Protocolo: HTTP REST");
        System.out.println("📄 Formato: JSON");
        System.out.println("\n🔗 Endpoints disponíveis:");
        System.out.println("   GET    /api/health           - Status do servidor");
        System.out.println("   POST   /api/clientes         - Adicionar cliente");
        System.out.println("   GET    /api/clientes         - Listar clientes");
        System.out.println("   GET    /api/clientes/{cpf}   - Consultar cliente");
        System.out.println("   DELETE /api/clientes/{cpf}   - Remover cliente");
        System.out.println("   POST   /api/linhas           - Adicionar linha");
        System.out.println("   DELETE /api/linhas           - Remover linha");
        System.out.println("   POST   /api/chamadas         - Registrar chamada");
        System.out.println("   POST   /api/faturas          - Gerar fatura");
        System.out.println("   GET    /api/faturas/{cpf}    - Listar faturas");
        System.out.println("   GET    /api/estatisticas     - Obter estatísticas");
        System.out.println("\n🚀 Servidor iniciado! Aguardando requisições...\n");
    }
    
    // Handler de Health Check
    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "OK");
            response.put("service", "Sistema de Telefonia API");
            response.put("version", "1.0");
            
            enviarResposta(exchange, 200, response);
        }
    }
    
    // Handler de Clientes
    static class ClienteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String metodo = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            System.out.println("📨 " + metodo + " " + path);
            
            try {
                if ("POST".equals(metodo)) {
                    // Adicionar cliente
                    JsonObject body = lerBody(exchange);
                    String resultado = servico.adicionarCliente(
                        body.get("nome").getAsString(),
                        body.get("cpf").getAsString(),
                        body.get("telefone").getAsString(),
                        body.get("email").getAsString()
                    );
                    enviarRespostaTexto(exchange, 201, resultado);
                    
                } else if ("GET".equals(metodo)) {
                    String[] partes = path.split("/");
                    if (partes.length > 3) {
                        // GET /api/clientes/{cpf}
                        String cpf = partes[3];
                        String resultado = servico.consultarCliente(cpf);
                        enviarRespostaTexto(exchange, 200, resultado);
                    } else {
                        // GET /api/clientes
                        List<String> clientes = servico.listarClientes();
                        enviarResposta(exchange, 200, clientes);
                    }
                    
                } else if ("DELETE".equals(metodo)) {
                    String[] partes = path.split("/");
                    String cpf = partes[3];
                    String resultado = servico.removerCliente(cpf);
                    enviarRespostaTexto(exchange, 200, resultado);
                    
                } else {
                    enviarErro(exchange, 405, "Método não permitido");
                }
            } catch (Exception e) {
                enviarErro(exchange, 500, "Erro: " + e.getMessage());
            }
        }
    }
    
    // Handler de Linhas
    static class LinhaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String metodo = exchange.getRequestMethod();
            System.out.println("📨 " + metodo + " " + exchange.getRequestURI().getPath());
            
            try {
                if ("POST".equals(metodo)) {
                    JsonObject body = lerBody(exchange);
                    String resultado = servico.adicionarLinha(
                        body.get("cpf").getAsString(),
                        body.get("numero").getAsString()
                    );
                    enviarRespostaTexto(exchange, 201, resultado);
                    
                } else if ("DELETE".equals(metodo)) {
                    JsonObject body = lerBody(exchange);
                    String resultado = servico.removerLinha(
                        body.get("cpf").getAsString(),
                        body.get("numero").getAsString()
                    );
                    enviarRespostaTexto(exchange, 200, resultado);
                    
                } else {
                    enviarErro(exchange, 405, "Método não permitido");
                }
            } catch (Exception e) {
                enviarErro(exchange, 500, "Erro: " + e.getMessage());
            }
        }
    }
    
    // Handler de Chamadas
    static class ChamadaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String metodo = exchange.getRequestMethod();
            System.out.println("📨 " + metodo + " " + exchange.getRequestURI().getPath());
            
            try {
                if ("POST".equals(metodo)) {
                    JsonObject body = lerBody(exchange);
                    String resultado = servico.registrarChamada(
                        body.get("origem").getAsString(),
                        body.get("destino").getAsString(),
                        body.get("duracao").getAsInt()
                    );
                    enviarRespostaTexto(exchange, 201, resultado);
                } else {
                    enviarErro(exchange, 405, "Método não permitido");
                }
            } catch (Exception e) {
                enviarErro(exchange, 500, "Erro: " + e.getMessage());
            }
        }
    }
    
    // Handler de Faturas
    static class FaturaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String metodo = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("📨 " + metodo + " " + path);
            
            try {
                if ("POST".equals(metodo)) {
                    JsonObject body = lerBody(exchange);
                    String resultado = servico.gerarFatura(body.get("cpf").getAsString());
                    enviarRespostaTexto(exchange, 201, resultado);
                    
                } else if ("GET".equals(metodo)) {
                    String[] partes = path.split("/");
                    String cpf = partes[3];
                    List<String> faturas = servico.listarFaturas(cpf);
                    enviarResposta(exchange, 200, faturas);
                    
                } else {
                    enviarErro(exchange, 405, "Método não permitido");
                }
            } catch (Exception e) {
                enviarErro(exchange, 500, "Erro: " + e.getMessage());
            }
        }
    }
    
    // Handler de Estatísticas
    static class EstatisticasHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("📨 GET " + exchange.getRequestURI().getPath());
            
            try {
                String resultado = servico.obterEstatisticas();
                enviarRespostaTexto(exchange, 200, resultado);
            } catch (Exception e) {
                enviarErro(exchange, 500, "Erro: " + e.getMessage());
            }
        }
    }
    
    // Métodos auxiliares
    private static JsonObject lerBody(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        return gson.fromJson(isr, JsonObject.class);
    }
    
    private static void enviarResposta(HttpExchange exchange, int codigo, Object dados) throws IOException {
        String json = gson.toJson(dados);
        byte[] resposta = json.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(codigo, resposta.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(resposta);
        os.close();
    }
    
    private static void enviarRespostaTexto(HttpExchange exchange, int codigo, String texto) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("message", texto);
        enviarResposta(exchange, codigo, response);
    }
    
    private static void enviarErro(HttpExchange exchange, int codigo, String mensagem) throws IOException {
        Map<String, String> erro = new HashMap<>();
        erro.put("error", mensagem);
        enviarResposta(exchange, codigo, erro);
    }
}
