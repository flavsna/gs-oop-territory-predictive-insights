import java.util.ArrayList;
import java.util.List;

/** Classe concreta: herda de ComponenteTerritorial (extends) e agrupa sensores. */
public class EstacaoDeColeta extends ComponenteTerritorial {
    private final List<Sensor> sensores = new ArrayList<>();

    public EstacaoDeColeta(String id, String nome, String regiao) {
        super(id, nome, regiao); // chama o construtor da classe mãe
    }

    public void adicionarSensor(Sensor sensor) { sensores.add(sensor); }

    public List<Sensor> getSensores() { return sensores; }

    @Override
    public String verificarStatus() {
        int ok = 0;
        for (Sensor s : sensores) {
            if (s.verificarFuncionamento()) ok++;
        }
        return getNome() + " (" + getRegiao() + "): "
                + (isAtivo() ? "OPERANDO" : "DESATIVADA")
                + " | sensores funcionando: " + ok + "/" + sensores.size();
    }
}
