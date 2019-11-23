import java.util.concurrent.ThreadLocalRandom;

public class Barbeiro extends Pessoa implements Runnable {
    //private int cadeirasDisponiveis;
    private Cliente clitenteAtendido;


    public Barbeiro(int id) {
        super(id);
    }

    @Override
    public void run() {
        Barbearia.proximoCliente();
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Barbearia.corteTerminado(clitenteAtendido);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
