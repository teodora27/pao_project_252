package Entitati;

public class Sofer extends Persoana {
    private String numarTelefon;
    private String tipVehicul;

    public Sofer(String nume, String numarTelefon, String tipVehicul) {
        super(nume);
        this.numarTelefon = numarTelefon;
        this.tipVehicul = tipVehicul;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    public String getTipVehicul() {
        return tipVehicul;
    }

    public void setTipVehicul(String tipVehicul) {
        this.tipVehicul = tipVehicul;
    }

    @Override
    public String toString() {
        return "Sofer: " + super.getNume() + ", Telefon: " + numarTelefon + ", Vehicul: " + tipVehicul;
    }
}
