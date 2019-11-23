import java.util.concurrent.ThreadLocalRandom;

public class Barbeiro extends Pessoa implements Runnable {
    private Barbearia barbearia;
    //private int cadeirasDisponiveis;
    //private Cliente clienteAtendido;


    public Barbeiro(int id, Barbearia b) {
        super(id);
        this.barbearia = b;
    }

    @Override
    public void run() {
        Cliente cliente;
        while (true) {
            //Se não tiver clientes na fila de espera
            while ((cliente = barbearia.proximoCliente()) == null) {
                System.out.println("Barbeiro " + this.getID() +
                        " indo dormir um pouco… não há clientes na barbearia...");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Barbeiro " + this.getID() + " acordou! Começando os trabalhos!");

            //Espera de 3 a 5 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Termina de cortar o cabelo do clinte
            barbearia.corteTerminado(cliente);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
