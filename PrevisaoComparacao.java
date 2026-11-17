import java.util.List;

/**
 * Compara o período atual (últimas N leituras) com o anterior (N leituras antes)
 * e projeta a tendência: previsto = atual + (atual - anterior).
 */
public class PrevisaoComparacao extends EstrategiaPrevisao {
    private final int tamanhoPeriodo;
    private double ultimaVariacao; // atributo próprio: guarda a variação da última previsão

    public PrevisaoComparacao(int tamanhoPeriodo) {
        super("Comparação de Períodos", tamanhoPeriodo * 2); // precisa de 2 períodos completos
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    public int getTamanhoPeriodo() { return tamanhoPeriodo; }

    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        double atual = media(leituras, fim - tamanhoPeriodo, fim);
        double anterior = media(leituras, fim - 2 * tamanhoPeriodo, fim - tamanhoPeriodo);
        ultimaVariacao = atual - anterior;
        return Math.max(0, atual + ultimaVariacao);
    }

    @Override
    public String explicar(double previsto, String unidade) {
        String tendencia = Math.abs(ultimaVariacao) < 0.01 ? "estável"
                : (ultimaVariacao > 0 ? "em ALTA" : "em QUEDA");
        return super.explicar(previsto, unidade)
                + " -> período atual vs anterior (" + tamanhoPeriodo + " leituras cada): tendência "
                + tendencia + " (variação " + String.format("%+.2f", ultimaVariacao) + " " + unidade + ").";
    }
}
