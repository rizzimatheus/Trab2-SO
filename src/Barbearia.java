import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

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
        qtdClientesRestantes = qtdClientes;
        qtdBarbeirosDisponiveis = qtdBarbeiros;
        filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
    }

    // Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente c) {
        if(barbeariaLotada()) { //se todos os barbeiros estiverem ocupados e a fila de espera estiver cheia
            return false;
        }
        else {
            filaEspera.offer(c);
            //qtdCadeirasDisponiveis--;
            System.out.println("Cliente " + c.getID() + " esperando corte...");
            acordarClientes = false;
            acordarBarbeiros = true;
            notifyAll();
            //barbeiros.notify();

            while (acordarClientes == false || clientes[c.getID()-1].getAcordar() == false)  {
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
        //Se não tiver clientes na fila de espera
        //Para os barbeiros é feito notify(), por isso não é necessário colocar o wait dentro de um loop
        //O barbeiro que recebe o notify() é o próximo a receber a posse da CPU, por isso não é necessário retestar a condição?
        if (filaEspera.isEmpty()) {
            System.out.println("Barbeiro " + idBarbeiro +
                    " indo dormir um pouco… não há clientes na barbearia...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.qtdBarbeirosDisponiveis--;
        System.out.println("Barbeiro " + idBarbeiro + " acordou! Começando os trabalhos!");

        //qtdCadeirasDisponiveis++;
        return filaEspera.poll();
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public synchronized void corteTerminado(Cliente c) {
        //tornar verdadeira a condição do cliente 'c' ser acordado
        c.setAcordar(true);
        acordarClientes = true;
        acordarBarbeiros = false;
        notifyAll();
        //bloqueia barbeiro??? ("espera que ele (o cliente) saia da barbearia")
        qtdClientesRestantes--;
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

    public synchronized int getQtdClientesRestantes() {
        return qtdClientesRestantes;
    }
}
