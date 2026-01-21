package tb2.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface remota para o sistema de gerenciamento de telefonia
 */
public interface RemoteInterface extends Remote {
    
    /**
     * Adiciona um novo cliente ao sistema
     */
    String adicionarCliente(String nome, String cpf) throws RemoteException;
    
    /**
     * Remove um cliente do sistema
     */
    boolean removerCliente(String cpf) throws RemoteException;
    
    /**
     * Consulta informações de um cliente
     */
    String consultarCliente(String cpf) throws RemoteException;
    
    /**
     * Lista todos os clientes cadastrados
     */
    List<String> listarClientes() throws RemoteException;
    
    /**
     * Adiciona uma linha telefônica a um cliente
     */
    String adicionarLinha(String cpf, String numero) throws RemoteException;
    
    /**
     * Remove uma linha telefônica de um cliente
     */
    boolean removerLinha(String cpf, String numero) throws RemoteException;
    
    /**
     * Registra uma chamada realizada
     */
    boolean registrarChamada(String numeroOrigem, String numeroDestino, int duracao) throws RemoteException;
    
    /**
     * Gera a fatura de um cliente
     */
    String gerarFatura(String cpf) throws RemoteException;
    
    /**
     * Lista todas as faturas de um cliente
     */
    List<String> listarFaturas(String cpf) throws RemoteException;
    
    /**
     * Retorna estatísticas do sistema
     */
    String obterEstatisticas() throws RemoteException;
}
