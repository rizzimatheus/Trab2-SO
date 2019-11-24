import java.util.concurrent.LinkedBlockingQueue;

public class Barbearia {
    private int qtdBarbeirosDisponiveis;
    private LinkedBlockingQueue<Cliente> filaEspera;
    private Cliente[] clientes;
    private Barbeiro[] barbeiros;
    private boolean acordarBarbeiros = false;
    private boolean acordarClientes = false;
    private boolean acordarEntradaBarbearia = false;
    private LinkedBlockingQueue<Cliente> filaEntrarBarbearia;
    private int qtdClientesRestantes;
    private boolean encerrar = false;

    public Barbearia(int qtdBarbeiros, int qtdCadeiras, int qtdClientes) {
        qtdBarbeirosDisponiveis = qtdBarbeiros;
        filaEspera = new LinkedBlockingQueue<Cliente>(qtdCadeiras);
        filaEntrarBarbearia = new LinkedBlockingQueue<Cliente>(qtdBarbeiros);
        qtdClientesRestantes = qtdClientes;
    }

    // **Operação chamada pelos clientes:

    // Se a barbearia não estiver lotada, espera que o corte seja feito e retorna TRUE.
    // Se a barbearia estiver lotada, retorna FALSE.
    public synchronized boolean cortaCabelo(Cliente cliente) {
        if(barbeariaLotada()) { //se todos os barbeiros estiverem ocupados e a fila de espera estiver cheia
            System.out.println("Cliente " + cliente.getID() + " tentou entrar na barbearia, mas está lotada… indo dar uma voltinha");
            return false;
        }
        else {
            //se as cadeiras de espera estiverem lotadas, mas tem barbeiros disponíveis ou
            // se tem alguem na fila para entrar na barbearia
            if (filaEspera.remainingCapacity() == 0 || (filaEntrarBarbearia.size() > 0)) {
                //se tem mais barbeiros disponíveis do que clientes querendo entrar na barbearia
                if ((qtdBarbeirosDisponiveis - filaEntrarBarbearia.size()) > 0) {
                    filaEntrarBarbearia.offer(cliente);
                    System.out.println("Cliente " + cliente.getID() + " entrou na fila para entrar na barbearia");
                    while (acordarEntradaBarbearia == false || cliente.getAcordar() == false) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    cliente.setAcordar(false);
                    filaEntrarBarbearia.poll();
                }
                else
                    return false;
            }
            filaEspera.offer(cliente);
            System.out.println("Cliente " + cliente.getID() + " esperando corte...");

            acordarClientes = false;
            acordarBarbeiros = true;
            acordarEntradaBarbearia = false;
            notifyAll();

            while (acordarClientes == false || clientes[cliente.getID()-1].getAcordar() == false)  {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //acorda barbeiro?? ("O barbeiro acorda o cliente que está na sua cadeira e **espera que ele saia da barbearia**")
            //qtdClientesRestantes--;
            System.out.println("Cliente " + cliente.getID() + " terminou o corte… saindo da barbearia!");
            return true;
        }
    }

    private boolean barbeariaLotada() {
        //if (filaEspera.remainingCapacity() == 0)
        if (filaEspera.remainingCapacity() == 0 && qtdBarbeirosDisponiveis == 0)
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
            //Gambiarra para o programa encerrar quando todos os clientes forem atendidos
            if (encerrar) {
                return null;
            }
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Gambiarra para o programa encerrar quando todos os clientes forem atendidos
            if (encerrar) {
                return null;
            }
        }
        qtdBarbeirosDisponiveis--;
        if (dormiu)
            System.out.println("Barbeiro " + idBarbeiro + " acordou! Começando os trabalhos!");

        Cliente cliente = filaEspera.poll();
        //Se tem algum cliente na fila para entrar na barbearia
        if (filaEntrarBarbearia.size() > 0) {
            acordarClientes = false;
            acordarBarbeiros = false;
            acordarEntradaBarbearia = true;
            filaEntrarBarbearia.peek().setAcordar(true);
            notifyAll();
        }
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
        acordarEntradaBarbearia = false;
        notifyAll();
        //bloqueia barbeiro??? ("espera que ele (o cliente) saia da barbearia")
        qtdBarbeirosDisponiveis++;
        qtdClientesRestantes--;
    }

    //Gambiarra para o programa encerrar quando todos os clientes forem atendidos
    public synchronized boolean terminou(int barbeiroID) {
        System.out.println("Barbeiro "+ barbeiroID + " ENTROU NO TERMINOU(). CLIENTES RESTANTES: " + qtdClientesRestantes);
        if (qtdClientesRestantes == 0) {
            encerrar = true;
            acordarBarbeiros = true;
            notifyAll();
            return true;
        }
        else
            return false;
    }


    public void setClientes(Cliente[] clientes) {
        this.clientes = clientes;
    }

    public void setBarbeiros(Barbeiro[] barbeiros) {
        this.barbeiros = barbeiros;
    }

//    public synchronized int getQtdClientesRestantes() {
//        return qtdClientesRestantes;
//    }

//    public String filaToString() {
//        String s = "[";
//        for (Cliente c: filaEspera) {
//            s = s + c.getID() + ", ";
//        }
//        s = s + "]";
//        return s;
//    }
}
