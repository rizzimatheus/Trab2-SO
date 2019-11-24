import java.util.concurrent.LinkedBlockingQueue;

public class Barbearia {
    //private int qtdBarbeiros;
    //private int qtdCadeiras;
    //private int qtdClientes;
    //private int qtdCadeirasDisponiveis;
    private int qtdClientesRestantes;
    private int qtdBarbeirosDisponiveis;
    private LinkedBlockingQueue<Cliente> filaEspera;
    private Cliente[] clientes;
    private Barbeiro[] barbeiros;
    private boolean acordarBarbeiros = false;
    private boolean acordarClientes = false;

    public Barbearia(int qtdBarbeiros, int qtdCadeiras, int qtdClientes) {
        //this.qtdBarbeiros = qtdBarbeiros;
        //this.qtdCadeiras = qtdCadeiras;
        //this.qtdClientes = qtdClientes;
        //qtdCadeirasDisponiveis = qtdCadeiras;
        //qtdClientesRestantes = qtdClientes;
        qtdBarbeirosDisponiveis = qtdBarbeiros;
        filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
    }

    // Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente cliente) {
        if(barbeariaLotada()) { //se todos os barbeiros estiverem ocupados e a fila de espera estiver cheia
            return false;
        }
        else {
            filaEspera.offer(cliente);
            System.out.println("Cliente " + cliente.getID() + " esperando corte...");

            acordarClientes = false;
            acordarBarbeiros = true;
            notifyAll();

            while (acordarClientes == false || clientes[cliente.getID()-1].getAcordar() == false)  {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //acorda barbeiro?? ("O barbeiro acorda o cliente que está na sua cadeira e **espera que ele saia da barbearia**")
            return true;
        }
    }

    private boolean barbeariaLotada() {
        //if (filaEspera.remainingCapacity() == 0 && qtdBarbeirosDisponiveis == 0)
        if (filaEspera.remainingCapacity() == 0)
            return true;
        else
            return false;
    }

    // Operações chamadas pelos barbeiros:

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
        this.qtdBarbeirosDisponiveis--;
        if (dormiu)
            System.out.println("Barbeiro " + idBarbeiro + " acordou! Começando os trabalhos!");

        Cliente cliente = filaEspera.poll();
        System.out.println("Cliente " + cliente.getID() + " cortando cabelo com Barbeiro " + idBarbeiro);
        return cliente;
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public synchronized void corteTerminado(Cliente cliente) {
        //tornar verdadeira a condição do cliente 'c' ser acordado
        cliente.setAcordar(true);
        acordarClientes = true;
        acordarBarbeiros = false;
        notifyAll();
        //bloqueia barbeiro??? ("espera que ele (o cliente) saia da barbearia")
        //qtdClientesRestantes--;
        qtdBarbeirosDisponiveis++;
    }

    //    public void setVetAcordarClientes(boolean[] vetAcordarClientes) {
//        this.vetAcordarClientes = vetAcordarClientes;
//    }


    public Cliente[] getClientes() {
        return clientes;
    }

    public void setClientes(Cliente[] clientes) {
        this.clientes = clientes;
    }

    public Barbeiro[] getBarbeiros() {
        return barbeiros;
    }

    public void setBarbeiros(Barbeiro[] barbeiros) {
        this.barbeiros = barbeiros;
    }

//    public synchronized int getQtdClientesRestantes() {
//        return qtdClientesRestantes;
//    }

    public String filaToString() {
        String s = "[";
        for (Cliente c: filaEspera) {
            s = s + c.getID() + ", ";
        }
        s = s + "]";
        return s;
    }
}
