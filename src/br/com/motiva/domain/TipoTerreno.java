package br.com.motiva.domain;

public enum TipoTerreno {
    UMIDO("Margem úmida", "Alta umidade acelera o crescimento"),
    SECO("Planície seca", "Crescimento moderado e previsível"),
    ROCHOSO("Encosta rochosa", "Crescimento lento, acesso difícil");

    private final String descricao;
    private final String detalhe;

    TipoTerreno(String descricao, String detalhe) {
        this.descricao = descricao;
        this.detalhe = detalhe;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDetalhe() {
        return detalhe;
    }
}
