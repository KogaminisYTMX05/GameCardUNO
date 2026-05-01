import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un jugador (humano o bot) con su mano de cartas y estado de "UNO".
 */
public class Player {
    private final String nombre;
    private final List<Card> mano;
    private final boolean esHumano;
    private boolean dijoUno;

    /**
     * Construye un jugador.
     *
     * @param nombre   nombre visible del jugador.
     * @param esHumano {@code true} si es controlado por una persona.
     */
    public Player(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.esHumano = esHumano;
        this.dijoUno = false;
    }

    /** @return nombre del jugador. */
    public String getNombre() { return nombre; }

    /** @return lista de cartas en la mano. */
    public List<Card> getMano() { return mano; }

    /** @return cantidad de cartas en la mano. */
    public int getTamañoMano() { return mano.size(); }

    /** @return {@code true} si es el jugador humano. */
    public boolean esHumano() { return esHumano; }

    /** @return {@code true} si el jugador declaró UNO en su turno. */
    public boolean haDichoUno() { return dijoUno; }

    /**
     * Establece si el jugador ha dicho UNO.
     *
     * @param dijo {@code true} para marcarlo.
     */
    public void setDijoUno(boolean dijo) { this.dijoUno = dijo; }

    /**
     * Añade una carta a la mano del jugador.
     *
     * @param carta carta a agregar.
     */
    public void agregarCarta(Card carta) { mano.add(carta); }

    /**
     * Juega (quita) la carta en la posición indicada.
     *
     * @param indice posición de la carta en la mano.
     * @return la carta jugada, o {@code null} si el índice no es válido.
     */
    public Card jugarCarta(int indice) {
        return (indice >= 0 && indice < mano.size()) ? mano.remove(indice) : null;
    }

    /**
     * Comprueba si el jugador tiene al menos una carta jugable.
     *
     * @param cartaMesa   carta superior de la pila de descarte.
     * @param colorActual color efectivo en la mesa.
     * @return {@code true} si existe una carta jugable.
     */
    public boolean tieneCartaJugable(Card cartaMesa, Card.Color colorActual) {
        return mano.stream().anyMatch(c -> c.esJugable(cartaMesa, colorActual));
    }

    /**
     * Obtiene la primera carta jugable de la mano.
     *
     * @param cartaMesa   carta superior de la pila de descarte.
     * @param colorActual color efectivo en la mesa.
     * @return la carta jugable, o {@code null} si no hay ninguna.
     */
    public Card getPrimeraCartaJugable(Card cartaMesa, Card.Color colorActual) {
        return mano.stream().filter(c -> c.esJugable(cartaMesa, colorActual)).findFirst().orElse(null);
    }

    /**
     * Muestra por consola las cartas actuales del jugador con su índice.
     */
    public void mostrarMano() {
        System.out.println("Cartas de " + nombre + ":");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println("  [" + i + "] " + mano.get(i));
        }
    }
}