package Repositories;

import Entitati.User;
import Servicii.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private List<User> utilizatori;

    public UserRepo() {
        utilizatori = new ArrayList<>();

        utilizatori.add(new User(1, "Ana Popescu", "ana@email.com", "parola1"));
        utilizatori.add(new User(2, "Ion Ionescu", "ion@email.com", "parola2"));
        utilizatori.add(new User(3, "Maria Dobre", "maria@email.com", "parola3"));
        utilizatori.add(new User(4, "Vlad Mihai", "vlad@email.com", "parola4"));
        utilizatori.add(new User(5, "Elena Radu", "elena@email.com", "parola5"));

        UserService userService = UserService.getInstance();
        for (User u : utilizatori) {
            userService.getTotiUtilizatorii().add(u);
        }
    }

    public List<User> getAll() {
        return utilizatori;
    }
}
