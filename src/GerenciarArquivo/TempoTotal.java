/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GerenciarArquivo;

import Processos.Processo;
import java.util.LinkedList;

/**
 *
 * @author elivelton
 */
public class TempoTotal {
    public int total=0;
    public TempoTotal(LinkedList<Processo> processo) {
        for(int i=0; i < processo.size(); i++){
            total += processo.get(i).getTempo();
        }
        
        System.out.println("Tempo Total: "+total);
    }
}