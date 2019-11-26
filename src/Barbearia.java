import java.util.concurrent.LinkedBlockingQueue;

public class Barbearia {
    private LinkedBlockingQueue<Cliente> filaEspera;
    //private Cliente[] clientes;
    //private Barbeiro[] barbeiros;
    private boolean acordarBarbeiros = false;
    private boolean acordarClientes = false;

    public Barbearia(int qtdCadeiras) {
        filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
    }

    // **Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente cliente) {
        if(barbeariaLotada()) { //se a fila de espera estiver cheia
            System.out.println("Cliente " + cliente.getID() + " tentou entrar na barbearia, mas está lotada… indo dar uma voltinha");
            return false;
        }
        else {
            filaEspera.offer(cliente);
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
            //acorda barbeiro?? ("O barbeiro acorda o cliente que está na sua cadeira e **espera que ele saia da barbearia**")
            System.out.println("Cliente " + cliente.getID() + " terminou o corte… saindo da barbearia!");
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
        //Se não tiver clientes na fila de espera
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

        Cliente cliente = filaEspera.poll();
        System.out.println("Cliente " + cliente.getID() + " cortando cabelo com Barbeiro " + idBarbeiro);
        return cliente;
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public synchronized void corteTerminado(Cliente cliente) {
        //tornar verdadeira a condição do cliente ser acordado
        cliente.setAcordar(true);
        acordarClientes = true;
        acordarBarbeiros = false;
        notifyAll();
        //bloqueia barbeiro??? ("espera que ele (o cliente) saia da barbearia")
    }

//    public Cliente[] getClientes() {
//        return clientes;
//    }
//
//    public void setClientes(Cliente[] clientes) {
//        this.clientes = clientes;
//    }
//
//    public Barbeiro[] getBarbeiros() {
//        return barbeiros;
//    }
//
//    public void setBarbeiros(Barbeiro[] barbeiros) {
//        this.barbeiros = barbeiros;
//    }

}