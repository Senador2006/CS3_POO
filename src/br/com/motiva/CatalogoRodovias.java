package br.com.motiva;

import br.com.motiva.domain.TipoTerreno;
import br.com.motiva.domain.TrechoRodovia;
import br.com.motiva.domain.TrechoRodoviaMonitorado;

public final class CatalogoRodovias {

    private CatalogoRodovias() {
    }

    public static TrechoRodovia[] criarCenarioDemonstracao() {
        return new TrechoRodovia[] {
            new TrechoRodovia("BR-101", 412, 0.85, TipoTerreno.SECO, false, false, 22),
            new TrechoRodovia("BR-101", 413, 1.05, TipoTerreno.UMIDO, false, false, 38),
            new TrechoRodovia("BR-101", 414, 0.95, TipoTerreno.UMIDO, true, false, 41),
            new TrechoRodovia("BR-116", 78, 1.15, TipoTerreno.ROCHOSO, true, false, 52),
            new TrechoRodoviaMonitorado("BR-116", 79, 0.70, TipoTerreno.SECO, false, true, 60, 1.28),
            new TrechoRodovia("BR-381", 505, 0.60, TipoTerreno.SECO, false, true, 15),
            new TrechoRodoviaMonitorado("BR-381", 506, 1.00, TipoTerreno.UMIDO, false, false, 33, 1.18),
            new TrechoRodovia("BR-381", 507, 1.35, TipoTerreno.SECO, false, false, 8)
        };
    }
}
