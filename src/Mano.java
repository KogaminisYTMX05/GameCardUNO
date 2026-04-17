import java.util.ArrayList;
import java.util.List;

public class Mano {
    private String nombre;          // identificador de la mano (Humano o CPU)
    private List<Carta> cartas;      // las cartas que tiene esta mano

    public Mano(String nombre) {
        this.nombre = nombre;
        this.cartas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public Carta jugarCarta(int indice) {
        return (indice >= 0 && indice < cartas.size()) ? cartas.remove(indice) : null;
    }

    public int getNumeroCartas() {
        return cartas.size();
    }

    public boolean tieneCartaJugable(Carta top) {
        for (Carta c : cartas) {
            if (c.esJugable(top)) return true;
        }
        return false;
    }

    public Carta getPrimeraCartaJugable(Carta top) {
        for (Carta c : cartas) {
            if (c.esJugable(top)) return c;
        }
        return null;
    }

    public void mostrarCartas() {
        System.out.println("Cartas de " + nombre + ":");
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println((i + 1) + ": " + cartas.get(i));
        }
    }
}