package Servicii;
import Entitati.User;
import java.util.*;

public class UserService {
    private static UserService instance = null;
    private List<User> utilizatori;
    private int currentId = 1;

    public UserService() {
        utilizatori = new ArrayList<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User creeazaUser(String nume, String email, String parola) {
        User user = new User(currentId++, nume, email, parola);
        utilizatori.add(user);
        return user;
    }

    public User autentificare(String email, String parola) {
        for (User user : utilizatori) {
            if (user.getEmail().equals(email) && user.getParola().equals(parola)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getTotiUtilizatorii() {
        return utilizatori;
    }
}
