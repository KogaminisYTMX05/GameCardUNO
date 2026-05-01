import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Mazo principal del juego.
 * <p>
 * Contiene las 108 cartas iniciales y permite robar, barajar y rellenar
 * con cartas provenientes de la pila de descarte.
 * </p>
 */
public class Deck {
    private Stack<Card> cartas;

    /**
     * Crea el mazo completo con todas las cartas del juego y lo baraja.
     */
    public Deck() {
        cartas = new Stack<>();
        // Cartas numéricas
        for (Card.Color color : Card.Color.values()) {
            cartas.push(new Card(color, 0));
            for (int num = 1; num <= 9; num++) {
                cartas.push(new Card(color, num));
                cartas.push(new Card(color, num));
            }
        }
        // Cartas de acción
        for (Card.Color color : Card.Color.values()) {
            for (Card.Accion accion : Card.Accion.values()) {
                cartas.push(new Card(color, accion));
                cartas.push(new Card(color, accion));
            }
        }
        // Comodines
        for (int i = 0; i < 4; i++) {
            cartas.push(new Card(Card.Comodin.CAMBIO_COLOR));
            cartas.push(new Card(Card.Comodin.ROBA4));
        }
        barajar();
    }

    /**
     * Baraja el mazo actual aleatoriamente.
     */
    public void barajar() { Collections.shuffle(cartas); }

    /**
     * Roba la carta superior del mazo.
     *
     * @return la carta robada, o {@code null} si el mazo está vacío.
     */
    public Card robar() { return cartas.isEmpty() ? null : cartas.pop(); }

    /**
     * Verifica si el mazo se ha quedado sin cartas.
     *
     * @return {@code true} si no quedan cartas.
     */
    public boolean estaVacia() { return cartas.isEmpty(); }

    /**
     * Número de cartas que quedan en el mazo.
     *
     * @return cantidad actual de cartas.
     */
    public int tamaño() { return cartas.size(); }

    /**
     * Agrega una lista de cartas al mazo y luego lo baraja.
     * Se usa típicamente para reintegrar la pila de descarte.
     *
     * @param cartasParaAgregar lista de cartas que se añadirán.
     */
    public void agregarCartas(List<Card> cartasParaAgregar) {
        cartas.addAll(cartasParaAgregar);
        barajar();
    }
}