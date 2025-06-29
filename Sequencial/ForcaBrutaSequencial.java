package Sequencial;

import java.util.Random;

public class ForcaBrutaSequencial {

    private static final char[] caracteres = "0123456789".toCharArray(); //Caracteres que serão utilizados para quebrar a senha
    private static String senha; //Variável para armazenar senha correta

    public static void main(String[] args) {
        System.out.println("Teste com senhas de tamanho diferente");

        for (int TAM = 4; TAM <= 9; TAM++) {//Faz a repetição de quebrar e gerar senhas de até 9 digitos
            String senha = senhaRandom(TAM);
            System.out.println("Senha de " + TAM + " digitos gerada com sucesso!!!");

            long inicio = System.currentTimeMillis();
            String tentativa = forcaBruta(TAM, senha);
            long fim = System.currentTimeMillis();

            if (tentativa != null) {
                System.out.println("Senha encontrada: " + tentativa);
                System.out.println("Tempo: " + (fim - inicio) + " ms");
            } else {
                System.out.println("Senha não encontrada.");
            }
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

    //Método para quebrar senha
    public static String forcaBruta(int TAM, String senha) {
        int total = (int) Math.pow(caracteres.length, TAM);

        for (int i = 0; i < total; i++) {
            String tentativa = gerarTentativa(i, TAM);
            if (tentativa.equals(senha)) {
                return tentativa;
            }
        }
        return null;
    }

    //Método que gera tentativa de senha
    public static String gerarTentativa(int num, int TAM) {
        StringBuilder sb = new StringBuilder();
        int base = caracteres.length;

        for (int i = 0; i < TAM; i++) {
            sb.insert(0, caracteres[num % base]);
            num /= base;
        }

        //Preenche com '0' à  esquerda se precisar
        while(sb.length() < TAM){
            sb.insert(0, '0');
        }
        return sb.toString();
        
        
    }
}
