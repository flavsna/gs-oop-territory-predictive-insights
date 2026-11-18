import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ENCAPSULAMENTO: todos os atributos são private. O único jeito de mudar algo
 * é por métodos que VALIDAM. Alteração inválida é rejeitada e o valor anterior é mantido.
 * Os limiares de alerta só mudam com o código de acesso correto.
 */
public class DadosTerritorio {
    private String nomeTerritorio;
    private String regiao;
    private int populacao;
    private String codigoAcesso; // dado sensível: nunca há getter para ele
    private final Map<String, Limiar> limiares = new LinkedHashMap<>();

    /** Classe interna privada: guarda os 3 limiares e a faixa física válida de um sensor. */
    private static class Limiar {
        double atencao, alerta, critico;
        final double min, max;
        Limiar(double min, double max, double atencao, double alerta, double critico) {
            this.min = min; this.max = max;
            this.atencao = atencao; this.alerta = alerta; this.critico = critico;
        }
    }

    public DadosTerritorio(String nomeTerritorio, String regiao, int populacao, String codigoAcesso) {
        this.nomeTerritorio = nomeTerritorio;
        this.regiao = regiao;
        this.populacao = populacao;
        this.codigoAcesso = codigoAcesso;
        // limiares padrão (min, max, atenção, alerta, crítico)
        limiares.put("Chuva",        new Limiar(0, 150, 20, 40, 60));
        limiares.put("Nivel do Rio", new Limiar(0, 10, 2.5, 3.5, 4.5));
        limiares.put("Umidade",      new Limiar(0, 100, 70, 85, 95));
    }

    // ---------- getters ----------
    public String getNomeTerritorio() { return nomeTerritorio; }
    public String getRegiao()         { return regiao; }
    public int getPopulacao()         { return populacao; }

    public double getLimiarAtencao(String tipo) { return limiares.get(tipo).atencao; }
    public double getLimiarAlerta(String tipo)  { return limiares.get(tipo).alerta; }
    public double getLimiarCritico(String tipo) { return limiares.get(tipo).critico; }

    // ---------- setters com validação (retornam true se aceitou) ----------
    public boolean setRegiao(String novaRegiao) {
        if (novaRegiao == null || novaRegiao.trim().length() < 3) {
            System.out.println("[REJEITADO] Região deve ter pelo menos 3 caracteres. Mantido: " + regiao);
            return false;
        }
        this.regiao = novaRegiao.trim();
        return true;
    }

    public boolean setPopulacao(int novaPopulacao) {
        if (novaPopulacao <= 0 || novaPopulacao > 10_000_000) {
            System.out.println("[REJEITADO] População deve estar entre 1 e 10.000.000. Mantido: " + populacao);
            return false;
        }
        this.populacao = novaPopulacao;
        return true;
    }

    /** Só altera os limiares se o código estiver certo e os valores forem coerentes. */
    public boolean definirLimiares(String codigo, String tipo, double atencao, double alerta, double critico) {
        if (!codigoCorreto(codigo)) {
            System.out.println("[REJEITADO] Código de acesso incorreto. Limiares mantidos.");
            return false;
        }
        Limiar l = limiares.get(tipo);
        if (l == null) {
            System.out.println("[REJEITADO] Tipo de sensor desconhecido: " + tipo);
            return false;
        }
        boolean foraDaFaixa = atencao < l.min || critico > l.max;
        boolean foraDeOrdem = !(atencao < alerta && alerta < critico);
        if (foraDaFaixa || foraDeOrdem) {
            System.out.println("[REJEITADO] Limiares inválidos. Use atenção < alerta < crítico, dentro de "
                    + l.min + " a " + l.max + ". Mantido: "
                    + l.atencao + " / " + l.alerta + " / " + l.critico);
            return false;
        }
        l.atencao = atencao; l.alerta = alerta; l.critico = critico;
        return true;
    }

    public boolean alterarCodigoAcesso(String codigoAtual, String novoCodigo) {
        if (!codigoCorreto(codigoAtual)) {
            System.out.println("[REJEITADO] Código atual incorreto.");
            return false;
        }
        if (novoCodigo == null || novoCodigo.length() < 4) {
            System.out.println("[REJEITADO] O novo código precisa ter pelo menos 4 caracteres.");
            return false;
        }
        this.codigoAcesso = novoCodigo;
        return true;
    }

    private boolean codigoCorreto(String codigo) {
        return codigoAcesso.equals(codigo);
    }
}
