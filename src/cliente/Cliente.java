package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Cliente em linha de comando: lê o que o usuário digita, envia ao servidor
 * e exibe a resposta recebida.
 *
 * Uso: java -cp out cliente.Cliente [host] [porta]
 */
public class Cliente {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        try (Socket socket = new Socket(host, porta);
             var entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var saida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             var teclado = new Scanner(System.in, StandardCharsets.UTF_8)) {

            exibirResposta(entrada); // mensagem de boas-vindas

            while (true) {
                System.out.print("> ");
                if (!teclado.hasNextLine()) {
                    break;
                }
                String comando = teclado.nextLine();
                saida.println(comando);          // envia a solicitação
                exibirResposta(entrada);          // mostra a resposta
                if (comando.trim().equalsIgnoreCase("SAIR")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Não foi possível comunicar com o servidor: " + e.getMessage());
        }
    }

    /** Lê linhas até encontrar "FIM", que marca o final da resposta. */
    private static void exibirResposta(BufferedReader entrada) throws IOException {
        String linha;
        while ((linha = entrada.readLine()) != null && !linha.equals("FIM")) {
            System.out.println(linha);
        }
    }
}
