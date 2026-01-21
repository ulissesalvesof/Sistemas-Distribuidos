package tb2.server;

import tb2.common.RemoteInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementação do servidor RMI para gerenciamento de telefonia
 */
public class ServerImpl extends UnicastRemoteObject implements RemoteInterface {
    
    // Estruturas de dados para armazenar informações
    private Map<String, Cliente> clientes;
    private Map<String, Linha> linhas;
    private List<Chamada> chamadas;
    private Map<String, List<Fatura>> faturas;
    private int contadorClientes;
    
    public ServerImpl() throws RemoteException {
        super();
        this.clientes = new HashMap<>();
        this.linhas = new HashMap<>();
        this.chamadas = new ArrayList<>();
        this.faturas = new HashMap<>();
        this.contadorClientes = 0;
        System.out.println("[SERVIDOR] Servidor RMI inicializado com sucesso!");
    }
    
    @Override
    public synchronized String adicionarCliente(String nome, String cpf) throws RemoteException {
        if (clientes.containsKey(cpf)) {
            return "ERRO: Cliente com CPF " + cpf + " já existe!";
        }
        
        Cliente cliente = new Cliente(nome, cpf);
        clientes.put(cpf, cliente);
        contadorClientes++;
        
        String msg = String.format("[SERVIDOR] Cliente adicionado: %s (CPF: %s) - Total: %d clientes",
                nome, cpf, contadorClientes);
        System.out.println(msg);
        return "SUCESSO: Cliente " + nome + " adicionado com sucesso!";
    }
    
    @Override
    public synchronized boolean removerCliente(String cpf) throws RemoteException {
        if (!clientes.containsKey(cpf)) {
            System.out.println("[SERVIDOR] Tentativa de remover cliente inexistente: " + cpf);
            return false;
        }
        
        Cliente cliente = clientes.get(cpf);
        
        // Remove todas as linhas do cliente
        List<String> linhasParaRemover = new ArrayList<>(cliente.getLinhas());
        for (String numero : linhasParaRemover) {
            removerLinha(cpf, numero);
        }
        
        clientes.remove(cpf);
        faturas.remove(cpf);
        contadorClientes--;
        
        System.out.println("[SERVIDOR] Cliente removido: " + cliente.getNome() + " (CPF: " + cpf + ")");
        return true;
    }
    
    @Override
    public synchronized String consultarCliente(String cpf) throws RemoteException {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== INFORMAÇÕES DO CLIENTE ===\n");
        sb.append("Nome: ").append(cliente.getNome()).append("\n");
        sb.append("CPF: ").append(cliente.getCpf()).append("\n");
        sb.append("Linhas: ");
        
        if (cliente.getLinhas().isEmpty()) {
            sb.append("Nenhuma linha cadastrada\n");
        } else {
            sb.append("\n");
            for (String numero : cliente.getLinhas()) {
                Linha linha = linhas.get(numero);
                sb.append("  - ").append(numero);
                sb.append(" (Chamadas: ").append(linha.getQuantidadeChamadas()).append(")");
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public synchronized List<String> listarClientes() throws RemoteException {
        List<String> lista = new ArrayList<>();
        
        if (clientes.isEmpty()) {
            lista.add("Nenhum cliente cadastrado.");
            return lista;
        }
        
        lista.add("=== LISTA DE CLIENTES ===");
        lista.add(String.format("Total: %d cliente(s)\n", clientes.size()));
        
        for (Cliente cliente : clientes.values()) {
            String info = String.format("- %s (CPF: %s) - %d linha(s)", 
                    cliente.getNome(), 
                    cliente.getCpf(), 
                    cliente.getLinhas().size());
            lista.add(info);
        }
        
        return lista;
    }
    
    @Override
    public synchronized String adicionarLinha(String cpf, String numero) throws RemoteException {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        if (linhas.containsKey(numero)) {
            return "ERRO: Número de telefone já está em uso!";
        }
        
        Linha linha = new Linha(numero, cpf);
        linhas.put(numero, linha);
        cliente.adicionarLinha(numero);
        
        String msg = String.format("[SERVIDOR] Linha %s adicionada ao cliente %s", 
                numero, cliente.getNome());
        System.out.println(msg);
        return "SUCESSO: Linha " + numero + " adicionada com sucesso!";
    }
    
    @Override
    public synchronized boolean removerLinha(String cpf, String numero) throws RemoteException {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null || !linhas.containsKey(numero)) {
            return false;
        }
        
        Linha linha = linhas.get(numero);
        if (!linha.getCpfProprietario().equals(cpf)) {
            return false;
        }
        
        cliente.removerLinha(numero);
        linhas.remove(numero);
        
        System.out.println("[SERVIDOR] Linha " + numero + " removida do cliente " + cliente.getNome());
        return true;
    }
    
    @Override
    public synchronized boolean registrarChamada(String numeroOrigem, String numeroDestino, int duracao) 
            throws RemoteException {
        Linha linhaOrigem = linhas.get(numeroOrigem);
        
        if (linhaOrigem == null) {
            System.out.println("[SERVIDOR] Erro: Linha de origem não encontrada: " + numeroOrigem);
            return false;
        }
        
        if (duracao <= 0) {
            System.out.println("[SERVIDOR] Erro: Duração inválida: " + duracao);
            return false;
        }
        
        Chamada chamada = new Chamada(numeroOrigem, numeroDestino, duracao);
        chamadas.add(chamada);
        linhaOrigem.registrarChamada(duracao);
        
        System.out.println(String.format("[SERVIDOR] Chamada registrada: %s -> %s (%d min)", 
                numeroOrigem, numeroDestino, duracao));
        return true;
    }
    
    @Override
    public synchronized String gerarFatura(String cpf) throws RemoteException {
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            return "ERRO: Cliente não encontrado!";
        }
        
        double valorTotal = 0.0;
        int totalMinutos = 0;
        int totalChamadas = 0;
        double valorPorMinuto = 0.50; // R$ 0,50 por minuto
        
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         FATURA DE TELEFONIA            \n");
        sb.append("========================================\n");
        sb.append("Cliente: ").append(cliente.getNome()).append("\n");
        sb.append("CPF: ").append(cliente.getCpf()).append("\n");
        sb.append("Data: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DETALHAMENTO POR LINHA:\n\n");
        
        for (String numero : cliente.getLinhas()) {
            Linha linha = linhas.get(numero);
            int minutos = linha.getMinutosConsumidos();
            int chamadas = linha.getQuantidadeChamadas();
            double valor = minutos * valorPorMinuto;
            
            sb.append(String.format("Linha: %s\n", numero));
            sb.append(String.format("  Chamadas: %d\n", chamadas));
            sb.append(String.format("  Minutos: %d\n", minutos));
            sb.append(String.format("  Valor: R$ %.2f\n\n", valor));
            
            totalMinutos += minutos;
            totalChamadas += chamadas;
            valorTotal += valor;
        }
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("Total de Chamadas: %d\n", totalChamadas));
        sb.append(String.format("Total de Minutos: %d\n", totalMinutos));
        sb.append(String.format("Valor por Minuto: R$ %.2f\n", valorPorMinuto));
        sb.append(String.format("VALOR TOTAL: R$ %.2f\n", valorTotal));
        sb.append("========================================\n");
        
        // Salva a fatura
        Fatura fatura = new Fatura(cpf, valorTotal, totalMinutos, totalChamadas);
        if (!faturas.containsKey(cpf)) {
            faturas.put(cpf, new ArrayList<>());
        }
        faturas.get(cpf).add(fatura);
        
        System.out.println("[SERVIDOR] Fatura gerada para " + cliente.getNome() + 
                " - Valor: R$ " + String.format("%.2f", valorTotal));
        
        return sb.toString();
    }
    
    @Override
    public synchronized List<String> listarFaturas(String cpf) throws RemoteException {
        List<String> lista = new ArrayList<>();
        
        Cliente cliente = clientes.get(cpf);
        if (cliente == null) {
            lista.add("ERRO: Cliente não encontrado!");
            return lista;
        }
        
        List<Fatura> faturasList = faturas.get(cpf);
        
        if (faturasList == null || faturasList.isEmpty()) {
            lista.add("Nenhuma fatura encontrada para este cliente.");
            return lista;
        }
        
        lista.add("=== HISTÓRICO DE FATURAS ===");
        lista.add("Cliente: " + cliente.getNome() + " (CPF: " + cpf + ")\n");
        
        for (int i = 0; i < faturasList.size(); i++) {
            Fatura f = faturasList.get(i);
            lista.add(String.format("Fatura #%d - Data: %s", (i + 1), f.getDataFormatada()));
            lista.add(String.format("  Valor: R$ %.2f | Minutos: %d | Chamadas: %d\n",
                    f.getValor(), f.getMinutos(), f.getChamadas()));
        }
        
        return lista;
    }
    
    @Override
    public synchronized String obterEstatisticas() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("      ESTATÍSTICAS DO SISTEMA           \n");
        sb.append("========================================\n");
        sb.append(String.format("Total de Clientes: %d\n", clientes.size()));
        sb.append(String.format("Total de Linhas: %d\n", linhas.size()));
        sb.append(String.format("Total de Chamadas: %d\n", chamadas.size()));
        
        int totalMinutos = chamadas.stream().mapToInt(Chamada::getDuracao).sum();
        sb.append(String.format("Total de Minutos: %d\n", totalMinutos));
        
        double mediaDuracao = chamadas.isEmpty() ? 0 : 
                (double) totalMinutos / chamadas.size();
        sb.append(String.format("Média de Duração: %.2f min\n", mediaDuracao));
        
        sb.append("========================================\n");
        
        System.out.println("[SERVIDOR] Estatísticas solicitadas");
        return sb.toString();
    }
    
    // Classes internas para representar as entidades
    private class Cliente {
        private String nome;
        private String cpf;
        private List<String> linhas;
        
        public Cliente(String nome, String cpf) {
            this.nome = nome;
            this.cpf = cpf;
            this.linhas = new ArrayList<>();
        }
        
        public String getNome() { return nome; }
        public String getCpf() { return cpf; }
        public List<String> getLinhas() { return linhas; }
        
        public void adicionarLinha(String numero) {
            linhas.add(numero);
        }
        
        public void removerLinha(String numero) {
            linhas.remove(numero);
        }
    }
    
    private class Linha {
        private String numero;
        private String cpfProprietario;
        private int minutosConsumidos;
        private int quantidadeChamadas;
        
        public Linha(String numero, String cpfProprietario) {
            this.numero = numero;
            this.cpfProprietario = cpfProprietario;
            this.minutosConsumidos = 0;
            this.quantidadeChamadas = 0;
        }
        
        public String getCpfProprietario() { return cpfProprietario; }
        public int getMinutosConsumidos() { return minutosConsumidos; }
        public int getQuantidadeChamadas() { return quantidadeChamadas; }
        
        public void registrarChamada(int duracao) {
            this.minutosConsumidos += duracao;
            this.quantidadeChamadas++;
        }
    }
    
    private class Chamada {
        private String numeroOrigem;
        private String numeroDestino;
        private int duracao;
        private LocalDateTime dataHora;
        
        public Chamada(String numeroOrigem, String numeroDestino, int duracao) {
            this.numeroOrigem = numeroOrigem;
            this.numeroDestino = numeroDestino;
            this.duracao = duracao;
            this.dataHora = LocalDateTime.now();
        }
        
        public int getDuracao() { return duracao; }
    }
    
    private class Fatura {
        private String cpf;
        private double valor;
        private int minutos;
        private int chamadas;
        private LocalDateTime data;
        
        public Fatura(String cpf, double valor, int minutos, int chamadas) {
            this.cpf = cpf;
            this.valor = valor;
            this.minutos = minutos;
            this.chamadas = chamadas;
            this.data = LocalDateTime.now();
        }
        
        public double getValor() { return valor; }
        public int getMinutos() { return minutos; }
        public int getChamadas() { return chamadas; }
        
        public String getDataFormatada() {
            return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
    }
}
