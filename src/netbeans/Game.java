import java.util.*;

/**
 * Clase principal que orquesta una partida de UNO.
 * <p>
 * Coordina el mazo, la pila de descarte, los jugadores, el gestor de turnos
 * y el motor de reglas, ejecutando el bucle de juego hasta que un jugador
 * se quede sin cartas.
 * </p>
 */
public class Game {
    private final Deck mazo;
    private final DiscardPile pilaDescarte;
    private final List<Player> jugadores;
    private final TurnManager turnManager;
    private final RuleEngine ruleEngine;
    private final Scanner scanner;
    private boolean juegoTerminado;

    /**
     * Prepara una nueva partida con un jugador humano y tres bots.
     *
     * @param nombreHumano nombre del jugador humano.
     */
    public Game(String nombreHumano) {
        this.mazo = new Deck();
        this.pilaDescarte = new DiscardPile();
        this.jugadores = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        jugadores.add(new Player(nombreHumano, true));
        jugadores.add(new Player("Fatima", false));
        jugadores.add(new Player("Guadalupe", false));
        jugadores.add(new Player("Armando", false));

        this.turnManager = new TurnManager(jugadores);
        this.ruleEngine = new RuleEngine(mazo, pilaDescarte, turnManager, scanner);
        this.juegoTerminado = false;
    }

    /** Inicia y ejecuta el bucle principal del juego. */
    public void iniciar() {
        repartirCartasIniciales();
        configurarPrimeraCarta();

        imprimirMensajeBienvenida();
        mostrarEstado();

        while (!juegoTerminado) {
            Player jugadorActual = turnManager.getJugadorActual();
            System.out.println("\n--- Turno de " + jugadorActual.getNombre() + " ---");
            System.out.println("Carta en mesa: " + pilaDescarte.getCartaSuperior() +
                               " (color: " + pilaDescarte.getColorEfectivo() + ")");
            if (jugadorActual.esHumano()) {
                jugadorActual.mostrarMano();
            }

            boolean turnoCompletado = false;
            while (!turnoCompletado && !juegoTerminado) {
                turnoCompletado = jugadorActual.esHumano() ?
                        procesarTurnoHumano(jugadorActual) :
                        procesarTurnoBot(jugadorActual);
            }

            if (juegoTerminado) break;

            // Penalización por no decir UNO
            ruleEngine.aplicarPenalizacionUno(jugadorActual);

            // Verificar victoria
            if (jugadorActual.getTamañoMano() == 0) {
                System.out.println("\n¡" + jugadorActual.getNombre() + " ha ganado!");
                juegoTerminado = true;
                break;
            }

            turnManager.siguienteTurno();
            mostrarEstado();
        }
        scanner.close();
    }

    // ─── Métodos privados ────────────────────────────────────────────

    private void repartirCartasIniciales() {
        for (Player jugador : jugadores) {
            for (int i = 0; i < 7; i++) {
                jugador.agregarCarta(mazo.robar());
            }
        }
    }

    private void configurarPrimeraCarta() {
        Card primera = mazo.robar();
        pilaDescarte.agregarCarta(primera);
        if (primera.getTipo() == Card.Tipo.COMODIN) {
            Card.Color colorAleatorio = Card.Color.values()[
                    new Random().nextInt(Card.Color.values().length)];
            pilaDescarte.setColorEfectivo(colorAleatorio);
            System.out.println("Carta inicial comodin. Color elegido: " + colorAleatorio);
        }
    }

    private void imprimirMensajeBienvenida() {
        System.out.println("\nJuego de Cartas -UNO- v4.0 (Refactorizado)");
        System.out.println("\nEste juego consiste en un enfrentamiento entre 3 CPU");
        System.out.println("y un jugador, de donde tienes que seleccionar una carta");
        System.out.println("adecuada en la mesa y asi consecutivamente, el jugador");
        System.out.println("que se quede sin cartas, es el ganador.\n");
        System.out.println("CONTIENE NUEVAS MEJORAS, PARA SABER MAS, VE A README.md");
    }

    private void mostrarEstado() {
        System.out.println("\n=== ESTADO DEL JUEGO ===");
        System.out.println("Carta en mesa: " + pilaDescarte.getCartaSuperior() +
                           " (color: " + pilaDescarte.getColorEfectivo() + ")");
        System.out.println("Turno actual: " + turnManager.getJugadorActual().getNombre());
        System.out.println("Cartas por jugador:");
        for (Player p : jugadores) {
            System.out.println("  " + p.getNombre() + ": " + p.getTamañoMano() + " cartas");
        }
        System.out.println("========================\n");
    }

    private boolean procesarTurnoHumano(Player jugador) {
        System.out.print("Elige una carta (numero), o 'NO' para robar: ");
        String entrada = scanner.nextLine().trim();

        if (entrada.equalsIgnoreCase("NO")) {
            if (jugador.tieneCartaJugable(pilaDescarte.getCartaSuperior(), pilaDescarte.getColorEfectivo())) {
                System.out.println("\nTienes cartas jugables. No puedes robar.");
                return false;
            } else {
                Card robada = ruleEngine.robarCarta();
                if (robada != null) {
                    jugador.agregarCarta(robada);
                    System.out.println("Robaste: " + robada);
                    if (robada.esJugable(pilaDescarte.getCartaSuperior(), pilaDescarte.getColorEfectivo())) {
                        System.out.println("Juegas la carta robada.");
                        jugador.jugarCarta(jugador.getTamañoMano() - 1);
                        pilaDescarte.agregarCarta(robada);
                        ruleEngine.aplicarEfectoCarta(robada, jugador);
                        manejarUnoDespuesDeJugar(jugador);
                    }
                }
                return true;
            }
        }

        try {
            int indice = Integer.parseInt(entrada);
            if (indice < 0 || indice >= jugador.getTamañoMano()) {
                System.out.println("\nIndice fuera de rango.");
                return false;
            }
            Card elegida = jugador.getMano().get(indice);
            if (!ruleEngine.esJugadaValida(elegida, jugador)) {
                System.out.println("\nEsa carta no es jugable.");
                return false;
            }

            Card jugada = jugador.jugarCarta(indice);
            pilaDescarte.agregarCarta(jugada);
            System.out.println("Jugaste: " + jugada);
            ruleEngine.aplicarEfectoCarta(jugada, jugador);
            manejarUnoDespuesDeJugar(jugador);
            return true;

        } catch (NumberFormatException e) {
            System.out.println("\nEntrada no valida.");
            return false;
        }
    }

    private void manejarUnoDespuesDeJugar(Player jugador) {
        if (jugador.getTamañoMano() == 1 && jugador.esHumano()) {
            System.out.print("\nTe queda una carta. Escribe 'UNO' para declararlo: ");
            String respuesta = scanner.nextLine().trim();
            if (respuesta.equalsIgnoreCase("UNO")) {
                jugador.setDijoUno(true);
                System.out.println("Has dicho UNO.");
            } else {
                System.out.println("No declaraste UNO. Recibiras penalizacion si termina el turno.");
            }
        }
    }

    private boolean procesarTurnoBot(Player jugador) {
        Card superior = pilaDescarte.getCartaSuperior();
        Card.Color colorEfectivo = pilaDescarte.getColorEfectivo();

        Card jugable = jugador.getMano().stream()
                .filter(c -> ruleEngine.esJugadaValida(c, jugador))
                .findFirst().orElse(null);

        if (jugable != null) {
            int indice = jugador.getMano().indexOf(jugable);
            Card jugada = jugador.jugarCarta(indice);
            pilaDescarte.agregarCarta(jugada);
            System.out.println(jugador.getNombre() + " juega: " + jugada);
            ruleEngine.aplicarEfectoCarta(jugada, jugador);
            if (jugador.getTamañoMano() == 1) {
                jugador.setDijoUno(true);
                System.out.println(jugador.getNombre() + " dice UNO.");
            }
            return true;
        } else {
            System.out.println(jugador.getNombre() + " no tiene carta, roba.");
            Card robada = ruleEngine.robarCarta();
            if (robada != null) {
                jugador.agregarCarta(robada);
                System.out.println(jugador.getNombre() + " roba: " + robada);
                if (robada.esJugable(superior, colorEfectivo)) {
                    if (ruleEngine.esJugadaValida(robada, jugador)) {
                        int indice = jugador.getTamañoMano() - 1;
                        Card jugada = jugador.jugarCarta(indice);
                        pilaDescarte.agregarCarta(jugada);
                        System.out.println(jugador.getNombre() + " juega la carta robada: " + jugada);
                        ruleEngine.aplicarEfectoCarta(jugada, jugador);
                        if (jugador.getTamañoMano() == 1) {
                            jugador.setDijoUno(true);
                            System.out.println(jugador.getNombre() + " dice UNO.");
                        }
                    } else {
                        System.out.println(jugador.getNombre() + " no puede jugar la carta robada (regla +4).");
                    }
                } else {
                    System.out.println(jugador.getNombre() + " no puede jugarla.");
                }
            }
            return true;
        }
    }
}