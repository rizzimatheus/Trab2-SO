import java.util.concurrent.LinkedBlockingQueue;

public class Barbearia {
    private LinkedBlockingQueue<Cliente> filaEspera;    //Fila de espera da barbearia (cadeiras de espera)
    private Barbeiro[] barbeiros;                       //Lista de barbeiros
    private boolean acordarBarbeiros = false;           //Se for para acordar os barbeiros
    private boolean acordarClientes = false;            //Se for para acordar os clientes

    public Barbearia(int qtdCadeiras) {
        filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
    }

    // **Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente cliente) {
        if(barbeariaLotada()) { // Se a fila de espera estiver cheia
            System.out.println("Cliente " + cliente.getID() + " tentou entrar na barbearia, mas está lotada… indo dar uma voltinha");
            return false;
        }
        else {
            filaEspera.offer(cliente);  // Adiciona cliente na fila de espera
            System.out.println("Cliente " + cliente.getID() + " esperando corte...");

            acordarClientes = false;
            acordarBarbeiros = true;
            notifyAll();

            while (acordarClientes == false || cliente.getAcordar() == false)  {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cliente.setAcordar(false);
            System.out.println("Cliente " + cliente.getID() + " terminou o corte… saindo da barbearia!");

            // Acorda barbeiro ("O barbeiro acorda o cliente que está na sua cadeira e **espera que ele saia da barbearia**")
            acordarClientes = false;
            acordarBarbeiros = true;
            cliente.getBarbeiro().setAcordar(true);
            notifyAll();
            return true;
        }
    }

    private synchronized boolean barbeariaLotada() {
        if (filaEspera.remainingCapacity() == 0)
            return true;
        else
            return false;
    }

    // **Operações chamadas pelos barbeiros:

    // Seleciona o próximo cliente (dentro desta chamada o barbeiro pode dormir esperando um cliente).
    public synchronized Cliente proximoCliente(int idBarbeiro) {
        boolean dormiu = false;
        //Enquanto não tiver clientes na fila de espera
        while (filaEspera.isEmpty()) {
            if (!dormiu)
                System.out.println("Barbeiro " + idBarbeiro +
                        " indo dormir um pouco… não há clientes na barbearia...");
            dormiu = true;
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (dormiu)
            System.out.println("Barbeiro " + idBarbeiro + " acordou! Começando os trabalhos!");

        //Retira cliente da fila de espera e seta o barbeiro do cliente
        Cliente cliente = filaEspera.poll();
        cliente.setBarbeiro(barbeiros[idBarbeiro-1]);
        System.out.println("Cliente " + cliente.getID() + " cortando cabelo com Barbeiro " + idBarbeiro);
        return cliente;
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public synchronized void corteTerminado(Cliente cliente) {
        // Tornar verdadeira a condição dos clientes e do cliente específico ser acordado
        cliente.setAcordar(true);
        acordarClientes = true;
        acordarBarbeiros = false;
        notifyAll();
        // Bloqueia barbeiro("espera que ele (o cliente) saia da barbearia")
        while (acordarBarbeiros == false || cliente.getBarbeiro().getAcordar() == false)  {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cliente.getBarbeiro().setAcordar(false);
    }

    public void setBarbeiros(Barbeiro[] barbeiros) {
        this.barbeiros = barbeiros;
    }

}