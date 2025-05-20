import Entitati.*;
import Servicii.UserService;
import Utile.CategorieProdus;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import javafx.geometry.Insets;
import Repositories.*;
import Utile.MetodaPlata;

public class Main extends Application {

    private Map<Restaurant, List<Produs>> cosCumparaturi = new HashMap<>();
    private ListView<String> listaCos = new ListView<>();
    private ListView<String> listaRecenzii = new ListView<>();
    private UserService userService = UserService.getInstance();
    private User utilizatorCurent;
    private Label labelUser = new Label("Niciun utilizator logat");
    private Button butonDelogare = new Button("Logare");
    private ListView<String> listaIstoricComenzi = new ListView<>();
    private ListView<String> listaComenziRestaurant = new ListView<>();

    private UserRepo userRepo = new UserRepo();
    private RestaurantRepo restaurantRepo = new RestaurantRepo();

    @Override
    public void start(Stage firstStage) {
        VBox loginPane = new VBox(10);
        loginPane.setPadding(new Insets(10));
        TextField emailField = new TextField();
        PasswordField parolaField = new PasswordField();
        Button butonLogin = new Button("Login");
        Button butonRegister = new Button("Creeaza cont");
        Label mesajLogin = new Label();

        loginPane.getChildren().addAll(new Label("Email:"), emailField,
                new Label("Parola:"), parolaField,
                butonLogin, butonRegister, mesajLogin);

        Scene loginScene = new Scene(loginPane, 300, 250);
        Stage loginStage = new Stage();
        loginStage.setTitle("Autentificare");
        loginStage.setScene(loginScene);

        butonLogin.setOnAction(e -> {
            String email = emailField.getText();
            String parola = parolaField.getText();
            User u = userService.autentificare(email, parola);
            if (u != null) {
                utilizatorCurent = u;
                labelUser.setText("Utilizator logat: " + u.getNume());
                butonDelogare.setText("Delogare");
                loginStage.close();
            } else {
                mesajLogin.setText("Email sau parola incorecta.");
            }
        });

        butonRegister.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Creeaza cont nou");
            dialog.setHeaderText("Introduceti numele complet:");
            dialog.setContentText("Nume:");
            Optional<String> rezultat = dialog.showAndWait();
            rezultat.ifPresent(nume -> {
                String email = emailField.getText();
                String parola = parolaField.getText();
                if (!email.isEmpty() && !parola.isEmpty()) {
                    utilizatorCurent = userService.creeazaUser(nume, email, parola);
                    labelUser.setText("Utilizator logat: " + utilizatorCurent.getNume());
                    butonDelogare.setText("Delogare");
                    loginStage.close();
                } else {
                    mesajLogin.setText("Email si parola sunt obligatorii!");
                }
            });
        });

        butonDelogare.setOnAction(e -> {
            if (utilizatorCurent != null) {
                utilizatorCurent = null;
                labelUser.setText("Niciun utilizator logat");
                butonDelogare.setText("Logare");
            }
            loginStage.show();
        });


        ComboBox<Restaurant> restaurantCombo = new ComboBox<>();
        restaurantCombo.getItems().addAll(restaurantRepo.getAll());
        restaurantCombo.setPromptText("Alege restaurantul");


        ComboBox<CategorieProdus> categorieCombo = new ComboBox<>();
        categorieCombo.getItems().addAll(CategorieProdus.values());
        categorieCombo.setPromptText("Filtreaza dupa categorie");

        ListView<Produs> listaProduse = new ListView<>();

        restaurantCombo.setOnAction(e -> {
            Restaurant selectedRestaurant = restaurantCombo.getSelectionModel().getSelectedItem();
            actualizeazaListaProduse(restaurantCombo, categorieCombo, listaProduse);
            actualizeazaListaRecenzii(selectedRestaurant);
            actualizeazaComenziRestaurant(selectedRestaurant);
        });


        categorieCombo.setOnAction(e -> {
            actualizeazaListaProduse(restaurantCombo, categorieCombo, listaProduse);
        });

        Button butonIstoric = new Button("Istoric Comenzi");
        butonIstoric.setOnAction(e -> {
            if (utilizatorCurent != null) {
                actualizeazaIstoricComenzi();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Istoric Comenzi");
                alert.setHeaderText("Comenzile tale:");
                ListView<String> listaPopup = new ListView<>();
                listaPopup.getItems().addAll(listaIstoricComenzi.getItems());
                listaPopup.setMaxHeight(200);

                DialogPane pane = alert.getDialogPane();
                pane.setContent(listaPopup);
                alert.showAndWait();
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Trebuie sa fii logat pentru a vedea istoricul comenzilor!");
                alerta.showAndWait();
            }
        });


        Button adaugaInCos = new Button("Adauga in cos");
        adaugaInCos.setOnAction(e -> {
            Restaurant selectedRestaurant = restaurantCombo.getSelectionModel().getSelectedItem();
            Produs selectedProdus = listaProduse.getSelectionModel().getSelectedItem();
            if (utilizatorCurent != null && selectedRestaurant != null && selectedProdus != null) {
                utilizatorCurent.adaugaInCos(selectedRestaurant, selectedProdus);
                actualizeazaCos();
            }
        });


        Button adaugaRecenzie = new Button("Adauga recenzie");
        adaugaRecenzie.setOnAction(e -> {
            Restaurant selectedRestaurant = restaurantCombo.getSelectionModel().getSelectedItem();
            if (selectedRestaurant != null && utilizatorCurent != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Adaugare recenzie");
                dialog.setHeaderText("Scrie o recenzie pentru " + selectedRestaurant.getNume());
                dialog.setContentText("Recenzie:");

                Optional<String> rezultat = dialog.showAndWait();
                rezultat.ifPresent(text -> {
                    if (!text.trim().isEmpty()) {
                        Recenzie recenzie = new Recenzie(utilizatorCurent, text, selectedRestaurant);
                        selectedRestaurant.adaugaRecenzie(recenzie);
                        actualizeazaListaRecenzii(selectedRestaurant);
                    }
                });
            } else {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Trebuie sa fii logat si sa selectezi un restaurant pentru a adauga o recenzie.");
                alerta.showAndWait();
            }
        });


        Button finalizeazaComanda = new Button("Plaseaza comanda");
        finalizeazaComanda.setOnAction(e -> {
            if (utilizatorCurent == null) {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Trebuie sa fii logat pentru a plasa o comanda!");
                alerta.showAndWait();
                return;
            }

            TextInputDialog stradaDialog = new TextInputDialog();
            stradaDialog.setTitle("Introducere Adresa");
            stradaDialog.setHeaderText("Introduceti strada:");
            stradaDialog.setContentText("Strada:");

            Optional<String> stradaRezultat = stradaDialog.showAndWait();
            if (stradaRezultat.isPresent()) {
                TextInputDialog orasDialog = new TextInputDialog();
                orasDialog.setTitle("Introducere Adresa");
                orasDialog.setHeaderText("Introduceti orasul:");
                orasDialog.setContentText("Oras:");

                Optional<String> orasRezultat = orasDialog.showAndWait();
                if (orasRezultat.isPresent()) {
                    TextInputDialog codPostalDialog = new TextInputDialog();
                    codPostalDialog.setTitle("Introducere Adresa");
                    codPostalDialog.setHeaderText("Introduceti codul postal:");
                    codPostalDialog.setContentText("Cod postal:");

                    Optional<String> codPostalRezultat = codPostalDialog.showAndWait();
                    if (codPostalRezultat.isPresent()) {

                        Adresa adresa = new Adresa(stradaRezultat.get(), orasRezultat.get(), codPostalRezultat.get());

                        ChoiceDialog<MetodaPlata> dialogPlata =
                                new ChoiceDialog<>(MetodaPlata.CARD, MetodaPlata.values());
                        dialogPlata.setTitle("Metoda de plata");
                        dialogPlata.setHeaderText("Selectati metoda de plata:");
                        Optional<MetodaPlata> metodaAleasa = dialogPlata.showAndWait();
                        if (!metodaAleasa.isPresent()) return;

                        SoferRepo soferi= new SoferRepo();
                        utilizatorCurent.plaseazaComanda(adresa, soferi, metodaAleasa.get());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Comanda plasata");
                        alert.setHeaderText(null);
                        alert.setContentText("Comanda a fost trimisa cu succes, " + utilizatorCurent.getNume() + "!");
                        alert.showAndWait();

                        actualizeazaCos();
                        actualizeazaComenziRestaurant(restaurantCombo.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });



        VBox sectiuneRestaurante = new VBox(10,
                new Label("Alege restaurant:"),
                restaurantCombo,
                new Label("Filtru categorie:"),
                categorieCombo,
                new Label("Meniu:"),
                listaProduse,
                adaugaInCos,
                adaugaRecenzie
        );

        VBox sectiuneRecenzii = new VBox(10,
                new Label("Recenzii:"),
                listaRecenzii,
                new Label ("Comenzi plasate la acest restaurant:"),
                listaComenziRestaurant

        );

        VBox sectiuneCos = new VBox(10,
                new Label("Cos de cumparaturi:"),
                listaCos,
                finalizeazaComanda
        );

        VBox sectiuneUser = new VBox(10,
                labelUser,
                butonDelogare,
                butonIstoric
        );

        HBox layout = new HBox(20, sectiuneUser, sectiuneRestaurante, sectiuneRecenzii, sectiuneCos);
        layout.setPadding(new Insets(10));

        Scene scenaPrincipala = new Scene(layout, 1050, 500);
        firstStage.setTitle("Food Delivery - Demo");
        firstStage.setScene(scenaPrincipala);
        firstStage.show();

        loginStage.show();
    }

    private void actualizeazaListaProduse(ComboBox<Restaurant> restaurantCombo,
                                          ComboBox<CategorieProdus> categorieCombo,
                                          ListView<Produs> listaProduse) {
        Restaurant selectedRestaurant = restaurantCombo.getSelectionModel().getSelectedItem();
        CategorieProdus selectedCategorie = categorieCombo.getSelectionModel().getSelectedItem();

        if (selectedRestaurant != null) {
            if (selectedCategorie != null) {
                listaProduse.getItems().setAll(selectedRestaurant.getProdusDinCategorie(selectedCategorie));
            } else {
                listaProduse.getItems().setAll(selectedRestaurant.getMeniu());
            }
        }
    }

    private void actualizeazaListaRecenzii(Restaurant restaurant) {
        if (restaurant != null) {
            List<String> recenziiFormatate = new ArrayList<>();
            for (Recenzie recenzie : restaurant.getRecenzii()) {
                recenziiFormatate.add(recenzie.toString());
            }
            listaRecenzii.getItems().setAll(recenziiFormatate);
        } else {
            listaRecenzii.getItems().clear();
        }
    }

    private void actualizeazaCos() {
        List<String> continutCos = new ArrayList<>();
        if (utilizatorCurent != null) {
            Map<Restaurant, List<Produs>> cos = utilizatorCurent.getCosCumparaturi();
            for (Map.Entry<Restaurant, List<Produs>> entry : cos.entrySet()) {
                String numeRestaurant = entry.getKey().getNume();
                for (Produs p : entry.getValue()) {
                    continutCos.add(numeRestaurant + ": " + p.toString());
                }
            }
        }
        listaCos.getItems().setAll(continutCos);
    }
    private void actualizeazaComenziRestaurant(Restaurant restaurant) {
        if (restaurant != null) {
            List<String> comenziFormatate = new ArrayList<>();
            for (Comanda comanda : restaurant.getComenzi()) {
                comenziFormatate.add(comanda.toString());
            }
            listaComenziRestaurant.getItems().setAll(comenziFormatate);
        } else {
            listaComenziRestaurant.getItems().clear();
        }
    }


    private void actualizeazaIstoricComenzi() {
        List<String> comenziFormatate = new ArrayList<>();
        if (utilizatorCurent != null) {
            for (Comanda comanda : utilizatorCurent.getIstoricComenzi()) {
                comenziFormatate.add(comanda.toString());
            }
        }
        listaIstoricComenzi.getItems().setAll(comenziFormatate);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
