/**
 * CLASSE ABSTRATA: molde de tudo que é instalado no território.
 * Não pode ser instanciada (new ComponenteTerritorial(...) não compila).
 */
public abstract class ComponenteTerritorial {
    private final String id;
    private String nome;
    private String regiao;
    private boolean ativo;

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.ativo = false;
    }

    // Métodos concretos: iguais para todas as subclasses
    public void ativar()    { this.ativo = true; }
    public void desativar() { this.ativo = false; }
    public boolean isAtivo() { return ativo; }

    public String getId()     { return id; }
    public String getNome()   { return nome; }
    public String getRegiao() { return regiao; }

    // Método abstrato: cada tipo de componente DEVE implementar do seu jeito
    public abstract String verificarStatus();
}
