package juegouno;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nombre;
    private final List<Card> mano;
    private final boolean esHumano;
    private boolean dijoUno;

    public Player(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.esHumano = esHumano;
        this.dijoUno = false;
    }

    public String getNombre() { return nombre; }
    public List<Card> getMano() { return mano; }
    public int getTamañoMano() { return mano.size(); }
    public boolean esHumano() { return esHumano; }
    public boolean haDichoUno() { return dijoUno; }
    public void setDijoUno(boolean dijo) { this.dijoUno = dijo; }

    public void agregarCarta(Card carta) { mano.add(carta); }

    public Card jugarCarta(int indice) {
        return (indice >= 0 && indice < mano.size()) ? mano.remove(indice) : null;
    }

    public boolean tieneCartaJugable(Card cartaMesa, Card.Color colorActual) {
        return mano.stream().anyMatch(c -> c.esJugable(cartaMesa, colorActual));
    }

    public Card getPrimeraCartaJugable(Card cartaMesa, Card.Color colorActual) {
        return mano.stream().filter(c -> c.esJugable(cartaMesa, colorActual)).findFirst().orElse(null);
    }

    public void mostrarMano() {
        System.out.println("Cartas de " + nombre + ":");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println("  [" + i + "] " + mano.get(i));
        }
    }
}