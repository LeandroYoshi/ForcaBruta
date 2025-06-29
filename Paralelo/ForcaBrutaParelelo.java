package Paralelo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class ForcaBrutaParelelo {

    private static final char[] caracteres = "0123456789".toCharArray(); //Caracteres utilizados para quebrar a senha
    private static final int num_threads = 4; //Número de threads

    public static void main(String[] args) {
        for (int tam = 4; tam <= 9; tam++) {//Faz a repetição de quebrar e gerar senhas de até 9 digitos
            String senha = senhaRandom(tam);

            System.out.println("Senha de " + tam + " dígitos gerada com sucesso!!!");

            long total = (long) Math.pow(caracteres.length, tam);
            AtomicBoolean encontrada = new AtomicBoolean(false);
            long inicio = System.currentTimeMillis();

            //Cria e inicia as threads
            Thread[] threads = new Thread[num_threads];
            long intervalo = total / num_threads;

            for (int i = 0; i < num_threads; i++) {
                long inicioBusca = i * intervalo;
                long fimBusca = (i == num_threads - 1) ? total : (i + 1) * intervalo;

                threads[i] = new Thread(new forcaBruta(senha, tam, inicioBusca, fimBusca, encontrada));
                threads[i].start();
            }

            //espera todas as threads terminarem
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long fim = System.currentTimeMillis();
            System.out.println("Tempo total " + (fim - inicio) + "ms");
        }
    }

    //Método para gerar senha aleatória
    public static String senhaRandom(int TAM) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < TAM; i++) {
            char c = caracteres[random.nextInt(caracteres.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    //Método para quebrar senha utilizando as threads
    static class forcaBruta implements Runnable {

        private final String senha;
        private final int TAM;
        private final long inicio;
        private final long fim;
        private final AtomicBoolean encontrada;

        public forcaBruta(String senha, int TAM, long inicio, long fim, AtomicBoolean encontrada) {
            this.senha = senha;
            this.TAM = TAM;
            this.inicio = inicio;
            this.fim = fim;
            this.encontrada = encontrada;
        }

        @Override
        public void run() {
            for (long i = inicio; i < fim && !encontrada.get(); i++) {
                String tentativa = gerarTentativa(i, TAM);
                if (tentativa.equals(senha)) {
                    System.out.println("Senha encontrada pela thread" + Thread.currentThread().getName() + ": " + tentativa);
                    encontrada.set(true);
                    break;
                }
            }
        }
    }

    //Método que gera tentativa de senha
    public static String gerarTentativa(long num, int TAM) {
        StringBuilder sb = new StringBuilder();
        int base = caracteres.length;

        for (int i = 0; i < TAM; i++) {
            sb.insert(0, caracteres[(int)(num % base)]);
            num /= base;
        }

        //Preenche com '0' à  esquerda se precisar
        while (sb.length() < TAM) {
            sb.insert(0, '0');
        }
        return sb.toString();

    }

}
