import java.util.concurrent.ThreadLocalRandom;

public class Barbeiro extends Pessoa implements Runnable {
    private Barbearia barbearia;

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
