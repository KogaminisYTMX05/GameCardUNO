package juegouno;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<Card> cartas;

    public Deck() {
        cartas = new Stack<>();
        for (Card.Color color : Card.Color.values()) {
            cartas.push(new Card(color, 0));
            for (int num = 1; num <= 9; num++) {
                cartas.push(new Card(color, num));
                cartas.push(new Card(color, num));
            }
        }
        for (Card.Color color : Card.Color.values()) {
            for (Card.Accion accion : Card.Accion.values()) {
                cartas.push(new Card(color, accion));
                cartas.push(new Card(color, accion));
            }
        }
        for (int i = 0; i < 4; i++) {
            cartas.push(new Card(Card.Comodin.CAMBIO_COLOR));
            cartas.push(new Card(Card.Comodin.ROBA4));
        }
        barajar();
    }

    public void barajar() { Collections.shuffle(cartas); }
    public Card robar() { return cartas.isEmpty() ? null : cartas.pop(); }
    public boolean estaVacia() { return cartas.isEmpty(); }
    public int tamaño() { return cartas.size(); }

    public void agregarCartas(List<Card> cartasParaAgregar) {
        cartas.addAll(cartasParaAgregar);
        barajar();
    }
}