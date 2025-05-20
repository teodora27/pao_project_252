package Entitati;

import Utile.CategorieProdus;

public class Produs {
    private String nume;
    private double pret;
    private CategorieProdus categorieProdus;

    public Produs(String nume, double pret, CategorieProdus categorieProdus) {
        this.nume = nume;
        this.pret = pret;
        this.categorieProdus = categorieProdus;
    }

    public String getNume() { return nume; }
    public double getPret() { return pret; }
    public CategorieProdus getCategorieProdus() { return categorieProdus; }
    @Override
    public String toString() {
        return nume + " - " + pret + " lei";
    }
}
