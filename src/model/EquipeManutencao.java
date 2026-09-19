package model;

public record EquipeManutencao(
        Integer id,
        String nome,
        String especialidade,
        int quantidadeMembros,
        String baseOperacional,
        boolean disponivel
) {

    public EquipeManutencao(
            String nome,
            String especialidade,
            int quantidadeMembros,
            String baseOperacional,
            boolean disponivel) {
        this(null, nome, especialidade, quantidadeMembros, baseOperacional, disponivel);
    }

    public EquipeManutencao comId(int novoId) {
        return new EquipeManutencao(novoId, nome, especialidade, quantidadeMembros, baseOperacional, disponivel);
    }

    public EquipeManutencao comDisponibilidade(boolean novaDisponibilidade) {
        return new EquipeManutencao(id, nome, especialidade, quantidadeMembros, baseOperacional, novaDisponibilidade);
    }

    @Override
    public String toString() {
        return String.format(
                "Equipe #%s | %s | %s | %d membros | Base: %s | %s",
                id == null ? "?" : id,
                nome,
                especialidade,
                quantidadeMembros,
                baseOperacional,
                disponivel ? "Disponível" : "Ocupada");
    }
}
