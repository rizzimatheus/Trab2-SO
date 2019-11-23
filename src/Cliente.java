import java.util.concurrent.ThreadLocalRandom;

public class Cliente extends Pessoa implements Runnable{
    public Cliente(int id) {
        super(id);

    }

    @Override
    public void run() {
        while(!Barbearia.cortaCabelo(this)) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
