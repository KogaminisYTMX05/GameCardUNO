import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Motor de reglas del juego UNO.
 * <p>
 * Encapsula toda la lógica de validación de jugadas, ejecución de efectos
 * de cartas especiales, robo de cartas y penalizaciones.
 * </p>
 */
public class RuleEngine {
    private final Deck mazo;
    private final DiscardPile pilaDescarte;
    private final TurnManager turnManager;
    private final Scanner scanner;

    /**
     * Construye el motor de reglas.
     *
     * @param mazo         mazo principal.
     * @param pilaDescarte pila de descarte.
     * @param turnManager  gestor de turnos.
     * @param scanner      entrada del usuario (para elección de color).
     */
    public RuleEngine(Deck mazo, DiscardPile pilaDescarte, TurnManager turnManager, Scanner scanner) {
        this.mazo = mazo;
        this.pilaDescarte = pilaDescarte;
        this.turnManager = turnManager;
        this.scanner = scanner;
    }

    /**
     * Valida si una carta es jugable según las reglas completas.
     * <p>
     * Además de la coincidencia básica, implementa la restricción de
     * {@code +4}: no se puede jugar si el jugador posee una carta del color
     * actual (aunque sea de distinto tipo).
     * </p>
     *
     * @param carta   carta que se quiere jugar.
     * @param jugador jugador que la quiere jugar.
     * @return {@code true} si la jugada es válida.
     */
    public boolean esJugadaValida(Card carta, Player jugador) {
        Card superior = pilaDescarte.getCartaSuperior();
        Card.Color colorEfectivo = pilaDescarte.getColorEfectivo();

        if (!carta.esJugable(superior, colorEfectivo)) {
            return false;
        }

        // Regla especial +4
        if (carta.getTipo() == Card.Tipo.COMODIN && carta.getComodin() == Card.Comodin.ROBA4) {
            boolean tieneColorCoincidente = jugador.getMano().stream()
                    .anyMatch(c -> c.getTipo() != Card.Tipo.COMODIN && c.getColor() == colorEfectivo);
            if (tieneColorCoincidente) {
                return false;
            }
        }
        return true;
    }

    /**
     * Aplica el efecto correspondiente a la carta recién jugada.
     * <p>
     * Actualiza el color, la dirección, los saltos, o hace robar cartas
     * según el tipo de carta.
     * </p>
     *
     * @param carta         carta jugada.
     * @param jugadorActual jugador que la jugó.
     */
    public void aplicarEfectoCarta(Card carta, Player jugadorActual) {
        switch (carta.getTipo()) {
            case ACCION:
                aplicarEfectoAccion(carta, jugadorActual);
                break;
            case COMODIN:
                aplicarEfectoComodin(carta, jugadorActual);
                break;
            default:
                // NUMERO: sin efecto especial, solo fija el color
                pilaDescarte.setColorEfectivo(carta.getColor());
                break;
        }
    }

    /**
     * Roba una carta del mazo. Si el mazo está vacío, rellena con la pila
     * de descarte (excepto la carta superior) y baraja.
     *
     * @return carta robada, o {@code null} si no hay cartas disponibles.
     */
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

    /**
     * Aplica la penalización por no haber dicho UNO.
     * Si el jugador se quedó con una sola carta y no lo declaró, roba 2 cartas.
     *
     * @param jugador jugador a verificar.
     */
    public void aplicarPenalizacionUno(Player jugador) {
        if (jugador.getTamañoMano() == 1 && !jugador.haDichoUno()) {
            System.out.println("El jugador " + jugador.getNombre() + " no dijo UNO! Penalizacion: roba 2 cartas.");
            robarCartas(jugador, 2);
        }
        jugador.setDijoUno(false);
    }

    // ─── Métodos privados ────────────────────────────────────────────

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