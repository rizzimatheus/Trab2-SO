public class ProblemaDosBarbeiros {

    public static void main(String[] args) throws InterruptedException {
        int qtdBarbeiros = Integer.parseInt(args[0]);
        int qtdCadeiras = Integer.parseInt(args[1]);
        int qtdClientes = Integer.parseInt(args[2]);
        int i;

        Barbearia barbearia = new Barbearia(qtdCadeiras);

        Thread[] threads = new Thread[qtdBarbeiros+qtdClientes];

        //Barbeiro[] barbeiros = new Barbeiro[qtdBarbeiros];
        for (i = 0; i < qtdBarbeiros; i++) {
            //barbeiros[i] = new Barbeiro(i+1, barbearia);
            //threads[i] =  new Thread(barbeiros[i]);
            threads[i] =  new Thread(new Barbeiro(i+1, barbearia));
        }

        //Cliente[] clientes = new Cliente[qtdClientes];
        for(int j = 0; j < qtdClientes; j++,i++) {
            //clientes[j] = new Cliente(j+1, barbearia);
            //threads[i] =  new Thread(clientes[j]);
            threads[i] =  new Thread(new Cliente(j+1, barbearia));
        }

        //barbearia.setClientes(clientes);
        //barbearia.setBarbeiros(barbeiros);

        for (Thread t : threads)
            t.start();
//        for (Thread t : threads)
//            t.join();

    }
}
