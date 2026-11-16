/** Sensor de nível do rio/córrego (metros). */
public class SensorNivelRio implements Sensor {
    private double valorAtual = 1.5;
    private boolean funcionando = true;

    @Override
    public double lerValor() {
        valorAtual += (Math.random() - 0.42) * 0.6;
        valorAtual = Math.max(0, Math.min(10, valorAtual));
        return Math.round(valorAtual * 100) / 100.0;
    }

    @Override
    public boolean verificarFuncionamento() { return funcionando; }

    public void setFuncionando(boolean funcionando) { this.funcionando = funcionando; }

    @Override
    public String getTipo() { return "Nivel do Rio"; }

    @Override
    public String getUnidade() { return "m"; }
}
