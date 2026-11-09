package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * Servidor TCP: aguarda conexões e atende cada cliente em uma thread própria.
 *
 * Uso: java -cp out servidor.Servidor [porta] [arquivo.csv]
 */
public class Servidor {

    public static final int PORTA_PADRAO = 5000;

    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : PORTA_PADRAO;
        Path arquivo = Path.of(args.length > 1 ? args[1] : "dados/servicos.csv");

        var processador = new ProcessadorComandos(new RepositorioServicos(arquivo));

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor aguardando conexões na porta " + porta + "...");
            while (true) {
                Socket cliente = servidor.accept(); // bloqueia até alguém conectar
                new Thread(() -> atender(cliente, processador)).start();
            }
        }
    }

    private static void atender(Socket socket, ProcessadorComandos processador) {
        String origem = socket.getRemoteSocketAddress().toString();
        System.out.println("[+] Cliente conectado: " + origem);

        try (socket;
             var entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var saida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            saida.println("OK Bem-vindo! Digite AJUDA para ver os comandos.");
            saida.println("FIM");

            String linha;
            while ((linha = entrada.readLine()) != null) {
                if (linha.trim().equalsIgnoreCase("SAIR")) {
                    saida.println("OK Até logo!");
                    saida.println("FIM");
                    break;
                }
                System.out.println("[" + origem + "] " + linha);
                processador.processar(linha).forEach(saida::println);
                saida.println("FIM");
            }
        } catch (IOException e) {
            System.out.println("[!] Erro com " + origem + ": " + e.getMessage());
        }
        System.out.println("[-] Cliente desconectado: " + origem);
    }
}
