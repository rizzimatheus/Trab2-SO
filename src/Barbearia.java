import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Barbearia {
    private int qtdBarbeiros;
    private int qtdCadeiras;
    private int qtdClientes;
    private int qtdCadeirasDisponiveis;
    private int qtdBarbeirosDisponiveis;
    private Queue<Cliente> filaEspera;

    public Barbearia(int qtdBarbeiros, int qtdCadeiras, int qtdClientes) {
        this.qtdBarbeiros = qtdBarbeiros;
        this.qtdCadeiras = qtdCadeiras;
        this.qtdClientes = qtdClientes;
        this.qtdCadeirasDisponiveis = qtdCadeiras;
        this.qtdBarbeirosDisponiveis = qtdBarbeiros;
        this.filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
    }

    // Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente c) {
        if(barbeariaLotada()) { //se todos os barbeiros estiverem ocupados ou a fila de espera estiver cheia
            return false;
        }
        else {
            //printa “Cliente X cortando cabelo com Barbeiro Y”
            //acorda barbeiro
            //bloqueia o clinte
            //printa “Cliente X terminou o corte… saindo da barbearia!”
            //acorda barbeiro ("O barbeiro acorda o cliente que está na sua cadeira e **espera que ele saia da barbearia**")
            return true;
        }
    }

    private boolean barbeariaLotada() {
        if (qtdCadeirasDisponiveis == 0 && qtdBarbeirosDisponiveis == 0)
            return true;
        else
            return false;
    }

    // Operações chamadas pelos barbeiros:

    // Seleciona o próximo cliente (dentro desta chamada o barbeiro pode dormir esperando um cliente).
    public synchronized Cliente proximoCliente() {
        if (filaEspera.isEmpty())
            return null;
        else {
            qtdCadeirasDisponiveis--;
            return filaEspera.poll();
        }
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public synchronized void corteTerminado(Cliente c) {
        notifyAll();
        //acorda cliente
        //se bloqueia ("espera que ele saia da barbearia")
    }

}
