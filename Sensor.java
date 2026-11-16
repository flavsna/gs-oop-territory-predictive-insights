/** INTERFACE: contrato que todo sensor deve cumprir. Só declara, não implementa. */
public interface Sensor {
    double lerValor();                  // gera/retorna uma nova leitura
    boolean verificarFuncionamento();   // o sensor está operando?
    String getTipo();                   // ex.: "Chuva"
    String getUnidade();                // ex.: "mm/h"
}
