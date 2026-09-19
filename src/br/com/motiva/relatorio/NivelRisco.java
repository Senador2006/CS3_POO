package br.com.motiva.relatorio;

public enum NivelRisco {
    CRITICO("CRÍTICO", "Intervenção imediata"),
    ALTO("ALTO", "Agendar equipe em até 48h"),
    MODERADO("MODERADO", "Monitorar de perto");

    private final String rotulo;
    private final String orientacao;

    NivelRisco(String rotulo, String orientacao) {
        this.rotulo = rotulo;
        this.orientacao = orientacao;
    }

    public String getRotulo() {
        return rotulo;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public static NivelRisco aPartirDaPrioridade(int prioridade) {
        if (prioridade >= 60) {
            return CRITICO;
        }
        if (prioridade >= 35) {
            return ALTO;
        }
        return MODERADO;
    }
}
