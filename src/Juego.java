import java.util.List;
import java.util.Scanner;

public class Juego {
    private Baraja baraja;
    private Jugador jugadorHumano;
    private Jugador jugadorCPU;
    private Carta cartaActual;
    private boolean juegoTerminado;
    
    public Juego() {
        this.baraja = new Baraja();
        this.jugadorHumano = new Jugador("P1", true);
        this.jugadorCPU = new Jugador("CPU", false);
        this.juegoTerminado = false;
    }
    
    public void iniciarJuego() {
        System.out.println("\nBuena suerte a todos\n");
        
        // Repartir 7 cartas a cada jugador
        repartirCartasIniciales();
        
        // Colocar primera carta en el descarte
        cartaActual = baraja.repartirCarta();
        baraja.agregarADescarte(cartaActual);
        System.out.println("Carta inicial: " + cartaActual + "\n");
        
        // Comenzar el juego
        while (!juegoTerminado) {
            // Turno del humano
            turnoJugador(jugadorHumano);
            if (verificarGanador()) break;
            
            // Turno de la CPU
            turnoJugador(jugadorCPU);
            if (verificarGanador()) break;
        }
    }
    
    private void repartirCartasIniciales() {
        for (int i = 0; i < 7; i++) {
            jugadorHumano.recibirCarta(baraja.repartirCarta());
            jugadorCPU.recibirCarta(baraja.repartirCarta());
        }
    }
    
    private void turnoJugador(Jugador jugador) {
        if (juegoTerminado) return;
        
        System.out.println("\n--- Turno de " + jugador.getNombre() + " ---");
        System.out.println("Carta en mesa: " + cartaActual);
        
        Carta cartaJugada = jugador.jugarTurno(cartaActual, baraja);
        
        if (cartaJugada != null) {
            // Validar jugada
            if (cartaJugada.esJugable(cartaActual)) {
                cartaActual = cartaJugada;
                baraja.agregarADescarte(cartaJugada);
                System.out.println("¡Jugada valida!");
            } else {
                System.out.println("¡Error! Jugada invalida - La carta no deberia ser jugable");
                // Devolver la carta al jugador (esto no debería pasar)
                jugador.recibirCarta(cartaJugada);
            }
        }
    }
    
    private boolean verificarGanador() {
        if (!jugadorHumano.tieneCartas()) {
            System.out.println("\n¡Congratulations, eres el vencedor!");
            juegoTerminado = true;
            return true;
        } else if (!jugadorCPU.tieneCartas()) {
            System.out.println("\n¡OH NO, la CPU te ha vencido! Suerte pa'l proximo intento.");
            juegoTerminado = true;
            return true;
        }
        return false;
    }
    
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }
}