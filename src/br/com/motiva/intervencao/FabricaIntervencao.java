package br.com.motiva.intervencao;

import br.com.motiva.relatorio.TipoRecomendacao;

public final class FabricaIntervencao {

    private FabricaIntervencao() {
    }

    public static IntervencaoOperacional criar(TipoRecomendacao tipo) {
        return switch (tipo) {
            case ROCADA_MECANIZADA -> new RocadaMecanizada();
            case ROCADA_MANUAL -> new RocadaManual();
            case PULVERIZACAO -> new Pulverizacao();
        };
    }
}
