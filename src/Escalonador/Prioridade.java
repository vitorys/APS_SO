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
    public LinkedList<DadosGUI> dados = new LinkedList();
    private boolean flag = true;
    int tempo = 0;

    public LinkedList<DadosGUI> inicializar(LinkedList<Processo> listaProcesso) {
        this.listaProcesso = listaProcesso;
        System.out.println("Entrei Prio.");
        escalonar();
        
        return dados;
    }

    public void escalonar() {
        Processo processo = null;
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
                    tempo++;
                }
                verificaListaProcessos();
            }

            tempo++;
//            System.out.println("Lista Pronto " +listaPronto.size());
//            System.out.println("Lista Processo " +listaProcesso.size());
        } while (!(listaProcesso.isEmpty() && listaPronto.isEmpty()));
    }

    public Processo prioridade() {

        if (!listaPronto.isEmpty()) {
            Processo p = listaPronto.get(0);

            for (int i = 0; i < this.listaPronto.size(); i++) {
                if (listaPronto.get(i).getPrioridade() <= p.getPrioridade()) {
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
            executarProcessoSistema();

        } else if (!p.getListaES().isEmpty()) {
            verificaBlqueado(p);

        } else if (p.getDuracao() > 0 && !flag) {
            p.setDuracao(p.getDuracao() - 1);
            System.out.println("Executando processo "+p.getId());
            dados.add(new DadosGUI(p.getId(), tempo, "Executando", p.getPrioridade(), p.getDuracao(), "Usuário"));

            if (p.getDuracao() == 0) {
                listaPronto.remove(p);

                dados.add(new DadosGUI(p.getId(), tempo, "Término", p.getPrioridade(), p.getDuracao(), "Usuário"));

                flag = true;
            }
        }
    }

    public void verificaListaProcessos() {
        if (!listaProcesso.isEmpty()) {
            if (listaProcesso.getFirst().getTempo() == tempo) {
                listaPronto.add(listaProcesso.getFirst());
                System.out.println("[" + tempo + "][Chegada] Processo " + listaProcesso.getFirst().getId());
                dados.add(new DadosGUI(listaProcesso.getFirst().getId(), tempo, "Chegada", listaProcesso.getFirst().getPrioridade(), listaProcesso.getFirst().getDuracao(), "Usuário"));
                listaProcesso.removeFirst();
                flag = true;
            }
        }
    }

    private void executarProcessoSistema() {
        tempo++;
        verificaListaProcessos();
        dados.add(new DadosGUI(this.tempo, "Executando", "Sistema"));

        if (!listaBloqueado.isEmpty()) {
            for (int i = listaBloqueado.size(); i > 0; i--) {
                listaPronto.add(listaBloqueado.getFirst());
                listaBloqueado.removeFirst();
            }
                flag = true;

            tempo++;
            verificaListaProcessos();
        }
    }

    private void verificaBlqueado(Processo p) {
        for (int i = 0; i < p.getListaES().size(); i++) {
            if (tempo == p.getListaES().get(i)) {
                dados.add(new DadosGUI(p.getId(), tempo, "Bloqueado", p.getPrioridade(), p.getDuracao(), "Usuário"));
                System.out.println("Bloqueou");
                p.getListaES().remove(i);
                listaBloqueado.add(p);
                listaPronto.remove(p);
                flag = true;
            }
        }
    }
}
