package br.edu.ifpe.tads.pdm.projeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.interfaces.AddBanheiroObserver;

public class AddBanheiroSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<AddBanheiroObserver> observers = new ArrayList<>();
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        notifyAllObservers();
    }

    public void attach(AddBanheiroObserver observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (AddBanheiroObserver observer : observers) {
            observer.addBanheiro();
        }
    }

}
