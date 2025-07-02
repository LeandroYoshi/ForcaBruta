public class solucaoSequencial {
    //Caracteres utilizados na senha
    static final String CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789";
    
    //Senha
    static String target = "g1bc";

    public static void main(String[] args) {
        
        //Inicia contagem de tempo do inicio
        long start = System.currentTimeMillis();
        forcaBruta("", 0, 6);
        
        //Inicia contagem de tempo do fim e printa
        long end = System.currentTimeMillis();
        System.out.println("Tempo: " + (end - start) + "ms");
    }

    //Função de quebra de senha
    static boolean forcaBruta(String prefixo, int caractere, int maxCaracteres) {
        //Retorna texto senha encontrada
        if (prefixo.equals(target)) {
            System.out.println("Senha encontrada: " + prefixo);
            return true;
        }
        
        //Se atigir o maxCaracteres e não achar senha cancela
        if (caractere == maxCaracteres) return false;
        
        //Laço for que vai tentando a senha utilizada utilizando recursividade
        for (int i = 0; i < CHARSET.length(); i++) {
            if (forcaBruta(prefixo + CHARSET.charAt(i), caractere + 1, maxCaracteres)) {
                return true;
            }
        }
        return false;
    }
}
