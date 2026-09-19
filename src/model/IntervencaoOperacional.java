package model;

import java.time.LocalDate;

public record IntervencaoOperacional(
        Integer id,
        int idTrecho,
        int idEquipe,
        String tipo,
        String status,
        LocalDate dataAgendada,
        Double alturaAntes,
        Double alturaDepois,
        String observacao
) {

    public IntervencaoOperacional(
            int idTrecho,
            int idEquipe,
            String tipo,
            String status,
            LocalDate dataAgendada,
            Double alturaAntes,
            Double alturaDepois,
            String observacao) {
        this(null, idTrecho, idEquipe, tipo, status, dataAgendada, alturaAntes, alturaDepois, observacao);
    }

    public IntervencaoOperacional comId(int novoId) {
        return new IntervencaoOperacional(
                novoId, idTrecho, idEquipe, tipo, status, dataAgendada, alturaAntes, alturaDepois, observacao);
    }

    public IntervencaoOperacional comStatus(String novoStatus, Double novaAlturaDepois) {
        return new IntervencaoOperacional(
                id, idTrecho, idEquipe, tipo, novoStatus, dataAgendada, alturaAntes, novaAlturaDepois, observacao);
    }

    @Override
    public String toString() {
        return String.format(
                "Intervenção #%s | Trecho %d | Equipe %d | %s | %s | %s",
                id == null ? "?" : id,
                idTrecho,
                idEquipe,
                tipo,
                status,
                dataAgendada);
    }
}
