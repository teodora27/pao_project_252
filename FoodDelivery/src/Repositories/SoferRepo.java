package Repositories;

import Entitati.Sofer;
import java.util.ArrayList;
import java.util.List;

public class SoferRepo {

    private List<Sofer> soferi;

    public SoferRepo() {
        soferi = new ArrayList<>();
        adaugaSoferiStandard();
    }

    private void adaugaSoferiStandard() {
        soferi.add(new Sofer("Ion Popescu", "0712345678", "Masina"));
        soferi.add(new Sofer("Maria Ionescu", "0723456789", "Furgon"));
        soferi.add(new Sofer("Alexandru Vasile", "0734567890", "Masina"));
        soferi.add(new Sofer("Elena Georgescu", "0745678901", "Motocicleta"));
        soferi.add(new Sofer("Mihai Dobre", "0756789012", "Masina"));
    }

    public List<Sofer> getAll() {
        return new ArrayList<>(soferi);
    }

    public void adaugaSofer(Sofer sofer) {
        soferi.add(sofer);
    }

    public Sofer findByNume(String nume) {
        for (Sofer sofer : soferi) {
            if (sofer.getNume().equalsIgnoreCase(nume)) {
                return sofer;
            }
        }
        return null;
    }
}
