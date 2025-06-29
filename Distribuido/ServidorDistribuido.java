package Distribuido;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorDistribuido {

    private static final char[] caracteres = "0123456789".toCharArray();
    private static final int NUM_CLIENTES = 3;
    private static final int PORTA = 12345;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORTA);
        System.out.println("Servidor iniciado na porta " + PORTA);
        List<Socket> clientes = new ArrayList<>();

        // Aguarda conexão de 3 clientes
        while (clientes.size() < NUM_CLIENTES) {
            Socket cliente = serverSocket.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress());
            clientes.add(cliente);
        }

        // Testar para senhas de 4 a 9 dígitos
        for (int TAM = 4; TAM <= 9; TAM++) {
            String senha = gerarSenhaAleatoria(TAM);
            int total = (int) Math.pow(caracteres.length, TAM);
            int intervalo = total / NUM_CLIENTES;

            System.out.println("\nSenha gerada (" + TAM + " dígitos): " + senha);
            long inicio = System.currentTimeMillis();

            // Envia intervalo para cada cliente
            for (int i = 0; i < NUM_CLIENTES; i++) {
                int ini = i * intervalo;
                int fim = (i == NUM_CLIENTES - 1) ? total : (i + 1) * intervalo;

                DataOutputStream out = new DataOutputStream(clientes.get(i).getOutputStream());
                out.writeInt(ini);
                out.writeInt(fim);
                out.writeInt(TAM);
                out.writeUTF(senha);
            }

            // Espera resposta de algum cliente
            String resultado = null;
            for (Socket cliente : clientes) {
                DataInputStream in = new DataInputStream(cliente.getInputStream());
                String resposta = in.readUTF();
                if (!resposta.equals("NAO")) {
                    resultado = resposta;
                    break;
                }
            }

            long fim = System.currentTimeMillis();

            if (resultado != null) {
                System.out.println("Senha encontrada: " + resultado);
                System.out.println("Tempo: " + (fim - inicio) + " ms");
            } else {
                System.out.println("Senha não encontrada.");
            }
        }

        serverSocket.close();
    }

    public static String gerarSenhaAleatoria(int TAM) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAM; i++) {
            sb.append(caracteres[random.nextInt(caracteres.length)]);
        }
        return sb.toString();
    }
}

