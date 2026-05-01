package juegouno;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private final List<Card> pila;
    private Card.Color colorActual;

    public DiscardPile() {
        this.pila = new ArrayList<>();
        this.colorActual = null;
    }

    public void agregarCarta(Card carta) {
        pila.add(carta);
        if (carta.getTipo() != Card.Tipo.COMODIN) {
            colorActual = carta.getColor();
        }
    }

    public Card getCartaSuperior() {
        return pila.isEmpty() ? null : pila.get(pila.size() - 1);
    }

    public Card.Color getColorEfectivo() { return colorActual; }
    public void setColorEfectivo(Card.Color color) { this.colorActual = color; }

    public List<Card> extraerTodasMenosLaSuperior() {
        if (pila.size() <= 1) return new ArrayList<>();
        Card superior = pila.remove(pila.size() - 1);
        List<Card> resto = new ArrayList<>(pila);
        pila.clear();
        pila.add(superior);
        return resto;
    }

    public int tamaño() { return pila.size(); }
}