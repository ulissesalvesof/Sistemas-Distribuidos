package tb2.model;

import java.io.Serializable;

/**
 * ENTIDADE 2: Linha telefônica
 * COMPOSIÇÃO POR AGREGAÇÃO: Linha "tem-um" Cliente como proprietário
 */
public class Linha implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String numero;
    private Cliente proprietario; // AGREGAÇÃO: Linha "tem-um" Cliente
    private int minutosConsumidos;
    private int totalChamadas;
    private boolean ativa;
    
    public Linha() {}
    
    public Linha(String numero, Cliente proprietario) {
        this.numero = numero;
        this.proprietario = proprietario;
        this.minutosConsumidos = 0;
        this.totalChamadas = 0;
        this.ativa = true;
    }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public Cliente getProprietario() { return proprietario; }
    public void setProprietario(Cliente proprietario) { this.proprietario = proprietario; }
    
    public int getMinutosConsumidos() { return minutosConsumidos; }
    public void setMinutosConsumidos(int minutosConsumidos) { this.minutosConsumidos = minutosConsumidos; }
    
    public int getTotalChamadas() { return totalChamadas; }
    public void setTotalChamadas(int totalChamadas) { this.totalChamadas = totalChamadas; }
    
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    
    public void registrarChamada(int duracao) {
        this.minutosConsumidos += duracao;
        this.totalChamadas++;
    }
    
    @Override
    public String toString() {
        return "Linha{numero='" + numero + "', proprietario='" + 
               (proprietario != null ? proprietario.getNome() : "N/A") + 
               "', minutos=" + minutosConsumidos + ", chamadas=" + totalChamadas + "}";
    }
}
