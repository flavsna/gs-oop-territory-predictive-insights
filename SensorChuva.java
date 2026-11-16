/** Sensor de chuva (mm/h). Simulado com passeio aleatório para dar tendência às leituras. */
public class SensorChuva implements Sensor {
    private double valorAtual = 8.0;
    private boolean funcionando = true;

    @Override
    public double lerValor() {
        valorAtual += (Math.random() - 0.4) * 12;          // leve tendência de alta
        valorAtual = Math.max(0, Math.min(150, valorAtual)); // faixa realista: 0 a 150
        return Math.round(valorAtual * 10) / 10.0;
    }

    @Override
    public boolean verificarFuncionamento() { return funcionando; }

    public void setFuncionando(boolean funcionando) { this.funcionando = funcionando; }

    @Override
    public String getTipo() { return "Chuva"; }

    @Override
    public String getUnidade() { return "mm/h"; }
}
