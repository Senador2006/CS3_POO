package br.com.motiva.domain;

public class TrechoRodovia {

    public static final double LIMITE_ALTURA_CRITICA = 1.20;
    public static final double LIMITE_VISUAL = 2.00;

    private final String rodovia;
    private final int km;
    private double alturaVegetacao;
    private final TipoTerreno tipoTerreno;
    private final ComportamentoCrescimento comportamentoCrescimento;
    private final boolean acessoDificil;
    private final boolean infestacaoInvasora;
    private int diasSemManutencao;

    public TrechoRodovia(
            int km,
            double alturaVegetacao,
            ComportamentoCrescimento comportamentoCrescimento,
            boolean acessoDificil,
            boolean infestacaoInvasora) {
        this("BR-000", km, alturaVegetacao, TipoTerreno.SECO, comportamentoCrescimento, acessoDificil, infestacaoInvasora, 0);
    }

    public TrechoRodovia(
            String rodovia,
            int km,
            double alturaVegetacao,
            TipoTerreno tipoTerreno,
            boolean acessoDificil,
            boolean infestacaoInvasora,
            int diasSemManutencao) {
        this(
                rodovia,
                km,
                alturaVegetacao,
                tipoTerreno,
                FabricaCrescimento.criar(tipoTerreno),
                acessoDificil,
                infestacaoInvasora,
                diasSemManutencao);
    }

    public TrechoRodovia(
            String rodovia,
            int km,
            double alturaVegetacao,
            TipoTerreno tipoTerreno,
            ComportamentoCrescimento comportamentoCrescimento,
            boolean acessoDificil,
            boolean infestacaoInvasora,
            int diasSemManutencao) {
        this.rodovia = rodovia;
        this.km = km;
        this.alturaVegetacao = alturaVegetacao;
        this.tipoTerreno = tipoTerreno;
        this.comportamentoCrescimento = comportamentoCrescimento;
        this.acessoDificil = acessoDificil;
        this.infestacaoInvasora = infestacaoInvasora;
        this.diasSemManutencao = diasSemManutencao;
    }

    public String getRodovia() {
        return rodovia;
    }

    public int getKm() {
        return km;
    }

    public double getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public ComportamentoCrescimento getComportamentoCrescimento() {
        return comportamentoCrescimento;
    }

    public boolean isAcessoDificil() {
        return acessoDificil;
    }

    public boolean isInfestacaoInvasora() {
        return infestacaoInvasora;
    }

    public int getDiasSemManutencao() {
        return diasSemManutencao;
    }

    public boolean possuiMonitoramentoIot() {
        return false;
    }

    public void registrarAltura(double novaAltura) {
        this.alturaVegetacao = Math.max(0, novaAltura);
    }

    public void simularCrescimento() {
        comportamentoCrescimento.aplicar(this);
        diasSemManutencao++;
    }

    public void avancarDiaSemCrescimento() {
        diasSemManutencao++;
    }

    public void aplicarIntervencao(double novaAltura) {
        registrarAltura(novaAltura);
        diasSemManutencao = 0;
    }

    public boolean requerIntervencao() {
        return alturaVegetacao >= LIMITE_ALTURA_CRITICA;
    }

    public double getLimiteAlturaCritica() {
        return LIMITE_ALTURA_CRITICA;
    }

    public double getPercentualLimite() {
        return Math.min(100, (alturaVegetacao / LIMITE_ALTURA_CRITICA) * 100);
    }

    public String gerarBarraVegetacao(int largura) {
        int preenchido = (int) Math.round((alturaVegetacao / LIMITE_VISUAL) * largura);
        preenchido = Math.max(0, Math.min(largura, preenchido));

        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < largura; i++) {
            if (i < preenchido) {
                barra.append(requerIntervencao() ? '#' : '=');
            } else {
                barra.append('.');
            }
        }
        barra.append(']');
        return barra.toString();
    }

    public String getIdentificador() {
        return rodovia + " KM " + km;
    }
}
