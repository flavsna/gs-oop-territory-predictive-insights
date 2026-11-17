/** Níveis de alerta, do mais leve ao mais grave. */
public enum NivelAlerta {
    NORMAL, ATENCAO, ALERTA, CRITICO;

    public String rotulo() {
        switch (this) {
            case ATENCAO: return "ATENÇÃO";
            case ALERTA:  return "ALERTA";
            case CRITICO: return "CRÍTICO";
            default:      return "NORMAL";
        }
    }
}
