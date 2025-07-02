import java.io.*;
import java.net.*;

public class solucaoCliente {
    static final String CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789";
    static final int MAX_LENGTH = 6;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String[] dados = in.readLine().split(";");
        //Dados que precisam retornar para o servidor
        long inicio = Long.parseLong(dados[0]);
        long fim = Long.parseLong(dados[1]);
        String senhaAlvo = dados[2];
        
        //Dados são recebidor pelo servidor
        String clienteId = dados[3];

        for (long i = inicio; i < fim; i++) {
            String tentativa = numParaString(i);
            if (tentativa.equals(senhaAlvo)) {
                out.println("FOUND " + tentativa + ";" + clienteId);
                break;
            }
        }

        socket.close();
    }
    
    //Converte valores numéricos para String para não gerar senhas com erro
    static String numParaString(long num) {
        int base = CHARSET.length();
        long count = 0;
        StringBuilder sb = new StringBuilder();

        for (int len = 1; len <= MAX_LENGTH; len++) {
            long nivel = (long) Math.pow(base, len);
            if (num < count + nivel) {
                long offset = num - count;
                for (int i = 0; i < len; i++) {
                    sb.insert(0, CHARSET.charAt((int)(offset % base)));
                    offset /= base;
                }
                return sb.toString();
            }
            count += nivel;
        }

        return "";
    }
}
