package Distribuido;

import java.io.*;
import java.net.*;

public class ClienteDistribuido {

    private static final char[] caracteres = "0123456789".toCharArray();

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        System.out.println("Conectado ao servidor.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (true) {
            try {
                int ini = in.readInt();
                int fim = in.readInt();
                int TAM = in.readInt();
                String senha = in.readUTF();

                String tentativa = quebrarSenha(ini, fim, TAM, senha);
                if (tentativa != null) {
                    out.writeUTF(tentativa);
                } else {
                    out.writeUTF("NAO");
                }

            } catch (EOFException e) {
                break;
            }
        }

        socket.close();
    }

    public static String quebrarSenha(int ini, int fim, int TAM, String senha) {
        for (int i = ini; i < fim; i++) {
            String tentativa = gerarTentativa(i, TAM);
            if (tentativa.equals(senha)) {
                return tentativa;
            }
        }
        return null;
    }

    public static String gerarTentativa(int num, int TAM) {
        StringBuilder sb = new StringBuilder();
        int base = caracteres.length;

        for (int i = 0; i < TAM; i++) {
            sb.insert(0, caracteres[num % base]);
            num /= base;
        }

        while (sb.length() < TAM) {
            sb.insert(0, '0');
        }

        return sb.toString();
    }
}

