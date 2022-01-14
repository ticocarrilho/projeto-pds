package br.edu.ifpe.tads.pdm.projeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.projeto.interfaces.AddBanheiroObserver;
import br.edu.ifpe.tads.pdm.projeto.models.Banheiro;

public class AddBanheiroSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<AddBanheiroObserver> observers = new ArrayList<>();
    private Banheiro banheiro;

    public Banheiro getBanheiro() {
        return banheiro;
    }

    public void setBanheiro(String local, String tipo) {
        this.banheiro = new Banheiro(local, tipo);
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
