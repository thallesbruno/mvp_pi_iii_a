package servidor;

import modelo.Servico;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementa o PROTOCOLO da aplicação: recebe uma linha de texto do cliente
 * e devolve a lista de linhas da resposta. Não conhece sockets, então pode
 * ser testado separadamente.
 *
 * Formato da resposta:  "OK" ou "ERRO <motivo>", seguido das linhas de dados.
 * O servidor acrescenta a linha "FIM" ao final de cada resposta.
 */
public class ProcessadorComandos {

    public static final String AJUDA =
            "Comandos: LISTAR | BUSCAR <termo> | DETALHAR <id> | AJUDA | SAIR";

    private final RepositorioServicos repositorio;

    public ProcessadorComandos(RepositorioServicos repositorio) {
        this.repositorio = repositorio;
    }

    public List<String> processar(String linha) {
        List<String> resposta = new ArrayList<>();
        if (linha == null || linha.isBlank()) {
            resposta.add("ERRO comando vazio. " + AJUDA);
            return resposta;
        }

        String[] partes = linha.trim().split("\\s+", 2);
        String comando = partes[0].toUpperCase();
        String argumento = partes.length > 1 ? partes[1].trim() : "";

        switch (comando) {
            case "LISTAR" -> {
                resposta.add("OK");
                repositorio.listarTodos().forEach(s -> resposta.add(s.resumo()));
            }
            case "BUSCAR" -> {
                if (argumento.isEmpty()) {
                    resposta.add("ERRO informe um termo. Ex.: BUSCAR saude");
                } else {
                    List<Servico> achados = repositorio.buscar(argumento);
                    resposta.add("OK");
                    achados.forEach(s -> resposta.add(s.resumo()));
                    if (achados.isEmpty()) {
                        resposta.add("Nenhum resultado para: " + argumento);
                    }
                }
            }
            case "DETALHAR" -> {
                try {
                    int id = Integer.parseInt(argumento);
                    var encontrado = repositorio.porId(id);
                    if (encontrado.isPresent()) {
                        resposta.add("OK");
                        resposta.addAll(List.of(encontrado.get().detalhes().split("\n")));
                    } else {
                        resposta.add("ERRO id inexistente: " + id);
                    }
                } catch (NumberFormatException e) {
                    resposta.add("ERRO o id deve ser um número. Ex.: DETALHAR 2");
                }
            }
            case "AJUDA" -> {
                resposta.add("OK");
                resposta.add(AJUDA);
            }
            default -> resposta.add("ERRO comando desconhecido: " + comando + ". " + AJUDA);
        }
        return resposta;
    }
}
