import java.io.*;
import java.util.*;

/**
 * Classe principal: menu interativo, previsões e alertas.
 * Bônus: o histórico de leituras é salvo em historico.csv ao sair e recarregado ao iniciar.
 */
public class SistemaMonitoramento {
    private static final int MAX_HISTORICO = 50;
    private static final String ARQUIVO = "historico.csv";

    private final Scanner entrada = new Scanner(System.in);
    private final DadosTerritorio dados =
            new DadosTerritorio("Comunidade Córrego Norte", "Zona Leste", 12000, "1234");
    private final EstacaoDeColeta estacao =
            new EstacaoDeColeta("EST-01", "Estação Córrego Norte", "Zona Leste");
    private final List<Sensor> sensores = new ArrayList<>();
    private final Map<String, ArrayList<Double>> historico = new LinkedHashMap<>();
    private final List<EstrategiaPrevisao> estrategias = new ArrayList<>();

    public SistemaMonitoramento() {
        sensores.add(new SensorChuva());
        sensores.add(new SensorNivelRio());
        sensores.add(new SensorUmidade());
        for (Sensor s : sensores) {
            estacao.adicionarSensor(s);
            historico.put(s.getTipo(), new ArrayList<>());
        }
        estacao.ativar();
        estrategias.add(new PrevisaoMediaMovel(5));
        estrategias.add(new PrevisaoComparacao(3));
        carregarHistorico();
    }

    public static void main(String[] args) {
        new SistemaMonitoramento().executar();
    }

    // ------------------------------------------------------------ menu principal
    private void executar() {
        System.out.println("=== Territory Predictive Insights (CitizenTech) ===");
        boolean rodando = true;
        while (rodando) {
            System.out.println("\n--- MENU ---");
            System.out.println("1) Verificar sensores (nova leitura)");
            System.out.println("2) Rodar previsões e alertas");
            System.out.println("3) Gerenciar dados do território");
            System.out.println("4) Simular próximas leituras");
            System.out.println("5) Exibir status completo");
            System.out.println("0) Sair");
            System.out.print("Opção: ");
            String opcao = entrada.hasNextLine() ? entrada.nextLine().trim() : "0";
            switch (opcao) {
                case "1": verificarSensores(); break;
                case "2": rodarPrevisoes(); break;
                case "3": menuDados(); break;
                case "4": simularLeituras(); break;
                case "5": exibirStatus(); break;
                case "0": rodando = false; break;
                default:  System.out.println("Opção inválida.");
            }
        }
        salvarHistorico();
        System.out.println("Histórico salvo em " + ARQUIVO + ". Até logo!");
    }

    // ------------------------------------------------------------ 1) sensores
    private void verificarSensores() {
        System.out.println("\n" + estacao.verificarStatus());
        for (Sensor s : sensores) {
            if (!s.verificarFuncionamento()) {
                System.out.println("- " + s.getTipo() + ": FORA DE OPERAÇÃO (leitura ignorada)");
                continue;
            }
            double valor = registrarLeitura(s);
            NivelAlerta nivel = classificar(s.getTipo(), valor);
            System.out.println("- " + s.getTipo() + ": " + valor + " " + s.getUnidade()
                    + " [" + nivel.rotulo() + "]");
            if (nivel != NivelAlerta.NORMAL) {
                emitirAlerta(s, nivel, valor, "leitura atual");
            }
        }
    }

    private double registrarLeitura(Sensor s) {
        double valor = s.lerValor();
        ArrayList<Double> lista = historico.get(s.getTipo());
        lista.add(valor);
        if (lista.size() > MAX_HISTORICO) lista.remove(0); // mantém só as mais recentes
        return valor;
    }

    // ------------------------------------------------------------ 2) previsões
    private void rodarPrevisoes() {
        System.out.println("\n=== PREVISÕES ===");
        int alertas = 0;
        for (Sensor s : sensores) {
            ArrayList<Double> leituras = historico.get(s.getTipo());
            System.out.println("\nSensor: " + s.getTipo() + " (" + leituras.size() + " leituras guardadas)");
            for (EstrategiaPrevisao e : estrategias) {
                if (!e.podePrever(leituras)) {
                    // o sistema se cala: poucas leituras = previsão pouco confiável
                    System.out.println("  [" + e.getNome() + "] sem previsão: "
                            + leituras.size() + " leituras, mínimo " + e.getMinimoLeituras()
                            + ". Colete mais dados (opção 1 ou 4).");
                    continue;
                }
                double previsto = e.prever(leituras);
                System.out.println("  " + e.explicar(previsto, s.getUnidade()));
                NivelAlerta nivel = classificar(s.getTipo(), previsto);
                if (nivel != NivelAlerta.NORMAL) {
                    emitirAlerta(s, nivel, previsto, e.getNome());
                    alertas++;
                }
            }
        }
        System.out.println("\nTotal de alertas gerados: " + alertas);
    }

    // ------------------------------------------------------------ alertas
    private NivelAlerta classificar(String tipo, double valor) {
        if (valor >= dados.getLimiarCritico(tipo)) return NivelAlerta.CRITICO;
        if (valor >= dados.getLimiarAlerta(tipo))  return NivelAlerta.ALERTA;
        if (valor >= dados.getLimiarAtencao(tipo)) return NivelAlerta.ATENCAO;
        return NivelAlerta.NORMAL;
    }

    private double limiteEstourado(String tipo, NivelAlerta nivel) {
        switch (nivel) {
            case CRITICO: return dados.getLimiarCritico(tipo);
            case ALERTA:  return dados.getLimiarAlerta(tipo);
            default:      return dados.getLimiarAtencao(tipo);
        }
    }

    /** Explica o porquê: qual sensor, valor, limite estourado e a base (estratégia). */
    private void emitirAlerta(Sensor s, NivelAlerta nivel, double valor, String origem) {
        double limite = limiteEstourado(s.getTipo(), nivel);
        System.out.println("  **********************************************");
        System.out.println("  *** " + nivel.rotulo() + " ***  " + dados.getNomeTerritorio()
                + " (" + dados.getRegiao() + ")");
        System.out.println("  * Sensor: " + s.getTipo());
        System.out.println("  * Valor: " + String.format("%.2f", valor) + " " + s.getUnidade()
                + " (base: " + origem + ")");
        System.out.println("  * Limite estourado: " + limite + " " + s.getUnidade()
                + " (nível " + nivel.rotulo() + ")");
        System.out.println("  * Por quê: o valor ficou igual ou acima do limite configurado para este nível.");
        System.out.println("  * Este aviso APOIA a decisão: confirme em campo antes de agir"
                + " (ex.: avisar áreas baixas, acionar defesa civil).");
        System.out.println("  **********************************************");
    }

    // ------------------------------------------------------------ 3) dados do território
    private void menuDados() {
        System.out.println("\n--- DADOS DO TERRITÓRIO ---");
        System.out.println("1) Ver dados e limiares");
        System.out.println("2) Alterar população");
        System.out.println("3) Alterar região");
        System.out.println("4) Alterar limiares (exige código)");
        System.out.println("5) Alterar código de acesso");
        System.out.print("Opção: ");
        String op = entrada.nextLine().trim();
        switch (op) {
            case "1": mostrarDados(); break;
            case "2": {
                Double v = lerNumero("Nova população: ");
                if (v == null) break;
                if (v != Math.floor(v)) { System.out.println("[REJEITADO] População deve ser inteira."); break; }
                if (dados.setPopulacao(v.intValue())) System.out.println("População atualizada.");
                break;
            }
            case "3":
                System.out.print("Nova região: ");
                if (dados.setRegiao(entrada.nextLine())) System.out.println("Região atualizada.");
                break;
            case "4": alterarLimiares(); break;
            case "5":
                System.out.print("Código atual: ");
                String atual = entrada.nextLine();
                System.out.print("Novo código: ");
                String novo = entrada.nextLine();
                if (dados.alterarCodigoAcesso(atual, novo)) System.out.println("Código alterado.");
                break;
            default: System.out.println("Opção inválida.");
        }
    }

    private void alterarLimiares() {
        System.out.print("Código de acesso: ");
        String codigo = entrada.nextLine();
        System.out.println("Sensores: ");
        for (int i = 0; i < sensores.size(); i++) {
            System.out.println((i + 1) + ") " + sensores.get(i).getTipo());
        }
        Double idx = lerNumero("Escolha o sensor: ");
        if (idx == null || idx < 1 || idx > sensores.size()) {
            System.out.println("[REJEITADO] Sensor inexistente.");
            return;
        }
        String tipo = sensores.get(idx.intValue() - 1).getTipo();
        Double a = lerNumero("Limiar ATENÇÃO: ");
        Double b = lerNumero("Limiar ALERTA: ");
        Double c = lerNumero("Limiar CRÍTICO: ");
        if (a == null || b == null || c == null) return;
        if (dados.definirLimiares(codigo, tipo, a, b, c)) {
            System.out.println("Limiares de " + tipo + " atualizados.");
        }
    }

    private void mostrarDados() {
        System.out.println("Território: " + dados.getNomeTerritorio());
        System.out.println("Região: " + dados.getRegiao());
        System.out.println("População: " + dados.getPopulacao());
        for (Sensor s : sensores) {
            String t = s.getTipo();
            System.out.println("Limiares " + t + " (" + s.getUnidade() + "): atenção "
                    + dados.getLimiarAtencao(t) + " | alerta " + dados.getLimiarAlerta(t)
                    + " | crítico " + dados.getLimiarCritico(t));
        }
    }

    // ------------------------------------------------------------ 4) simulação
    private void simularLeituras() {
        Double n = lerNumero("Quantas leituras simular (1 a 30)? ");
        if (n == null || n < 1 || n > 30 || n != Math.floor(n)) {
            System.out.println("[REJEITADO] Informe um inteiro de 1 a 30.");
            return;
        }
        int total = n.intValue();
        for (int i = 1; i <= total; i++) {
            StringBuilder linha = new StringBuilder("Leitura " + i + ": ");
            for (Sensor s : sensores) {
                if (!s.verificarFuncionamento()) continue;
                double v = registrarLeitura(s);
                linha.append(s.getTipo()).append("=").append(v).append(s.getUnidade())
                     .append(" [").append(classificar(s.getTipo(), v).rotulo()).append("]  ");
            }
            System.out.println(linha);
        }
        System.out.println("Simulação concluída. Use a opção 2 para rodar as previsões.");
    }

    // ------------------------------------------------------------ 5) status
    private void exibirStatus() {
        System.out.println("\n=== STATUS COMPLETO ===");
        System.out.println(estacao.verificarStatus());
        mostrarDados();
        for (Sensor s : sensores) {
            ArrayList<Double> l = historico.get(s.getTipo());
            String ultima = l.isEmpty() ? "sem leituras" : l.get(l.size() - 1) + " " + s.getUnidade();
            System.out.println("- " + s.getTipo() + ": funcionando="
                    + s.verificarFuncionamento() + " | leituras=" + l.size() + " | última=" + ultima);
        }
        for (EstrategiaPrevisao e : estrategias) {
            System.out.println("Estratégia: " + e.getNome() + " (mínimo de " + e.getMinimoLeituras() + " leituras)");
        }
    }

    // ------------------------------------------------------------ utilidades
    private Double lerNumero(String mensagem) {
        System.out.print(mensagem);
        String texto = entrada.nextLine().trim().replace(",", ".");
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            System.out.println("[REJEITADO] Entrada não numérica: \"" + texto + "\"");
            return null;
        }
    }

    // ------------------------------------------------------------ bônus: arquivo
    private void salvarHistorico() {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (Map.Entry<String, ArrayList<Double>> e : historico.entrySet()) {
                StringBuilder sb = new StringBuilder(e.getKey());
                for (double v : e.getValue()) sb.append(";").append(v);
                out.println(sb);
            }
        } catch (IOException ex) {
            System.out.println("Não foi possível salvar o histórico: " + ex.getMessage());
        }
    }

    private void carregarHistorico() {
        File f = new File(ARQUIVO);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                ArrayList<Double> lista = historico.get(partes[0]);
                if (lista == null) continue;
                for (int i = 1; i < partes.length; i++) lista.add(Double.parseDouble(partes[i]));
            }
            System.out.println("Histórico anterior carregado de " + ARQUIVO + ".");
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Histórico ignorado (arquivo inválido): " + ex.getMessage());
        }
    }
}
