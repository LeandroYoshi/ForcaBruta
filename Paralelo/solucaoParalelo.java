public class SolucaoParalelo {
    //Caracteres disponiveis na senha
    static final String caracteres = "abcdefghijklmnopqrstuvwxyz0123456789";
    
    //Máximo de caracteres na senha
    static final int MAX_LENGTH = 6;

    // Senha a ser encontrada
    static final String senhaAlvo = "g1bc";

    static volatile boolean encontrado = false;

    public static void main(String[] args) {
        int nThreads = 4;
        long totalComb = combinacaoTotal(MAX_LENGTH);
        long faixa = totalComb / nThreads;

        Thread[] threads = new Thread[nThreads];
        long start = System.currentTimeMillis();

        //Inicia as threads de acordo com o inicio e fim de onde cada Thread vai trabalhar
        for (int i = 0; i < nThreads; i++) {
            final long inicio = i * faixa;
            final long fim = (i == nThreads - 1) ? totalComb : (i + 1) * faixa;

            threads[i] = new Thread(() -> forcaBruta(inicio, fim));
            threads[i].start();
        }

        //Espera todas as threads encerrarem
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ignored) {}
        }

        //Calcula o tempo utilizado para função
        long end = System.currentTimeMillis();
        System.out.println("Tempo: " + (end - start) + "ms");
    }

    static void forcaBruta(long start, long end) {
        for (long i = start; i < end && !encontrado; i++) {
            String tentativa = numParaString(i);
            if (tentativa.equals(senhaAlvo)) {
                System.out.println("Senha encontrada: " + tentativa);
                encontrado = true;
                break;
            }
        }
    }

    //Calcula o total de possibilidades de acordo com os caracteres
    static long combinacaoTotal(int maxLen) {
        long total = 0;
        int base = caracteres.length();
        for (int len = 1; len <= maxLen; len++) {
            total += Math.pow(base, len);
        }
        return total;
    }

    //Converte valores numéricos para String para não gerar senhas com erro
    static String numParaString(long num) {
        int base = caracteres.length();
        StringBuilder sb = new StringBuilder();
        long count = 0;

        for (int len = 1; len <= MAX_LENGTH; len++) {
            long nivel = (long) Math.pow(base, len);
            if (num < count + nivel) {
                long offset = num - count;
                for (int i = 0; i < len; i++) {
                    sb.insert(0, caracteres.charAt((int)(offset % base)));
                    offset /= base;
                }
                return sb.toString();
            }
            count += nivel;
        }

        return "";
    }
}
