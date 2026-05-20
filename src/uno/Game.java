package uno;

import java.util.ArrayList;

/**
 * Clase principal que orquesta la lógica del juego UNO.
 * Inicializa el mazo, los jugadores, reparte cartas y coloca la primera carta.
 */
public class Game {

    /** Mazo de cartas. */
    private Deck deck;

    /** Pila de descartes. */
    private DiscardPile discardPile;

    /** Lista de jugadores. */
    private ArrayList<Player> players;

    /** Gestor de turnos (no utilizado activamente en la lógica actual). */
    private TurnManager turnManager;

    /** Motor de reglas. */
    private RuleEngine ruleEngine;

    /** Indica si el juego ha terminado. */
    private boolean gameOver = false;

    /** Jugador que ha ganado la partida (si {@code gameOver} es true). */
    private Player winner = null;

    /**
     * Construye una nueva partida de UNO para un jugador humano.
     * Crea tres CPUs, reparte 7 cartas a cada uno y coloca una carta inicial válida (no especial).
     *
     * @param playerName nombre del jugador humano
     */
    public Game(String playerName) {

        deck = new Deck();

        discardPile = new DiscardPile();

        ruleEngine = new RuleEngine();

        players = new ArrayList<>();

        // =====================================
        // JUGADORES
        // =====================================

        // HUMANO (ABAJO)

        players.add(
                new Player(
                        playerName,
                        false
                )
        );

        // CPU IZQUIERDA

        players.add(
                new Player(
                        "CPU 1",
                        true
                )
        );

        // CPU ARRIBA

        players.add(
                new Player(
                        "CPU 2",
                        true
                )
        );

        // CPU DERECHA

        players.add(
                new Player(
                        "CPU 3",
                        true
                )
        );

        turnManager =
                new TurnManager(players);

        // =====================================
        // REPARTIR EXACTAMENTE 7
        // =====================================

        dealInitialCards();

        // =====================================
        // CARTA INICIAL SEGURA
        // =====================================

        placeInitialCard();

        // =====================================
        // DEBUG OPCIONAL
        // =====================================

        verifyInitialHands();
    }

    /**
     * Reparte 7 cartas a cada jugador, una por una (modo por rondas).
     */
    private void dealInitialCards() {

        // REPARTIR POR TURNOS
        // COMO UNO REAL

        for(int round = 0;
            round < 7;
            round++) {

            for(Player player : players) {

                Card drawnCard =
                        deck.drawCard();

                if(drawnCard != null) {

                    player.addCard(drawnCard);
                }
            }
        }
    }

    /**
     * Coloca la primera carta en la pila de descartes.
     * Si sale una carta especial (+4, +2, skip, reverse, wild), se devuelve al mazo y se roba otra.
     */
    private void placeInitialCard() {

        Card firstCard =
                deck.drawCard();

        // EVITAR CARTAS ESPECIALES
        // COMO PRIMERA CARTA

        while(firstCard != null
                &&
                (
                        firstCard.getValue()
                                .equals("+4")

                        ||

                        firstCard.getValue()
                                .equals("+2")

                        ||

                        firstCard.getValue()
                                .equals("skip")

                        ||

                        firstCard.getValue()
                                .equals("reverse")

                        ||

                        firstCard.getValue()
                                .equals("wild")
                )) {

            // REGRESAR CARTA AL MAZO

            deck.drawCard();

            firstCard =
                    deck.drawCard();
        }

        if(firstCard != null) {

            discardPile.addCard(firstCard);
        }
    }

    /**
     * Imprime en consola la cantidad de cartas que tiene cada jugador al inicio (solo para depuración).
     */
    private void verifyInitialHands() {

        for(Player player : players) {

            System.out.println(
                    player.getName()
                            + " -> "
                            + player.getHand().size()
                            + " cartas"
            );
        }
    }

    // =====================================================
    // GETTERS
    // =====================================================

    /**
     * Devuelve el mazo de cartas.
     *
     * @return el mazo
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Devuelve la pila de descartes.
     *
     * @return la pila de descartes
     */
    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    /**
     * Devuelve la lista de jugadores.
     *
     * @return los jugadores de la partida
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Devuelve el motor de reglas.
     *
     * @return el motor de reglas
     */
    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

    /**
     * Indica si el juego ha terminado.
     *
     * @return {@code true} si hay un ganador, {@code false} en caso contrario
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Establece el jugador ganador y marca la partida como terminada (solo la primera vez).
     *
     * @param player el jugador que ganó
     */
    public void setWinner(Player player) {

        if(!gameOver) {

            gameOver = true;

            winner = player;
        }
    }

    /**
     * Devuelve el jugador que ha ganado la partida.
     *
     * @return el ganador, o {@code null} si aún no hay
     */
    public Player getWinner() {
        return winner;
    }
}