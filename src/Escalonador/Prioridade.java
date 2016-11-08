package Escalonador;

import Processos.Processo;
import Processos.Tipo;
import java.util.LinkedList;

/**
 *
 * @author elivelton
 */
public class Prioridade {

    LinkedList<Processo> listaProcesso = null;
    LinkedList<Processo> listaPronto = new LinkedList();
    LinkedList<Processo> listaBloqueado = new LinkedList();
    private boolean flag = true;
    int tempo = -1;

    public void inicializar(LinkedList<Processo> listaProcesso) {
        this.listaProcesso = listaProcesso;
        escalonar();
    }

    public void escalonar() {
        Processo processo = null;
            tempo++;

        do {
            verificaListaProcessos();
            if (flag) {
                processo = prioridade();
                if (processo != null) {
                    flag = false;
                }

            }

            while (!flag) {
                if (processo != null) {
                    executar(processo);
//                    System.out.println("Executando processo");
                    tempo++;
                }
                verificaListaProcessos();
            }

        } while (!(listaProcesso.isEmpty() && listaPronto.isEmpty()));
        System.out.println("Fim do while");
    }

    public Processo prioridade() {

        if (!listaPronto.isEmpty()) {
            Processo p = listaPronto.get(0);
            

            for (int i = 0; i < this.listaPronto.size(); i++) {
                if (listaPronto.get(i).getPrioridade() < p.getPrioridade()) {
                    p = listaPronto.get(i);
                }
            }

            return p;
        } else {
            return null;
        }
    }

    public void executar(Processo p) {
        if (p.getTipo().equals(Tipo.Sistema)) {
            if (!listaBloqueado.isEmpty()) {
                for (int i = listaBloqueado.size(); i > 0; i--) {
                    listaPronto.add(listaBloqueado.getFirst());
                    listaBloqueado.removeFirst();
                }
            }
        }

        if (!p.getListaES().isEmpty()) {
            if (tempo == p.getListaES().getFirst()) {
                listaBloqueado.add(p);
                listaPronto.remove(p);
            }
        }

        if (p.getDuracao() > 0) {
            p.setDuracao(p.getDuracao() - 1);
            System.out.println("["+ tempo +"][Executando] Processo " + p.getId());
            if (p.getDuracao() == 0) {
                listaPronto.remove(p);
                System.out.println("["+ tempo +"][Término] Processo " + p.getId());
                flag = true;
            }
        }
    }

    public void verificaListaProcessos() {
        if (!listaProcesso.isEmpty()) {
            if (listaProcesso.getFirst().getTempo() == tempo) {
                listaPronto.add(listaProcesso.getFirst());
                System.out.println("["+ tempo +"][Chegada] Processo " + listaProcesso.getFirst().getId());
                listaProcesso.removeFirst();
                flag = true;
            }
        }
    }
}
