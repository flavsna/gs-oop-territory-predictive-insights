import java.util.List;

/** HERANÇA: classe base de todas as formas de prever. */
public abstract class EstrategiaPrevisao {
    private final String nome;
    private final int minimoLeituras; // abaixo disso o sistema "se cala"

    public EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }

    public String getNome()           { return nome; }
    public int getMinimoLeituras()    { return minimoLeituras; }

    public boolean podePrever(List<Double> leituras) {
        return leituras != null && leituras.size() >= minimoLeituras;
    }

    /** Cada estratégia calcula do seu jeito. */
    public abstract double prever(List<Double> leituras);

    /** Texto base explicando o resultado; as subclasses complementam usando super. */
    public String explicar(double previsto, String unidade) {
        return "[" + nome + "] valor previsto: " + String.format("%.2f", previsto) + " " + unidade;
    }

    /** Método comum reaproveitado pelas subclasses. */
    protected double media(List<Double> leituras, int inicio, int fim) {
        double soma = 0;
        for (int i = inicio; i < fim; i++) soma += leituras.get(i);
        return soma / (fim - inicio);
    }
}
