import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Baraja {
    private Stack<Carta> cartas;

    public Baraja() {
        cartas = new Stack<>();
        for (Carta.Color color : Carta.Color.values()) {
            cartas.push(new Carta(color, 0));
            for (int num = 1; num <= 9; num++) {
                cartas.push(new Carta(color, num));
                cartas.push(new Carta(color, num));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cartas);
    }

    public Carta draw() {
        return cartas.isEmpty() ? null : cartas.pop();
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    public int size() {
        return cartas.size();
    }

    public void addCards(List<Carta> cartasToAdd) {
        cartas.addAll(cartasToAdd);
        shuffle();
    }
}