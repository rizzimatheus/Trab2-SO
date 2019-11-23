import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class Barbearia {
    private static int qtdBarbeiros;
    private static int qtdCadeiras;
    private static int qtdClientes;
    private static int qtdCadeirasDisponiveis;
    private static int qtdBarbeirosDisponiveis;
    private static Queue<Cliente> filaEspera;

    public Barbearia(int qtdBarbeiros, int qtdCadeiras, int qtdClientes) {
        Barbearia.qtdBarbeiros = qtdBarbeiros;
        Barbearia.qtdCadeiras = qtdCadeiras;
        Barbearia.qtdClientes = qtdClientes;
        Barbearia.qtdCadeirasDisponiveis = qtdCadeiras;
        Barbearia.qtdBarbeirosDisponiveis = qtdBarbeiros;
        Barbearia.filaEspera = new LinkedList<Cliente>();
    }

    // Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public static synchronized boolean cortaCabelo(Cliente c) {
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

    private static boolean barbeariaLotada() {
        if (qtdCadeirasDisponiveis == 0 && qtdBarbeirosDisponiveis == 0)
            return true;
        else
            return false;
    }

    // Operações chamadas pelos barbeiros:

    // Seleciona o próximo cliente (dentro desta chamada o barbeiro pode dormir esperando um cliente).
    public static synchronized Cliente proximoCliente() {
        if (filaEspera.isEmpty()){
            //printa “Barbeiro Y indo dormir um pouco… não há clientes na barbearia...”
            //bloqueia barbeiro
        }
        //printa “Barbeiro Y acordou! Começando os trabalhos!”
        //retira cliente da fila de espera
        return filaClientes.pop();
    }

    // O barbeiro acorda o cliente que está na sua cadeira e espera que ele saia da barbearia
    // (tome cuidado para acordar o cliente certo).
    public static synchronized void corteTerminado(Cliente c) {
        //acorda cliente
        //se bloqueia ("espera que ele saia da barbearia")
    }

}
