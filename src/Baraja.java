import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {
    private List<Carta> cartas;
    private List<Carta> descarte;
    private String[] colores = {"rojo", "azul", "verde", "amarillo"};
    
    public Baraja() {
        cartas = new ArrayList<>();
        descarte = new ArrayList<>();
        inicializarBaraja();
    }
    
    private void inicializarBaraja() {
        // 76 cartas: 4 colores × (1 carta del 0 + 2 cartas del 1-9) = 4 × 19 = 76
        for (String color : colores) {
            // Una carta del 0 por color
            cartas.add(new Carta(color, 0));
            
            // Dos cartas del 1 al 9 por color
            for (int numero = 1; numero <= 9; numero++) {
                cartas.add(new Carta(color, numero));
                cartas.add(new Carta(color, numero));
            }
        }
        barajar();
    }
    
    public void barajar() {
        Collections.shuffle(cartas);
    }
    
    public Carta repartirCarta() {
        if (cartas.isEmpty()) {
            recargarBaraja();
        }
        return cartas.remove(0);
    }
    
    private void recargarBaraja() {
        if (descarte.size() > 1) {
            Carta superior = descarte.remove(descarte.size() - 1);
            cartas.addAll(descarte);
            descarte.clear();
            descarte.add(superior);
            barajar();
        }
    }
    
    public void agregarADescarte(Carta carta) {
        descarte.add(carta);
    }
    
    public Carta getCartaSuperior() {
        if (descarte.isEmpty()) {
            Carta primera = repartirCarta();
            descarte.add(primera);
        }
        return descarte.get(descarte.size() - 1);
    }
    
    public boolean isEmpty() {
        return cartas.isEmpty() && descarte.isEmpty();
    }
}