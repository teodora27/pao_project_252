package Entitati;

import Utile.MetodaPlata;

import java.time.LocalDateTime;
import java.util.*;

public class Comanda {
    private static int nextId = 1;
    private int id;
    private User client;
    private Map<Restaurant, List<Produs>> produseComandate;
    private LocalDateTime dataComanda;
    private Adresa adresaLivrare;
    private Sofer sofer;
    private MetodaPlata metodaPlata;

    public Comanda(User client, Adresa adresaLivrare, MetodaPlata metodaPlata) {
        this.id = nextId++;
        this.client = client;
        this.adresaLivrare = adresaLivrare;
        this.produseComandate = new HashMap<>();
        this.dataComanda = LocalDateTime.now();
        this.metodaPlata = metodaPlata;
    }

    public void setMetodaPlata(MetodaPlata metodaPlata) {
        this.metodaPlata = metodaPlata;
    }

    public void setSofer(Sofer sofer) {
        this.sofer = sofer;
    }

    public Sofer getSofer() {
        return sofer;
    }

    public Adresa getAdresaLivrare() {
        return adresaLivrare;
    }

    public void adaugaProdus(Restaurant restaurant, Produs produs) {
        produseComandate.putIfAbsent(restaurant, new ArrayList<>());
        produseComandate.get(restaurant).add(produs);
    }

    public double calculeazaTotal() {
        double total = 0;
        for (List<Produs> lista : produseComandate.values()) {
            for (Produs p : lista) {
                total += p.getPret();
            }
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public Map<Restaurant, List<Produs>> getProduseComandate() {
        return produseComandate;
    }

    public LocalDateTime getDataComanda() {
        return dataComanda;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comanda pentru ")
                .append(client.getNume())
                .append(" - Total: ").append(String.format("%.2f", calculeazaTotal())).append(" lei")
                .append(" - Data: ").append(dataComanda)
                .append(" - Plata: ").append(metodaPlata)
                .append("\n")
                .append("Livrata de: ").append(sofer != null ? sofer.getNume() : "N/A")
                .append("\n");

        for (Map.Entry<Restaurant, List<Produs>> entry : produseComandate.entrySet()) {
            sb.append("  Restaurant: ").append(entry.getKey().getNume()).append("\n");
            for (Produs produs : entry.getValue()) {
                sb.append("    - ").append(produs.getNume())
                        .append(" (").append(String.format("%.2f", produs.getPret())).append(" lei)")
                        .append("\n");
            }
        }
        return sb.toString();
    }
}
