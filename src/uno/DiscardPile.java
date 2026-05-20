package uno;

import java.util.Stack;

/**
 * Representa la pila de descartes (montón de cartas jugadas).
 * Permite añadir cartas y consultar la última carta jugada.
 */
public class DiscardPile {

    /** Pila que almacena las cartas en orden de juego. */
    private Stack<Card> pile;

    /**
     * Crea una pila de descartes vacía.
     */
    public DiscardPile() {

        pile = new Stack<>();
    }

    /**
     * Añade una carta al tope de la pila de descartes.
     *
     * @param card la carta a descartar
     */
    public void addCard(Card card) {

        pile.push(card);
    }

    /**
     * Obtiene la carta que está actualmente en la cima de la pila (sin eliminarla).
     *
     * @return la carta superior, o {@code null} si la pila está vacía
     */
    public Card getTopCard() {

        if(pile.isEmpty()) {

            return null;
        }

        return pile.peek();
    }
}