import java.util.ArrayList;
import java.util.List;

/**
 * Pila de descarte del juego.
 * <p>
 * Almacena las cartas jugadas en orden y mantiene el color efectivo
 * que se debe considerar cuando la última carta es un comodín.
 * </p>
 */
public class DiscardPile {
    private final List<Card> pila;
    private Card.Color colorActual;

    /**
     * Crea una pila de descarte vacía sin color definido.
     */
    public DiscardPile() {
        this.pila = new ArrayList<>();
        this.colorActual = null;
    }

    /**
     * Agrega una carta a la cima de la pila.
     * Si no es un comodín, su color se convierte en el color efectivo.
     *
     * @param carta carta que se acaba de jugar.
     */
    public void agregarCarta(Card carta) {
        pila.add(carta);
        if (carta.getTipo() != Card.Tipo.COMODIN) {
            colorActual = carta.getColor();
        }
    }

    /**
     * @return la carta en la cima de la pila.
     */
    public Card getCartaSuperior() {
        return pila.isEmpty() ? null : pila.get(pila.size() - 1);
    }

    /**
     * @return el color que debe respetarse en la siguiente jugada.
     */
    public Card.Color getColorEfectivo() {
        return colorActual;
    }

    /**
     * Fija manualmente el color efectivo. Se usa tras jugar un comodín.
     *
     * @param color nuevo color efectivo.
     */
    public void setColorEfectivo(Card.Color color) {
        this.colorActual = color;
    }

    /**
     * Extrae todas las cartas de la pila excepto la superior.
     * Se utiliza para rellenar el mazo sin perder la carta de referencia.
     *
     * @return lista de cartas extraídas (puede estar vacía).
     */
    public List<Card> extraerTodasMenosLaSuperior() {
        if (pila.size() <= 1) return new ArrayList<>();
        Card superior = pila.remove(pila.size() - 1);
        List<Card> resto = new ArrayList<>(pila);
        pila.clear();
        pila.add(superior);
        return resto;
    }

    /**
     * @return número de cartas en la pila.
     */
    public int tamaño() { return pila.size(); }
}