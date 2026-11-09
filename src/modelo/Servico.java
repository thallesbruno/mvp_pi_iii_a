package modelo;

/**
 * Um item de informação que a instituição disponibiliza para consulta.
 * Neste exemplo: um serviço/atividade de uma ONG fictícia.
 * Adapte os campos para a realidade da SUA instituição.
 */
public record Servico(int id, String categoria, String nome, String horario, String descricao) {

    /** Linha resumida, usada nas listagens. */
    public String resumo() {
        return id + " | " + categoria + " | " + nome;
    }

    /** Todos os detalhes do item, usada no comando DETALHAR. */
    public String detalhes() {
        return "Nome: " + nome + "\n"
             + "Categoria: " + categoria + "\n"
             + "Horário: " + horario + "\n"
             + "Descrição: " + descricao;
    }

    /** Indica se o termo aparece em algum campo textual (sem diferenciar maiúsculas). */
    public boolean contem(String termo) {
        String t = termo.toLowerCase();
        return nome.toLowerCase().contains(t)
            || categoria.toLowerCase().contains(t)
            || descricao.toLowerCase().contains(t);
    }
}
