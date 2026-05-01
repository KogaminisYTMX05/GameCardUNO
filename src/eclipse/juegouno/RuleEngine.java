package juegouno;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RuleEngine {
    private final Deck mazo;
    private final DiscardPile pilaDescarte;
    private final TurnManager turnManager;
    private final Scanner scanner;

    public RuleEngine(Deck mazo, DiscardPile pilaDescarte, TurnManager turnManager, Scanner scanner) {
        this.mazo = mazo;
        this.pilaDescarte = pilaDescarte;
        this.turnManager = turnManager;
        this.scanner = scanner;
    }

    public boolean esJugadaValida(Card carta, Player jugador) {
        Card superior = pilaDescarte.getCartaSuperior();
        Card.Color colorEfectivo = pilaDescarte.getColorEfectivo();

        if (!carta.esJugable(superior, colorEfectivo)) {
            return false;
        }

        if (carta.getTipo() == Card.Tipo.COMODIN && carta.getComodin() == Card.Comodin.ROBA4) {
            boolean tieneColorCoincidente = jugador.getMano().stream()
                    .anyMatch(c -> c.getTipo() != Card.Tipo.COMODIN && c.getColor() == colorEfectivo);
            if (tieneColorCoincidente) {
                return false;
            }
        }
        return true;
    }

    public void aplicarEfectoCarta(Card carta, Player jugadorActual) {
        switch (carta.getTipo()) {
            case ACCION:
                aplicarEfectoAccion(carta, jugadorActual);
                break;
            case COMODIN:
                aplicarEfectoComodin(carta, jugadorActual);
                break;
            default:
                pilaDescarte.setColorEfectivo(carta.getColor());
                break;
        }
    }

    public Card robarCarta() {
        if (mazo.estaVacia()) {
            if (pilaDescarte.tamaño() <= 1) {
                System.out.println("No quedan cartas para robar.");
                return null;
            }
            List<Card> resto = pilaDescarte.extraerTodasMenosLaSuperior();
            mazo.agregarCartas(resto);
            System.out.println("Mazo rellenado con descarte.");
        }
        return mazo.robar();
    }

    public void aplicarPenalizacionUno(Player jugador) {
        if (jugador.getTamañoMano() == 1 && !jugador.haDichoUno()) {
            System.out.println("El jugador " + jugador.getNombre() + " no dijo UNO! Penalizacion: roba 2 cartas.");
            robarCartas(jugador, 2);
        }
        jugador.setDijoUno(false);
    }

    private void aplicarEfectoAccion(Card carta, Player jugadorActual) {
        pilaDescarte.setColorEfectivo(carta.getColor());
        switch (carta.getAccion()) {
            case REVERSA:
                turnManager.invertirDireccion();
                System.out.println("Sentido invertido!");
                break;
            case SALTO:
                turnManager.agregarSalto();
                System.out.println("Salta turno!");
                break;
            case ROBA2:
                Player siguiente = turnManager.getSiguienteJugador();
                robarCartas(siguiente, 2);
                System.out.println(siguiente.getNombre() + " roba 2 cartas y pierde turno.");
                turnManager.agregarSalto();
                break;
        }
    }

    private void aplicarEfectoComodin(Card carta, Player jugadorActual) {
        Card.Color colorElegido = jugadorActual.esHumano() ? elegirColorHumano() : elegirColorAleatorio();
        pilaDescarte.setColorEfectivo(colorElegido);
        System.out.println("Color elegido: " + colorElegido);

        if (carta.getComodin() == Card.Comodin.ROBA4) {
            Player siguiente = turnManager.getSiguienteJugador();
            robarCartas(siguiente, 4);
            System.out.println(siguiente.getNombre() + " roba 4 cartas y pierde turno.");
            turnManager.agregarSalto();
        }
    }

    private void robarCartas(Player jugador, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Card robada = robarCarta();
            if (robada != null) jugador.agregarCarta(robada);
        }
    }

    private Card.Color elegirColorAleatorio() {
        Card.Color[] colores = Card.Color.values();
        return colores[new Random().nextInt(colores.length)];
    }

    private Card.Color elegirColorHumano() {
        while (true) {
            System.out.print("\nElige un color (ROJO, AMARILLO, VERDE, AZUL): ");
            try {
                return Card.Color.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Color no valido.");
            }
        }
    }
}