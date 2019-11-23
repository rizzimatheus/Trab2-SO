public class ProblemaDosBarbeiros {

    public static void main(String[] args) throws InterruptedException {
        int qtdBarbeiros = Integer.parseInt(args[0]);
        int qtdCadeiras = Integer.parseInt(args[1]);
        int qtdClientes = Integer.parseInt(args[2]);
        int i;

        Thread[] threads = new Thread[qtdBarbeiros+qtdClientes];

        Barbeiro[] barbeiros = new Barbeiro[qtdBarbeiros];
        for (i = 0; i < qtdBarbeiros; i++) {
            barbeiros[i] = new Barbeiro(i+1);
            threads[i] =  new Thread(barbeiros[i]);
        }

        Cliente[] clientes = new Cliente[qtdClientes];
        for(int j = 0; j < qtdClientes; j++,i++) {
            clientes[j] = new Cliente(i);
            threads[i] =  new Thread(clientes[i]);
        }

        for (Thread t : threads)
            t.start();
        for (Thread t : threads)
            t.join();

//        i = 0;
//
//        while(i < qtdClientes) {
//            try {
//                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
//            } catch (InterruptedException e){}
//            Cliente novoCliente = new Cliente(i++);
//            if(/*qtdGntCortando == qtdBarbeiros || qtdGntNaFila == qtd qtdCadeiras*/) {
//                System.out.println("Cliente "+ novoCliente.getID() +" terminou o corte… saindo da barbearia!");
//            }
//            else
//                System.out.println("Cliente "+novoCliente.getID()+
//                        " tentou entrar na barbearia, mas está lotada… indo dar uma voltinha");
//
//        }
    }
}
