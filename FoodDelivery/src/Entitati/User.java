package Entitati;

import Repositories.SoferRepo;
import Utile.MetodaPlata;

import java.util.*;

public class User extends Persoana{
    private int id;
    private String email;
    private String parola;
    private Map<Restaurant, List<Produs>> cosCumparaturi;
    private List<Comanda> istoricComenzi=new ArrayList<>();

    public User(int id, String nume, String email, String parola) {
        super(nume);
        this.id = id;
        this.email = email;
        this.parola = parola;
        this.cosCumparaturi = new HashMap<>();
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getParola() { return parola; }
    public  Map<Restaurant, List<Produs>> getCosCumparaturi() { return cosCumparaturi; }

    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setParola(String parola) { this.parola = parola; }

    public void adaugaInCos(Restaurant restaurant, Produs produs) {
        cosCumparaturi.putIfAbsent(restaurant, new ArrayList<>());
        cosCumparaturi.get(restaurant).add(produs);
    }
    public void plaseazaComanda(Adresa adresa, SoferRepo soferRepo, MetodaPlata metodaPlata) {
        if(!cosCumparaturi.isEmpty()) {
            Comanda comandaClient = new Comanda(this, adresa, metodaPlata);

            List<Sofer> soferiDisponibili = soferRepo.getAll();
            Sofer soferAles=null;
            if (!soferiDisponibili.isEmpty()) {
                Random random = new Random();
                soferAles = soferiDisponibili.get(random.nextInt(soferiDisponibili.size()));
                comandaClient.setSofer(soferAles);
            }

            for (Map.Entry<Restaurant, List<Produs>> restaurant : cosCumparaturi.entrySet()) {
                Comanda comandaRestaurant = new Comanda(this, adresa, metodaPlata);
                if (soferAles != null) {
                    comandaRestaurant.setSofer(soferAles);
                }

                for (Produs produs : restaurant.getValue()) {
                    comandaClient.adaugaProdus(restaurant.getKey(), produs);
                    comandaRestaurant.adaugaProdus(restaurant.getKey(), produs);
                }
                Restaurant restaurantKey = restaurant.getKey();
                restaurantKey.adaugaCoamnda(comandaRestaurant);
            }

            istoricComenzi.add(comandaClient);
            this.cosCumparaturi = new HashMap<>();
        }
    }

    public List<Comanda> getIstoricComenzi() { return istoricComenzi; }

    @Override
    public String toString() {
        return super.getNume() + " (" + email + ")";
    }
}