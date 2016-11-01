/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    LinkedList<Processo> listaProcesso = null;
    LinkedList<Processo> listaPronto = new LinkedList();
    LinkedList<Processo> listaBloqueado = new LinkedList();
    int tempo = 0;

    public void iniciar(LinkedList<Processo> listaProcesso) {
        this.listaProcesso = listaProcesso; // Lista de processos retirados do arquivo ordenados por ordem de chegada.
        escalonar();
    }

    public void escalonar() {
        Processo processo = null;

        do {

            verificaListaProcessos();

            if (flag == false) {
                processo = menorTempoExecucao();
                if (processo != null) {
                    flag = true;
                }
            }

            while (flag) {
                verificaListaProcessos();
                System.out.println("Executando processo " + processo.getId());
                executar(processo);
                tempo++;
            }

            // imprimir(listaPronto);
            tempo++;
        } while (!(listaProcesso.isEmpty()) && !(listaPronto.isEmpty())); // ERRADO AQUI <<<<<<<<<<<<<<<<<<<<<<<<<

    }

    public Processo menorTempoExecucao() {
        int duracao = 99999999;
        Processo p = null;

        for (int i = 0; i < this.listaPronto.size(); i++) {
            if (listaPronto.get(i).getDuracao() < duracao) {
                p = listaPronto.get(i);
            }

        }

        return p;
    }

    public void executar(Processo p) {
        if (p.getTipo() == Tipo.Sistema) {
            for (int i = listaBloqueado.size(); i > 0; i--) {
                listaPronto.add(listaBloqueado.getFirst());
                listaBloqueado.removeFirst();
            }

        }

        if (!p.getListaES().isEmpty()) {
            if (tempo == p.getListaES().getFirst()) {
                listaBloqueado.add(p);
                listaPronto.remove(p);
            }
        }

        if (p.getDuracao() > 0) {
            flag = true;
            p.setDuracao(p.getDuracao() - 1);
            if (p.getDuracao() == 0) {
                listaPronto.remove(p);
                System.out.println("Processo " + p.getId() + " terminou");
                flag = false;
            }
        }
    }

    public void verificaListaProcessos() {
        if (!listaProcesso.isEmpty()) {
            if (listaProcesso.getFirst().getTempo() == tempo) {

                listaPronto.add(listaProcesso.getFirst());
                System.out.println("Processo " + listaProcesso.getFirst().getId() + " chegou.");
                listaProcesso.removeFirst();

            }
        }
    }

    public void imprimir(LinkedList<Processo> x) {
        for (int i = 0; i < x.size(); i++) {
            System.out.println(x.get(i).toString());
        }
    }
}