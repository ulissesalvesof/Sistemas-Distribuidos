package tb2.model;

import java.io.Serializable;

/**
 * Classe base para entidades do tipo pessoa (COMPOSIÇÃO POR EXTENSÃO)
 * Hierarquia "é-um"
 */
public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String nome;
    protected String cpf;
    protected String telefone;
    
    public Pessoa() {}
    
    public Pessoa(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    @Override
    public String toString() {
        return "Pessoa{nome='" + nome + "', cpf='" + cpf + "', telefone='" + telefone + "'}";
    }
}
