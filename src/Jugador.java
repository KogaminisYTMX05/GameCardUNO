import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jugador {
    private String nombre;
    private List<Carta> mano;
    private boolean esHumano;
    
    public Jugador(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.esHumano = esHumano;
        this.mano = new ArrayList<>();
    }
    
    public void recibirCarta(Carta carta) {
        mano.add(carta);
    }
    
    public List<Carta> getMano() {
        return mano;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public boolean tieneCartas() {
        return !mano.isEmpty();
    }
    
    public Carta jugarTurno(Carta cartaSuperior, Baraja baraja) {
        if (esHumano) {
            return jugarTurnoHumano(cartaSuperior, baraja);
        } else {
            return jugarTurnoCPU(cartaSuperior, baraja);
        }
    }
    
    private Carta jugarTurnoHumano(Carta cartaSuperior, Baraja baraja) {
        Scanner scanner = new Scanner(System.in);
        mostrarMano();
        
        // Buscar cartas jugables
        List<Integer> indicesJugables = new ArrayList<>();
        for (int i = 0; i < mano.size(); i++) {
            if (mano.get(i).esJugable(cartaSuperior)) {
                indicesJugables.add(i);
            }
        }
        
        if (indicesJugables.isEmpty()) {
            System.out.println("No tienes cartas jugables. Debes tomar una carta.");
            Carta nuevaCarta = baraja.repartirCarta();
            System.out.println("Tomaste: " + nuevaCarta);
            mano.add(nuevaCarta);
            return null;
        }
        
        System.out.println("Cartas jugables: " + indicesJugables);
        System.out.print("Elige el indice de la carta a jugar (o -1 para tomar carta): ");
        int eleccion = scanner.nextInt();
        
        if (eleccion == -1) {
            Carta nuevaCarta = baraja.repartirCarta();
            System.out.println("Tomaste: " + nuevaCarta);
            mano.add(nuevaCarta);
            return null;
        }
        
        if (eleccion >= 0 && eleccion < mano.size()) {
            Carta jugada = mano.remove(eleccion);
            System.out.println(nombre + " juega: " + jugada);
            return jugada;
        }
        
        return null;
    }
    
    private Carta jugarTurnoCPU(Carta cartaSuperior, Baraja baraja) {
        System.out.println("\nTurno de " + nombre);
        
        // Buscar primera carta jugable
        for (int i = 0; i < mano.size(); i++) {
            if (mano.get(i).esJugable(cartaSuperior)) {
                Carta jugada = mano.remove(i);
                System.out.println(nombre + " juega: " + jugada);
                return jugada;
            }
        }
        
        // Si no hay cartas jugables, tomar una carta
        System.out.println(nombre + " no tiene cartas jugables. Toma una carta.");
        Carta nuevaCarta = baraja.repartirCarta();
        mano.add(nuevaCarta);
        return null;
    }
    
    private void mostrarMano() {
        System.out.println("\nMano de " + nombre + ":");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println(i + ": " + mano.get(i));
        }
    }
}