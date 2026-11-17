import java.util.List;

/** Previsão = média das últimas N leituras. Atributo próprio: tamanho da janela. */
public class PrevisaoMediaMovel extends EstrategiaPrevisao {
    private final int janela;

    public PrevisaoMediaMovel(int janela) {
        super("Média Móvel", janela); // precisa de pelo menos N leituras
        this.janela = janela;
    }

    public int getJanela() { return janela; }

    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        return media(leituras, fim - janela, fim);
    }

    @Override
    public String explicar(double previsto, String unidade) {
        return super.explicar(previsto, unidade)
                + " -> média das últimas " + janela + " leituras (nível recente esperado).";
    }
}
