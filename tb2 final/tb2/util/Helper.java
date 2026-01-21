package tb2.util;

import java.util.Random;

/**
 * Classe utilitária com métodos auxiliares
 */
public class Helper {
    
    private static final Random random = new Random();
    
    /**
     * Valida um CPF (validação simples de formato)
     */
    public static boolean validarCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        
        // CPF deve ter 11 dígitos
        return cpfLimpo.length() == 11;
    }
    
    /**
     * Valida um número de telefone (validação simples)
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String telLimpo = telefone.replaceAll("[^0-9]", "");
        
        // Telefone deve ter entre 10 e 11 dígitos
        return telLimpo.length() >= 10 && telLimpo.length() <= 11;
    }
    
    /**
     * Formata um CPF com pontos e traço
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        
        return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
    }
    
    /**
     * Formata um número de telefone
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null) {
            return telefone;
        }
        
        String telLimpo = telefone.replaceAll("[^0-9]", "");
        
        if (telLimpo.length() == 10) {
            return String.format("(%s) %s-%s",
                    telLimpo.substring(0, 2),
                    telLimpo.substring(2, 6),
                    telLimpo.substring(6));
        } else if (telLimpo.length() == 11) {
            return String.format("(%s) %s-%s",
                    telLimpo.substring(0, 2),
                    telLimpo.substring(2, 7),
                    telLimpo.substring(7));
        }
        
        return telefone;
    }
    
    /**
     * Gera um CPF aleatório (para testes)
     */
    public static String gerarCPFAleatorio() {
        StringBuilder cpf = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            cpf.append(random.nextInt(10));
        }
        return cpf.toString();
    }
    
    /**
     * Gera um telefone aleatório (para testes)
     */
    public static String gerarTelefoneAleatorio() {
        return String.format("11%d%04d",
                90000000 + random.nextInt(10000000),
                random.nextInt(10000));
    }
    
    /**
     * Calcula o valor de uma chamada
     */
    public static double calcularValorChamada(int minutos, double valorPorMinuto) {
        return minutos * valorPorMinuto;
    }
    
    /**
     * Formata um valor monetário
     */
    public static String formatarValor(double valor) {
        return String.format("R$ %.2f", valor);
    }
}
