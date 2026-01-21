package tb2.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ENTIDADE 3: Chamada telefônica
 */
public class Chamada implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String numeroOrigem;
    private String numeroDestino;
    private int duracao; // em minutos
    private LocalDateTime dataHora;
    
    public Chamada() {}
    
    public Chamada(String numeroOrigem, String numeroDestino, int duracao) {
        this.numeroOrigem = numeroOrigem;
        this.numeroDestino = numeroDestino;
        this.duracao = duracao;
        this.dataHora = LocalDateTime.now();
    }
    
    public String getNumeroOrigem() { return numeroOrigem; }
    public void setNumeroOrigem(String numeroOrigem) { this.numeroOrigem = numeroOrigem; }
    
    public String getNumeroDestino() { return numeroDestino; }
    public void setNumeroDestino(String numeroDestino) { this.numeroDestino = numeroDestino; }
    
    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "Chamada{origem='" + numeroOrigem + "', destino='" + numeroDestino + 
               "', duracao=" + duracao + "min, data=" + dataHora.format(formatter) + "}";
    }
}
