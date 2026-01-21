package tb2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDADE 1: Cliente do serviço de telefonia
 * COMPOSIÇÃO POR EXTENSÃO: Cliente "é-um" tipo de Pessoa
 */
public class Cliente extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String email;
    private List<Linha> linhas; // COMPOSIÇÃO POR AGREGAÇÃO: Cliente "tem-um" conjunto de Linhas
    
    public Cliente() {
        super();
        this.linhas = new ArrayList<>();
    }
    
    public Cliente(String nome, String cpf, String telefone, String email) {
        super(nome, cpf, telefone);
        this.email = email;
        this.linhas = new ArrayList<>();
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<Linha> getLinhas() { return linhas; }
    public void setLinhas(List<Linha> linhas) { this.linhas = linhas; }
    
    public void adicionarLinha(Linha linha) {
        this.linhas.add(linha);
    }
    
    public void removerLinha(Linha linha) {
        this.linhas.remove(linha);
    }
    
    @Override
    public String toString() {
        return "Cliente{nome='" + nome + "', cpf='" + cpf + "', telefone='" + telefone + 
               "', email='" + email + "', linhas=" + linhas.size() + "}";
    }
}
