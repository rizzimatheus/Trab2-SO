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

            //Gambiarra para o programa encerrar quando todos os clientes forem atendidos
            //Se não tiver mais clientes (o programa deve encerrar)
            if (cliente == null)
                break;

            //Espera de 3 a 5 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            barbearia.corteTerminado(cliente);

            //Gambiarra para o programa encerrar quando todos os clientes forem atendidos
            //Se for o ultimo barbeiro com cliente
            if (barbearia.terminou(getID())) {
                System.out.println("Barbeiro " + getID() + " indo dormir um pouco… não há clientes na barbearia...");
                break;
            }

            //Espera de 3 a 5 segundos
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
