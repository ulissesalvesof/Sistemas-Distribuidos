package tb2.model;

import java.io.Serializable;

/**
 * ENTIDADE 5: Funcionário da operadora de telefonia
 * COMPOSIÇÃO POR EXTENSÃO: Funcionario "é-um" tipo de Pessoa
 */
public class Funcionario extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String matricula;
    private String cargo;
    private double salario;
    
    public Funcionario() {
        super();
    }
    
    public Funcionario(String nome, String cpf, String telefone, String matricula, String cargo, double salario) {
        super(nome, cpf, telefone);
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
    }
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
    
    @Override
    public String toString() {
        return "Funcionario{nome='" + nome + "', cpf='" + cpf + "', matricula='" + matricula + 
               "', cargo='" + cargo + "', salario=" + salario + "}";
    }
}
