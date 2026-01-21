package tb2.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ENTIDADE 4: Fatura de telefonia
 */
public class Fatura implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String cpfCliente;
    private double valorTotal;
    private int minutosTotal;
    private int chamadasTotal;
    private LocalDateTime dataGeracao;
    private static final double VALOR_POR_MINUTO = 0.50;
    
    public Fatura() {}
    
    public Fatura(String cpfCliente, int minutosTotal, int chamadasTotal) {
        this.cpfCliente = cpfCliente;
        this.minutosTotal = minutosTotal;
        this.chamadasTotal = chamadasTotal;
        this.valorTotal = minutosTotal * VALOR_POR_MINUTO;
        this.dataGeracao = LocalDateTime.now();
    }
    
    public String getCpfCliente() { return cpfCliente; }
    public void setCpfCliente(String cpfCliente) { this.cpfCliente = cpfCliente; }
    
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    
    public int getMinutosTotal() { return minutosTotal; }
    public void setMinutosTotal(int minutosTotal) { this.minutosTotal = minutosTotal; }
    
    public int getChamadasTotal() { return chamadasTotal; }
    public void setChamadasTotal(int chamadasTotal) { this.chamadasTotal = chamadasTotal; }
    
    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("Fatura{cpf='%s', chamadas=%d, minutos=%d, valor=R$ %.2f, data=%s}",
                           cpfCliente, chamadasTotal, minutosTotal, valorTotal, dataGeracao.format(formatter));
    }
}
