package Escalonador;

import Processos.Processo;
import Processos.Tipo;
import java.util.LinkedList;

/**
 *
 * @author vitor
 */
public class ShortestJobFirst {

    private boolean flag = false;
    LinkedList<Processo> listaProcesso = new LinkedList();
    LinkedList<Processo> listaPronto = new LinkedList();
    LinkedList<Processo> listaBloqueado = new LinkedList();
    public LinkedList<DadosGUI> dados = new LinkedList();
    int tempo = 0;
    Processo pSistema = new Processo();

    public LinkedList<DadosGUI> iniciar(LinkedList<Processo> listaProcesso) {
        this.listaProcesso = listaProcesso; // Lista de processos retirados do arquivo ordenados por ordem de chegada.
        pSistema.setTipo(Tipo.Sistema);
        escalonar();
        return dados;
    }

    public void escalonar() {
        Processo p_Executar;

        do {

            verificaListaProcessos();
            if (!listaBloqueado.isEmpty() && tempo % 3 == 0) {
                executar(pSistema);
            }

            if (flag == false) {
                if (!listaPronto.isEmpty()) {
                    p_Executar = ShortestJob();
                    flag = true;

                    while (flag) {
                        tempo++;
                        dados.add(new DadosGUI(p_Executar.getId(), tempo, "Executando", p_Executar.getPrioridade(), p_Executar.getDuracao(), "Usuário"));
                        executar(p_Executar);
                        verificaListaProcessos();
                    }
                }
            }

            tempo++;
        } while (!(listaProcesso.isEmpty() && listaPronto.isEmpty()));
    }

    public Processo ShortestJob() {
        Processo p = listaPronto.getFirst();

        for (int i = 0; i < listaPronto.size(); i++) {
            if (listaPronto.get(i).getDuracao() < p.getDuracao()) {
                p = listaPronto.get(i);
            }
        }

        return p;
    }

    public void verificaListaProcessos() {
        if (!listaProcesso.isEmpty()) {
            if (listaProcesso.getFirst().getTempo() == tempo) {

                listaPronto.add(listaProcesso.getFirst());
                System.out.println("[" + tempo + "][Chegada] Processo " + listaProcesso.getFirst().getId());
                dados.add(new DadosGUI(listaProcesso.getFirst().getId(), tempo, "Chegada", listaProcesso.getFirst().getPrioridade(), listaProcesso.getFirst().getDuracao(), "Usuário"));

                listaProcesso.removeFirst();

            }
        }
    }

    public void executar(Processo p) {

        if (p.getTipo().equals(Tipo.Sistema)) {
            executaProcessoSistema();

        } else if (!p.getListaES().isEmpty()) {
            verificaBloqueado(p);

        }if (p.getDuracao() > 0 && p.getTipo().equals(Tipo.Usuario) && flag == true) {
            p.setDuracao(p.getDuracao() - 1);
            p.setTempoExecucao(p.getTempoExecucao() + 1);
        }
        
        if (p.getDuracao() == 0 && p.getTipo().equals(Tipo.Usuario) && flag == true) {
            dados.add(new DadosGUI(p.getId(), tempo, "Término", p.getPrioridade(), p.getDuracao(), "Usuário"));
            listaPronto.remove(p);
            flag = false;
            tempo--;
        }
    }

    private void executaProcessoSistema() {
        tempo++;
        verificaListaProcessos();
        System.out.println("teste");
        dados.add(new DadosGUI(this.tempo, "Executando", "Sistema"));
        

        if (!listaBloqueado.isEmpty()) {
            for (int i = listaBloqueado.size(); i > 0; i--) {
                listaPronto.add(listaBloqueado.getFirst());
                listaBloqueado.removeFirst();
                flag = false;
            }

        }
    }

    private void verificaBloqueado(Processo p) {
        for (int i = 0; i < p.getListaES().size(); i++) {

            if (p.getTempoExecucao() == p.getListaES().get(i)) {
                dados.add(new DadosGUI(p.getId(), tempo, "Bloqueio", p.getPrioridade(), p.getDuracao(), "Usuário"));
                listaBloqueado.add(p);
                listaPronto.remove(p);
                flag = false;
                tempo--;
                
            }
            else if(tempo > p.getListaES().get(i)){
                p.getListaES().remove(i);
            }
        }
    }
}