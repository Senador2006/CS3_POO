package model;

import br.com.motiva.domain.TipoTerreno;
import br.com.motiva.domain.TrechoRodoviaMonitorado;

public record TrechoRodovia(
        Integer id,
        String rodovia,
        int km,
        double alturaVegetacao,
        String tipoTerreno,
        boolean acessoDificil,
        boolean infestacaoInvasora,
        int diasSemManutencao,
        boolean monitoradoIot,
        Double leituraSensor,
        Integer idEquipeResponsavel
) {

    public TrechoRodovia(
            String rodovia,
            int km,
            double alturaVegetacao,
            String tipoTerreno,
            boolean acessoDificil,
            boolean infestacaoInvasora,
            int diasSemManutencao,
            boolean monitoradoIot,
            Double leituraSensor,
            Integer idEquipeResponsavel) {
        this(
                null,
                rodovia,
                km,
                alturaVegetacao,
                tipoTerreno,
                acessoDificil,
                infestacaoInvasora,
                diasSemManutencao,
                monitoradoIot,
                leituraSensor,
                idEquipeResponsavel);
    }

    public TrechoRodovia comId(int novoId) {
        return new TrechoRodovia(
                novoId,
                rodovia,
                km,
                alturaVegetacao,
                tipoTerreno,
                acessoDificil,
                infestacaoInvasora,
                diasSemManutencao,
                monitoradoIot,
                leituraSensor,
                idEquipeResponsavel);
    }

    public TrechoRodovia comAltura(double novaAltura) {
        return new TrechoRodovia(
                id,
                rodovia,
                km,
                novaAltura,
                tipoTerreno,
                acessoDificil,
                infestacaoInvasora,
                diasSemManutencao,
                monitoradoIot,
                leituraSensor,
                idEquipeResponsavel);
    }

    public String getIdentificador() {
        return rodovia + " KM " + km;
    }

    public br.com.motiva.domain.TrechoRodovia paraDominio() {
        TipoTerreno terreno = TipoTerreno.valueOf(tipoTerreno);
        if (monitoradoIot) {
            double sensor = leituraSensor != null ? leituraSensor : alturaVegetacao;
            return new TrechoRodoviaMonitorado(
                    rodovia,
                    km,
                    alturaVegetacao,
                    terreno,
                    acessoDificil,
                    infestacaoInvasora,
                    diasSemManutencao,
                    sensor);
        }
        return new br.com.motiva.domain.TrechoRodovia(
                rodovia,
                km,
                alturaVegetacao,
                terreno,
                acessoDificil,
                infestacaoInvasora,
                diasSemManutencao);
    }

    @Override
    public String toString() {
        return String.format(
                "Trecho #%s | %s | %.2fm | %s | %d dias s/ manutenção%s",
                id == null ? "?" : id,
                getIdentificador(),
                alturaVegetacao,
                tipoTerreno,
                diasSemManutencao,
                monitoradoIot ? " | IoT" : "");
    }
}
