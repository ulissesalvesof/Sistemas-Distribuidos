package tb2.protocol;

import java.io.*;
import java.net.*;

/**
 * Manipulador de Requisições seguindo o protocolo da Seção 5.2 do livro texto
 * Implementa os métodos:
 * - doOperation: envia requisição e aguarda resposta (usado pelo cliente)
 * - getRequest: obtém requisição de um cliente (usado pelo servidor)
 * - sendReply: envia resposta ao cliente (usado pelo servidor)
 */
public class RequestHandler {
    
    /**
     * MÉTODO DO CLIENTE:
     * Envia uma mensagem de requisição para o objeto remoto e retorna a resposta.
     * 
     * @param o RemoteObjectRef - referência ao objeto remoto
     * @param methodId String - identificador do método a ser invocado
     * @param arguments byte[] - argumentos do método em formato externo (JSON)
     * @return byte[] - resposta do servidor
     */
    public static byte[] doOperation(RemoteObjectRef o, String methodId, byte[] arguments) throws IOException {
        Socket socket = null;
        try {
            // Conectar ao servidor remoto
            socket = new Socket(o.getServerHost(), o.getServerPort());
            
            // Criar mensagem de requisição
            int requestId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            MensagemRequest request = new MensagemRequest();
            request.setRequestId(requestId);
            request.setObjectReference(o.getObjectName());
            request.setMethodId(methodId);
            request.setArguments(arguments);
            
            // Enviar requisição
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(request);
            saida.flush();
            
            System.out.println("[doOperation] Enviado: " + request);
            
            // Receber resposta
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            MensagemReply reply = (MensagemReply) entrada.readObject();
            
            System.out.println("[doOperation] Recebido: " + reply);
            
            if (!reply.isSuccess()) {
                throw new IOException("Erro no servidor: " + reply.getErrorMessage());
            }
            
            return reply.getResult();
            
        } catch (ClassNotFoundException e) {
            throw new IOException("Erro ao desserializar resposta: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    
    /**
     * MÉTODO DO SERVIDOR:
     * Obtém uma requisição de um cliente através de um socket.
     * 
     * @param clientSocket Socket conectado ao cliente
     * @return MensagemRequest - requisição recebida do cliente
     */
    public static MensagemRequest getRequest(Socket clientSocket) throws IOException {
        try {
            ObjectInputStream entrada = new ObjectInputStream(clientSocket.getInputStream());
            MensagemRequest request = (MensagemRequest) entrada.readObject();
            
            System.out.println("[getRequest] Recebido: " + request);
            
            return request;
            
        } catch (ClassNotFoundException e) {
            throw new IOException("Erro ao desserializar requisição: " + e.getMessage());
        }
    }
    
    /**
     * MÉTODO DO SERVIDOR:
     * Envia a mensagem de resposta para o cliente.
     * 
     * @param reply MensagemReply - resposta a ser enviada
     * @param clientSocket Socket conectado ao cliente
     */
    public static void sendReply(MensagemReply reply, Socket clientSocket) throws IOException {
        ObjectOutputStream saida = new ObjectOutputStream(clientSocket.getOutputStream());
        saida.writeObject(reply);
        saida.flush();
        
        System.out.println("[sendReply] Enviado: " + reply);
    }
}
