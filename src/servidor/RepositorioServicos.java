package servidor;

import modelo.Servico;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Guarda as informações em memória. Os dados vêm de um arquivo CSV,
 * então NÃO precisamos de banco de dados (conforme a proposta do projeto).
 */
public class RepositorioServicos {

    private final List<Servico> servicos = new ArrayList<>();

    public RepositorioServicos(Path arquivoCsv) throws IOException {
        for (String linha : Files.readAllLines(arquivoCsv, StandardCharsets.UTF_8)) {
            if (linha.isBlank() || linha.startsWith("#")) {
                continue; // ignora linhas vazias e comentários
            }
            String[] c = linha.split(";", 5);
            servicos.add(new Servico(Integer.parseInt(c[0].trim()),
                    c[1].trim(), c[2].trim(), c[3].trim(), c[4].trim()));
        }
    }

    public List<Servico> listarTodos() {
        return List.copyOf(servicos);
    }

    public List<Servico> buscar(String termo) {
        return servicos.stream().filter(s -> s.contem(termo)).toList();
    }

    public Optional<Servico> porId(int id) {
        return servicos.stream().filter(s -> s.id() == id).findFirst();
    }
}
