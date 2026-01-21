package tb2.protocol;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Mensagem de Requisição do Protocolo RMI Manual
 * Segue o formato descrito na seção 5.2 do livro texto:
 * - messageType (Request)
 * - requestId (identificador único da requisição)
 * - objectReference (nome do objeto que fornece o serviço)
 * - methodId (nome do método a ser invocado)
 * - arguments (argumentos do método em representação externa - JSON)
 */
public class MensagemRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int messageType = 0; // 0 = Request
    private int requestId;
    private String objectReference; // Nome do objeto remoto
    private String methodId; // Nome do método
    private byte[] arguments; // Argumentos serializados em JSON
    
    private static final Gson gson = new Gson();
    
    public MensagemRequest() {}
    
    public MensagemRequest(int requestId, String objectReference, String methodId, Map<String, Object> args) {
        this.requestId = requestId;
        this.objectReference = objectReference;
        this.methodId = methodId;
        // Serializar argumentos para JSON (representação externa de dados)
        this.arguments = gson.toJson(args).getBytes();
    }
    
    // Getters e Setters
    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }
    
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    
    public String getObjectReference() { return objectReference; }
    public void setObjectReference(String objectReference) { this.objectReference = objectReference; }
    
    public String getMethodId() { return methodId; }
    public void setMethodId(String methodId) { this.methodId = methodId; }
    
    public byte[] getArguments() { return arguments; }
    public void setArguments(byte[] arguments) { this.arguments = arguments; }
    
    /**
     * Desserializa os argumentos de JSON para Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getArgumentsAsMap() {
        if (arguments == null || arguments.length == 0) {
            return new HashMap<>();
        }
        String json = new String(arguments);
        return gson.fromJson(json, Map.class);
    }
    
    @Override
    public String toString() {
        return "MensagemRequest{requestId=" + requestId + 
               ", objectRef='" + objectReference + 
               "', methodId='" + methodId + 
               "', argsSize=" + (arguments != null ? arguments.length : 0) + "}";
    }
}
