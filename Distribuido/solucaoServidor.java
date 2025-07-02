import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class solucaoServidor {
    //Caracteres disponiveis de senha
    static final String CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789";
    static final int MAX_LENGTH = 6;
    
    //Senha
    static final String SENHA_ALVO = "g1bc";
    
    //Número de clientes
    static final int NUM_CLIENTES = 4;
    
    static AtomicBoolean encontrada = new AtomicBoolean(false);
    static long totalComb;
    static long inicioTempo;

    public static void main(String[] args) throws Exception {
        totalComb = calcularTotalCombinacoes(MAX_LENGTH);
        long faixa = totalComb / NUM_CLIENTES;

        ServerSocket server = new ServerSocket(5000);
        System.out.println("Servidor iniciado na porta 5000...");

        inicioTempo = System.currentTimeMillis();

        for (int i = 0; i < NUM_CLIENTES; i++) {
            Socket cliente = server.accept();
            long inicio = i * faixa;
            long fim = (i == NUM_CLIENTES - 1) ? totalComb : (i + 1) * faixa;

            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
            // Envia: início;fim;senha;ID_cliente
            out.println(inicio + ";" + fim + ";" + SENHA_ALVO + ";" + i);

            new Thread(new ClienteHandler(cliente)).start();
        }

        server.close();
    }

    static class ClienteHandler implements Runnable {
        Socket socket;

        ClienteHandler(Socket socket) {
            this.socket = socket;
        }
        
        //Aqui nesta run ele vai trabalhar os clientes como se fossem threads
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String linha;
                while ((linha = in.readLine()) != null && !encontrada.get()) {
                    if (linha.startsWith("FOUND")) {
                        encontrada.set(true);
                        long fimTempo = System.currentTimeMillis();
                        long duracao = fimTempo - inicioTempo;

                        // Ex: FOUND a1b2;Cliente 2
                        String[] partes = linha.substring(6).split(";");
                        String senha = partes[0];
                        String clienteId = partes[1];

                        System.out.println("Senha encontrada: " + senha);
                        System.out.println("Cliente que encontrou: Cliente " + clienteId);
                        System.out.println("Tempo decorrido: " + duracao + "ms");
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static long calcularTotalCombinacoes(int maxLen) {
        long total = 0;
        int base = CHARSET.length();
        for (int i = 1; i <= maxLen; i++) {
            total += Math.pow(base, i);
        }
        return total;
    }
}
