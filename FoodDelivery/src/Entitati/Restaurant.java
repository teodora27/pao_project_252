package Entitati;
import Utile.CategorieProdus;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String nume;
    private List<Produs> meniu;
    private List<Recenzie> recenzii;
    private List<Comanda> comenzi;

    public Restaurant(String nume, List<Produs> meniu) {
        this.nume = nume;
        this.meniu = meniu;
        this.recenzii = new ArrayList<Recenzie>();
        this.comenzi = new ArrayList<>();

    }

    public List<Comanda> getComenzi() {return comenzi;}

    public void adaugaRecenzie(Recenzie r) {
        recenzii.add(r);
    }

    public List<Recenzie> getRecenzii() {
        return recenzii;
    }

    public String getNume() { return nume; }
    public List<Produs> getMeniu() { return meniu; }
    public List<Produs> getProdusDinCategorie(CategorieProdus categorieProdus) {
        List<Produs> produse = new ArrayList<>();
        for (Produs produs : meniu) {
            if(produs.getCategorieProdus() == categorieProdus){
                produse.add(produs);
            }
        }
        return produse;
    }

    public void adaugaCoamnda(Comanda comanda) {
        comenzi.add(comanda);
    }

    @Override
    public String toString() {
        return nume;
    }
}
