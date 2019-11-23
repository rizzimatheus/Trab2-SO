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
            System.out.println("Cliente " + this.getID() + " tentou entrar na barbearia, mas está lotada… indo dar uma voltinha");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Cliente " + this.getID() + " terminou o corte… saindo da barbearia!");
    }

    public boolean getAcordar() {
        return acordar;
    }

    public void setAcordar(boolean acordar) {
        this.acordar = acordar;
    }
}
