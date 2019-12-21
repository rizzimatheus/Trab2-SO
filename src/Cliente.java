import java.util.concurrent.ThreadLocalRandom;

public class Cliente extends Pessoa implements Runnable{
    private Barbearia barbearia;
    private boolean acordar = false;
    private Barbeiro barbeiro;

    public Cliente(int id, Barbearia b) {
        super(id);
        this.barbearia = b;
    }

    @Override
    public void run() {
        while(true) {
            while (!barbearia.cortaCabelo(this)) {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Cliente deve ficar em loop chamando cortaCabelo(). Dê um intervalo entre 3 e 5 seg a cada loop.
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

    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }
}