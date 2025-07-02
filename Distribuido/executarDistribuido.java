public class executarDistribuido {
    public static void main(String[] args) throws Exception {
        // Inicia o servidor em uma nova thread
        new Thread(() -> {
            try {
                solucaoServidor.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Aguarda o servidor subir (importante!)
        Thread.sleep(2000);

        // Inicia os 4 clientes em novas threads
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {
                    solucaoCliente.main(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
