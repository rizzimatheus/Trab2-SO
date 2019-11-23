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
            cliente = barbearia.proximoCliente(this.getID());

            //Espera de 3 a 5 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Cliente " + cliente.getID() + " cortando cabelo com Barbeiro " + this.getID());
            barbearia.corteTerminado(cliente);

            //Espera de 3 a 5 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
