package br.com.motiva.relatorio;

public enum TipoRecomendacao {
    ROCADA_MECANIZADA("Roçada Mecanizada"),
    ROCADA_MANUAL("Roçada Manual"),
    PULVERIZACAO("Pulverização");

    private final String descricao;

    TipoRecomendacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
