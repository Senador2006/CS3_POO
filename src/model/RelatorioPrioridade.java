package model;

import java.time.LocalDateTime;

public record RelatorioPrioridade(
        Integer id,
        LocalDateTime dataGeracao,
        int qtUrgente,
        int qtCritico,
        int qtAtencao,
        int qtNormal,
        String resumo
) {

    public RelatorioPrioridade(
            int qtUrgente,
            int qtCritico,
            int qtAtencao,
            int qtNormal,
            String resumo) {
        this(null, null, qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);
    }

    public RelatorioPrioridade comId(int novoId, LocalDateTime geradoEm) {
        return new RelatorioPrioridade(novoId, geradoEm, qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);
    }

    public int getTotalTrechos() {
        return qtUrgente + qtCritico + qtAtencao + qtNormal;
    }

    @Override
    public String toString() {
        return String.format(
                "Relatório #%s | %s | Urgente=%d Crítico=%d Atenção=%d Normal=%d | %s",
                id == null ? "?" : id,
                dataGeracao == null ? "agora" : dataGeracao,
                qtUrgente,
                qtCritico,
                qtAtencao,
                qtNormal,
                resumo);
    }
}
