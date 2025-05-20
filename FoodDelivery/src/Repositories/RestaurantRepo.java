package Repositories;

import Entitati.Restaurant;
import Entitati.Produs;
import Utile.CategorieProdus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantRepo {
    private List<Restaurant> restaurante;

    public RestaurantRepo() {
        restaurante = new ArrayList<>();

        restaurante.add(new Restaurant("La Bunica", Arrays.asList(
                new Produs("Ciorba de burta", 22.0, CategorieProdus.Pranz),
                new Produs("Sarmale cu mamaliga", 30.0, CategorieProdus.Cina),
                new Produs("Papanasi", 18.0, CategorieProdus.Desert)
        )));

        restaurante.add(new Restaurant("Fast&Yummy", Arrays.asList(
                new Produs("Cheeseburger", 28.0, CategorieProdus.Pranz),
                new Produs("Cartofi prajiti", 12.0, CategorieProdus.Pranz),
                new Produs("Milkshake", 15.0, CategorieProdus.Desert)
        )));

        restaurante.add(new Restaurant("Casa Gustului", Arrays.asList(
                new Produs("Friptura de vita", 45.0, CategorieProdus.Cina),
                new Produs("Salata Caesar", 26.0, CategorieProdus.Pranz),
                new Produs("Tiramisu", 20.0, CategorieProdus.Desert)
        )));

        restaurante.add(new Restaurant("Pizza Express", Arrays.asList(
                new Produs("Pizza Margherita", 24.0, CategorieProdus.Cina),
                new Produs("Pizza Diavola", 29.0, CategorieProdus.Cina),
                new Produs("Pizza Quattro Stagioni", 31.0, CategorieProdus.Cina)
        )));

        restaurante.add(new Restaurant("Burger Town", Arrays.asList(
                new Produs("Double Beef Burger", 35.0, CategorieProdus.Pranz),
                new Produs("Onion Rings", 14.0, CategorieProdus.Cina),
                new Produs("Cola 0.5L", 8.0, CategorieProdus.Bauturi)
        )));
    }

    public List<Restaurant> getAll() {
        return restaurante;
    }
}
