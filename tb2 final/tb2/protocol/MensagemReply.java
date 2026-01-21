package tb2.protocol;

import com.google.gson.Gson;
import java.io.Serializable;

/**
 * Mensagem de Resposta do Protocolo RMI Manual
 * Segue o formato descrito na seção 5.2 do livro texto:
 * - messageType (Reply)
 * - requestId (identificador da requisição correspondente)
 * - result (resultado da invocação em representação externa - JSON)
 */
public class MensagemReply implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int messageType = 1; // 1 = Reply
    private int requestId;
    private byte[] result; // Resultado serializado em JSON
    private boolean success;
    private String errorMessage;
    
    private static final Gson gson = new Gson();
    
    public MensagemReply() {}
    
    public MensagemReply(int requestId, Object resultado) {
        this.requestId = requestId;
        this.success = true;
        // Serializar resultado para JSON (representação externa de dados)
        this.result = gson.toJson(resultado).getBytes();
    }
    
    public MensagemReply(int requestId, String errorMessage) {
        this.requestId = requestId;
        this.success = false;
        this.errorMessage = errorMessage;
        this.result = new byte[0];
    }
    
    // Getters e Setters
    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }
    
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    
    public byte[] getResult() { return result; }
    public void setResult(byte[] result) { this.result = result; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    /**
     * Desserializa o resultado de JSON para o tipo especificado
     */
    public <T> T getResultAs(Class<T> tipo) {
        if (result == null || result.length == 0) {
            return null;
        }
        String json = new String(result);
        return gson.fromJson(json, tipo);
    }
    
    /**
     * Retorna o resultado como String
     */
    public String getResultAsString() {
        if (result == null || result.length == 0) {
            return null;
        }
        return new String(result);
    }
    
    @Override
    public String toString() {
        return "MensagemReply{requestId=" + requestId + 
               ", success=" + success + 
               ", resultSize=" + (result != null ? result.length : 0) + 
               ", error='" + (errorMessage != null ? errorMessage : "none") + "'}";
    }
}
