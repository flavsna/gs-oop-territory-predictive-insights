/** Sensor de umidade do solo/ar (%). */
public class SensorUmidade implements Sensor {
    private double valorAtual = 60.0;
    private boolean funcionando = true;

    @Override
    public double lerValor() {
        valorAtual += (Math.random() - 0.4) * 8;
        valorAtual = Math.max(30, Math.min(100, valorAtual));
        return Math.round(valorAtual * 10) / 10.0;
    }

    @Override
    public boolean verificarFuncionamento() { return funcionando; }

    public void setFuncionando(boolean funcionando) { this.funcionando = funcionando; }

    @Override
    public String getTipo() { return "Umidade"; }

    @Override
    public String getUnidade() { return "%"; }
}
