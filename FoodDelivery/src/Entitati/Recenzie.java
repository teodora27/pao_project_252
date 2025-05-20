package Entitati;

public class Recenzie {
    private User user;
    private String text;
    private Restaurant restaurant;

    public Recenzie(User user, String text, Restaurant restaurant) {
        this.user = user;
        this.text = text;
        this.restaurant = restaurant;
    }

    public User getUser() { return user; }
    public String getText() { return text; }
    public Restaurant getRestaurant() { return restaurant; }

    @Override
    public String toString() {
        return user.getNume() + ": \"" + text + "\"";
    }
}
