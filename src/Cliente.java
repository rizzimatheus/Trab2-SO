import java.util.concurrent.ThreadLocalRandom;

public class Cliente extends Pessoa implements Runnable{
    private Barbearia barbearia;
    private boolean acordar = false;

    public Cliente(int id, Barbearia b) {
        super(id);
        this.barbearia = b;
    }

    @Override
    public void run() {
        while(!barbearia.cortaCabelo(this)) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
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
