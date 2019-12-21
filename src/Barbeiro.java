import java.util.concurrent.ThreadLocalRandom;

public class Barbeiro extends Pessoa implements Runnable {
    private Barbearia barbearia;
    private boolean acordar = false;

    public Barbeiro(int id, Barbearia b) {
        super(id);
        this.barbearia = b;
    }

    @Override
    public void run() {
        Cliente cliente;
        while (true) {
            cliente = barbearia.proximoCliente(this.getID());

            //Espera de 1 a 3 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            barbearia.corteTerminado(cliente);

            //Espera de 1 a 3 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getAcordar() {
        return acordar;
    }

    public void setAcordar(boolean acordar) {
        this.acordar = acordar;
    }
}