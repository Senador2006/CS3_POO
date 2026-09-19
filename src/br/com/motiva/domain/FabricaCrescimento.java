package br.com.motiva.domain;

public final class FabricaCrescimento {

    private FabricaCrescimento() {
    }

    public static ComportamentoCrescimento criar(TipoTerreno tipoTerreno) {
        return switch (tipoTerreno) {
            case UMIDO -> new CrescimentoUmido();
            case SECO -> new CrescimentoSeco();
            case ROCHOSO -> new CrescimentoRochoso();
        };
    }
}
