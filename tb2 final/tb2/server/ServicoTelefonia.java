package tb2.server;

import tb2.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de Telefonia (Objeto Remoto)
 * Executa operações NO SERVIDOR (Passagem por Referência)
 * Os objetos são mantidos no servidor e não enviados inteiros ao cliente
 */
public class ServicoTelefonia {
    
    // Estruturas de dados no servidor (PASSAGEM POR REFERÊNCIA)
    private Map<String, Cliente> clientes = new ConcurrentHashMap<>();
    private Map<String, Linha> linhas = new ConcurrentHashMap<>();
    private List<Chamada> chamadas = Collections.synchronizedList(new ArrayList<>());
    private Map<String, List<Fatura>> faturas = new ConcurrentHashMap<>();
    
    /**
     * Adiciona um cliente ao sistema (PASSAGEM POR VALOR nos parâmetros)
     */
    public synchronized String adicionarCliente(String nome, String cpf, String telefone, String email) {
        if (clientes.containsKey(cpf)) {
            return "ERRO: Cliente com CPF " + cpf + " já cadastrado!";
        }
        
        Cliente cliente = new Cliente(nome, cpf, telefone, email);
        clientes.put(cpf, cliente);
        
        System.out.println("✅ Cliente adicionado: " + cliente);
        return "Cliente adicionado com sucesso!";
    }
    
    /**
     * Remove um cliente do sistema
     */
    public synchronized String removerCliente(String cpf) {
        Cliente cliente = clientes.remove(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        // Remover todas as linhas do cliente
        cliente.getLinhas().forEach(linha -> linhas.remove(linha.getNumero()));
        
        System.out.println("✅ Cliente removido: " + cpf);
        return "Cliente removido com sucesso!";
    }
    
    /**
     * Consulta informações de um cliente
     * Retorna String (PASSAGEM POR VALOR)
     */
    public synchronized String consultarCliente(String cpf) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(cliente.getNome()).append("\n");
        sb.append("CPF: ").append(cliente.getCpf()).append("\n");
        sb.append("Telefone: ").append(cliente.getTelefone()).append("\n");
        sb.append("Email: ").append(cliente.getEmail()).append("\n");
        sb.append("Linhas: ").append(cliente.getLinhas().size()).append("\n");
        
        for (Linha linha : cliente.getLinhas()) {
            sb.append("  - ").append(linha.getNumero())
              .append(" (").append(linha.getMinutosConsumidos()).append(" min, ")
              .append(linha.getTotalChamadas()).append(" chamadas)\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Lista todos os clientes
     */
    public synchronized List<String> listarClientes() {
        List<String> lista = new ArrayList<>();
        for (Cliente cliente : clientes.values()) {
            lista.add(cliente.getNome() + " - " + cliente.getCpf() + 
                     " (" + cliente.getLinhas().size() + " linhas)");
        }
        return lista;
    }
    
    /**
     * Adiciona uma linha a um cliente
     */
    public synchronized String adicionarLinha(String cpf, String numero) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        if (linhas.containsKey(numero)) {
            return "ERRO: Linha já cadastrada!";
        }
        
        Linha linha = new Linha(numero, cliente);
        linhas.put(numero, linha);
        cliente.adicionarLinha(linha);
        
        System.out.println("✅ Linha adicionada: " + numero + " para " + cliente.getNome());
        return "Linha adicionada com sucesso!";
    }
    
    /**
     * Remove uma linha de um cliente
     */
    public synchronized String removerLinha(String cpf, String numero) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        Linha linha = linhas.remove(numero);
        if (linha == null) {
            return "ERRO: Linha não encontrada!";
        }
        
        cliente.removerLinha(linha);
        
        System.out.println("✅ Linha removida: " + numero);
        return "Linha removida com sucesso!";
    }
    
    /**
     * Registra uma chamada
     */
    public synchronized String registrarChamada(String origem, String destino, int duracao) {
        Linha linhaOrigem = linhas.get(origem);
        if (linhaOrigem == null) {
            return "ERRO: Linha de origem não encontrada!";
        }
        
        if (duracao <= 0) {
            return "ERRO: Duração inválida!";
        }
        
        Chamada chamada = new Chamada(origem, destino, duracao);
        chamadas.add(chamada);
        linhaOrigem.registrarChamada(duracao);
        
        System.out.println("✅ Chamada registrada: " + chamada);
        return "Chamada registrada com sucesso!";
    }
    
    /**
     * Gera fatura de um cliente
     */
    public synchronized String gerarFatura(String cpf) {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        int minutosTotal = 0;
        int chamadasTotal = 0;
        
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         FATURA DE TELEFONIA            \n");
        sb.append("========================================\n");
        sb.append("Cliente: ").append(cliente.getNome()).append("\n");
        sb.append("CPF: ").append(cliente.getCpf()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DETALHAMENTO POR LINHA:\n\n");
        
        for (Linha linha : cliente.getLinhas()) {
            minutosTotal += linha.getMinutosConsumidos();
            chamadasTotal += linha.getTotalChamadas();
            
            double valorLinha = linha.getMinutosConsumidos() * 0.50;
            
            sb.append("Linha: ").append(linha.getNumero()).append("\n");
            sb.append("  Chamadas: ").append(linha.getTotalChamadas()).append("\n");
            sb.append("  Minutos: ").append(linha.getMinutosConsumidos()).append("\n");
            sb.append(String.format("  Valor: R$ %.2f\n\n", valorLinha));
        }
        
        double valorTotal = minutosTotal * 0.50;
        
        sb.append("----------------------------------------\n");
        sb.append("Total de Chamadas: ").append(chamadasTotal).append("\n");
        sb.append("Total de Minutos: ").append(minutosTotal).append("\n");
        sb.append("Valor por Minuto: R$ 0,50\n");
        sb.append(String.format("VALOR TOTAL: R$ %.2f\n", valorTotal));
        sb.append("========================================\n");
        
        // Armazenar fatura
        Fatura fatura = new Fatura(cpf, minutosTotal, chamadasTotal);
        faturas.computeIfAbsent(cpf, k -> new ArrayList<>()).add(fatura);
        
        System.out.println("✅ Fatura gerada para " + cliente.getNome());
        
        return sb.toString();
    }
    
    /**
     * Lista faturas de um cliente
     */
    public synchronized List<String> listarFaturas(String cpf) {
        List<Fatura> faturasCliente = faturas.get(cpf);
        List<String> lista = new ArrayList<>();
        
        if (faturasCliente == null || faturasCliente.isEmpty()) {
            lista.add("Nenhuma fatura encontrada");
            return lista;
        }
        
        for (Fatura fatura : faturasCliente) {
            lista.add(fatura.toString());
        }
        
        return lista;
    }
    
    /**
     * Obtém estatísticas do sistema
     */
    public synchronized String obterEstatisticas() {
        int totalMinutos = linhas.values().stream()
                                 .mapToInt(Linha::getMinutosConsumidos)
                                 .sum();
        
        int totalChamadas = linhas.values().stream()
                                   .mapToInt(Linha::getTotalChamadas)
                                   .sum();
        
        double mediaDuracao = totalChamadas > 0 ? (double) totalMinutos / totalChamadas : 0;
        
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("    ESTATÍSTICAS DO SISTEMA             \n");
        sb.append("========================================\n");
        sb.append("Total de Clientes: ").append(clientes.size()).append("\n");
        sb.append("Total de Linhas: ").append(linhas.size()).append("\n");
        sb.append("Total de Chamadas: ").append(totalChamadas).append("\n");
        sb.append("Total de Minutos: ").append(totalMinutos).append("\n");
        sb.append(String.format("Média de Duração: %.2f min\n", mediaDuracao));
        sb.append("========================================\n");
        
        return sb.toString();
    }
}
