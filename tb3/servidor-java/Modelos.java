package servidor;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente do sistema de telefonia
 */
class Cliente {
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private List<Linha> linhas;
    
    public Cliente(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.linhas = new ArrayList<>();
    }
    
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public List<Linha> getLinhas() { return linhas; }
    
    public void adicionarLinha(Linha linha) {
        this.linhas.add(linha);
    }
    
    public void removerLinha(Linha linha) {
        this.linhas.remove(linha);
    }
}

/**
 * Linha telefônica
 */
class Linha {
    private String numero;
    private Cliente proprietario;
    private int minutosConsumidos;
    private int totalChamadas;
    
    public Linha(String numero, Cliente proprietario) {
        this.numero = numero;
        this.proprietario = proprietario;
        this.minutosConsumidos = 0;
        this.totalChamadas = 0;
    }
    
    public String getNumero() { return numero; }
    public Cliente getProprietario() { return proprietario; }
    public int getMinutosConsumidos() { return minutosConsumidos; }
    public int getTotalChamadas() { return totalChamadas; }
    
    public void registrarChamada(int duracao) {
        this.minutosConsumidos += duracao;
        this.totalChamadas++;
    }
}

/**
 * Chamada telefônica
 */
class Chamada {
    private String numeroOrigem;
    private String numeroDestino;
    private int duracao;
    private String dataHora;
    
    public Chamada(String numeroOrigem, String numeroDestino, int duracao) {
        this.numeroOrigem = numeroOrigem;
        this.numeroDestino = numeroDestino;
        this.duracao = duracao;
        this.dataHora = new java.util.Date().toString();
    }
    
    public String getNumeroOrigem() { return numeroOrigem; }
    public String getNumeroDestino() { return numeroDestino; }
    public int getDuracao() { return duracao; }
}

/**
 * Fatura
 */
class Fatura {
    private String cpfCliente;
    private double valorTotal;
    private int minutosTotal;
    private int chamadasTotal;
    private String dataGeracao;
    
    public Fatura(String cpfCliente, int minutosTotal, int chamadasTotal) {
        this.cpfCliente = cpfCliente;
        this.minutosTotal = minutosTotal;
        this.chamadasTotal = chamadasTotal;
        this.valorTotal = minutosTotal * 0.50;
        this.dataGeracao = new java.util.Date().toString();
    }
    
    @Override
    public String toString() {
        return String.format("Fatura{cpf='%s', chamadas=%d, minutos=%d, valor=R$ %.2f, data=%s}",
                           cpfCliente, chamadasTotal, minutosTotal, valorTotal, dataGeracao);
    }
}
