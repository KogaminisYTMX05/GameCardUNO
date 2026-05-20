package uno;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * Interfaz gráfica del juego UNO.
 * Gestiona la ventana principal, los paneles de jugadores, los clics del humano
 * y la lógica de turnos incluyendo CPUs, efectos especiales y la cuenta regresiva de UNO.
 */
public class GUI extends JFrame {

    // =====================================================
    // VARIABLES DE JUEGO Y ESTADO
    // =====================================================
    /** Instancia del juego lógico. */
    private Game game;
    private JLabel currentCardLabel;
    private JPanel humanCardsPanel;
    private JPanel cpuTopCards;
    private JPanel cpuLeftCards;
    private JPanel cpuRightCards;
    private JTextArea chatArea;

    /** Indica si es el turno del humano. */
    private boolean playerTurn = true;
    /** Evita que se ejecuten múltiples acciones de turno a la vez. */
    private boolean turnInProgress = false;
    private int currentPlayerIdx = 0;
    private boolean clockwise = true;
    private boolean skipNextPlayer = false;
    private int pendingDrawCards = 0;
    private String forcedColor = null;

    /** Control del mecanismo de UNO (cuando al jugador le queda 1 carta). */
    private boolean waitingForUno = false;
    private Timer unoTimer;
    private JLabel countdownLabel;
    private int countdownSeconds = 5;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    /**
     * Construye la ventana principal del juego y la hace visible.
     * Inicializa el juego, crea los paneles de la mesa y los botones.
     *
     * @param playerName nombre del jugador humano
     */
    public GUI(String playerName) {
        game = new Game(playerName);
        setTitle("GameCardUNO_v5.0");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel board = new JPanel();
        board.setLayout(null);
        board.setBackground(new Color(0, 170, 0));

        // Paneles de jugadores
        cpuTopCards = createPlayerPanel("CPU 2", 395, 15, 490, 160);
        cpuLeftCards = createPlayerPanel("CPU 1", 20, 250, 340, 215);
        cpuRightCards = createPlayerPanel("CPU 3", 925, 250, 340, 215);
        humanCardsPanel = createPlayerPanel(playerName + " (HUMANO)", 395, 515, 490, 160);

        // Carta central
        currentCardLabel = new JLabel();
        currentCardLabel.setBounds(550, 250, 170, 215);

        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.BOLD, 12));
        chatArea.setBackground(Color.YELLOW);
        chatArea.setForeground(Color.BLACK);
        chatArea.setCaretColor(Color.BLACK);
        chatArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(930, 15, 330, 160);

        // Countdown label (inicialmente invisible)
        countdownLabel = new JLabel();
        countdownLabel.setVisible(false);
        board.add(countdownLabel);

        // Botones con sprites
        JButton drawButton = createSpriteButton("/sprites/boton_robar.png", "/sprites/boton_robar_selected.png", 20, 620);
        JButton unoButton = createSpriteButton("/sprites/boton_uno.png", "/sprites/boton_uno_selected.png", 1040, 620);
        JButton exitButton = createSpriteButton("/sprites/boton_salir.png", "/sprites/boton_salir_selected.png", 20, 20);

        // Eventos
        drawButton.addActionListener(e -> {
            if (playerTurn && !turnInProgress && !game.isGameOver() && !waitingForUno) {
                drawCard();
            }
        });
        unoButton.addActionListener(e -> {
            if (waitingForUno) {
                // Declarar UNO correctamente
                if (unoTimer != null) unoTimer.stop();
                countdownLabel.setVisible(false);
                waitingForUno = false;
                addChatMessage(game.getPlayers().get(0).getName() + " grita UNO!");
                turnInProgress = false;
                afterMoveTransition();
            } else {
                addChatMessage("No tienes una sola carta o no es momento de decir UNO.");
            }
        });
        exitButton.addActionListener(e -> System.exit(0));

        // Agregar componentes
        board.add(cpuTopCards);
        board.add(cpuLeftCards);
        board.add(cpuRightCards);
        board.add(humanCardsPanel);
        board.add(currentCardLabel);
        board.add(scrollPane);
        board.add(drawButton);
        board.add(unoButton);
        board.add(exitButton);
        add(board);

        // Inicializar estado del juego
        updateCenterCard();
        updatePlayerCards();
        addChatMessage("Juego de Cartas -UNO- v5.0 (con interfaz\ngráfica).");
        addChatMessage("\nEste juego consiste en un enfrentamiento\nentre la CPU y el jugador, de donde\ntienes que seleccionar una carta adecuada\nen la mesa y asi consecutivamente, el\njugador que se quede sin cartas, es el\nganador.\n");
        addChatMessage("¡Partida iniciada! Turno de " + game.getPlayers().get(0).getName());
        setVisible(true);

        // Comenzar el juego (el humano empieza)
        currentPlayerIdx = 0;
        playerTurn = true;
        turnInProgress = false;
    }

    // =====================================================
    // MÉTODOS AUXILIARES DE UI
    // =====================================================

    /**
     * Crea un botón con dos imágenes (normal y hover) en la posición indicada.
     *
     * @param normalPath ruta del sprite en estado normal
     * @param hoverPath  ruta del sprite cuando el ratón está encima
     * @param x          coordenada X
     * @param y          coordenada Y
     * @return el botón configurado
     */
    private JButton createSpriteButton(String normalPath, String hoverPath, int x, int y) {
        URL normalURL = getClass().getResource(normalPath);
        URL hoverURL = getClass().getResource(hoverPath);
        if (normalURL == null || hoverURL == null) {
            System.out.println("No se encontró sprite: " + normalPath + " o " + hoverPath);
            return new JButton("ERROR");
        }
        ImageIcon normalIcon = new ImageIcon(normalURL);
        ImageIcon hoverIcon = new ImageIcon(hoverURL);
        JButton button = new JButton(normalIcon);
        button.setBounds(x, y, normalIcon.getIconWidth(), normalIcon.getIconHeight());
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { button.setIcon(hoverIcon); }
            @Override
            public void mouseExited(MouseEvent e) { button.setIcon(normalIcon); }
        });
        return button;
    }

    /**
     * Crea un panel con título para representar a un jugador en la mesa.
     *
     * @param text texto del título (nombre del jugador)
     * @param x    coordenada X
     * @param y    coordenada Y
     * @param w    ancho
     * @param h    alto
     * @return el panel configurado
     */
    private JPanel createPlayerPanel(String text, int x, int y, int w, int h) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, w, h);
        panel.setBackground(new Color(45, 45, 45));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, -20, 20));
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }

    /**
     * Añade un mensaje al área de chat.
     *
     * @param msg mensaje a mostrar
     */
    private void addChatMessage(String msg) {
        chatArea.append(msg + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * Carga una imagen desde la ruta dada, devolviendo un ImageIcon o null si no existe.
     *
     * @param path ruta del recurso
     * @return ImageIcon o null
     */
    private ImageIcon loadSafeIcon(String path) {
        URL imageURL = getClass().getResource(path);
        if (imageURL == null) {
            System.out.println("No se encontró: " + path);
            return null;
        }
        return new ImageIcon(imageURL);
    }

    /**
     * Actualiza la carta central con la imagen de la carta superior de la pila de descartes.
     */
    private void updateCenterCard() {
        Card currentCard = game.getDiscardPile().getTopCard();
        if (currentCard == null) return;
        ImageIcon icon = loadSafeIcon(currentCard.getSpritePath());
        if (icon == null) return;
        Image img = icon.getImage().getScaledInstance(170, 215, Image.SCALE_SMOOTH);
        currentCardLabel.setIcon(new ImageIcon(img));
    }

    /**
     * Refresca la visualización de las manos de todos los jugadores.
     * Las CPUs muestran el dorso de las cartas.
     */
    private void updatePlayerCards() {
        // Mano del humano
        humanCardsPanel.removeAll();
        Player human = game.getPlayers().get(0);
        JLabel title = new JLabel(human.getName() + " (HUMANO)");
        title.setForeground(Color.WHITE);
        humanCardsPanel.add(title);
        for (Card card : human.getHand()) {
            ImageIcon icon = loadSafeIcon(card.getSpritePath());
            if (icon == null) continue;
            Image img = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            JLabel cardLabel = new JLabel(new ImageIcon(img));
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (playerTurn && !turnInProgress && !game.isGameOver() && !waitingForUno) {
                        cardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) { cardLabel.setBorder(null); }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (playerTurn && !turnInProgress && !game.isGameOver() && !waitingForUno) {
                        playCard(card);
                    }
                }
            });
            humanCardsPanel.add(cardLabel);
        }
        // CPUs
        updateCpuCards(cpuTopCards, game.getPlayers().get(2));
        updateCpuCards(cpuLeftCards, game.getPlayers().get(1));
        updateCpuCards(cpuRightCards, game.getPlayers().get(3));
        humanCardsPanel.revalidate();
        humanCardsPanel.repaint();
    }

    /**
     * Muestra la mano de una CPU como imágenes de dorso.
     *
     * @param panel panel del jugador CPU
     * @param cpu   jugador CPU correspondiente
     */
    private void updateCpuCards(JPanel panel, Player cpu) {
        panel.removeAll();
        JLabel title = new JLabel(cpu.getName());
        title.setForeground(Color.WHITE);
        panel.add(title);
        for (int i = 0; i < cpu.getHand().size(); i++) {
            ImageIcon icon = loadSafeIcon("/sprites/detras.png");
            if (icon == null) continue;
            Image img = icon.getImage().getScaledInstance(40, 60, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(img)));
        }
        panel.revalidate();
        panel.repaint();
    }

    // =====================================================
    // NÚCLEO DE LA LÓGICA DE TURNOS
    // =====================================================

    /**
     * Avanza al siguiente jugador según el sentido y aplica efectos pendientes (robar, saltar).
     */
    private void advanceToNextPlayer() {
        if (game.isGameOver() || waitingForUno) return;

        int playerCount = game.getPlayers().size();
        int nextIdx = (clockwise ? currentPlayerIdx + 1 : currentPlayerIdx - 1);
        nextIdx = (nextIdx + playerCount) % playerCount;

        if (pendingDrawCards > 0) {
            Player target = game.getPlayers().get(nextIdx);
            addChatMessage(target.getName() + " debe robar " + pendingDrawCards + " carta(s).");
            applyDrawToPlayer(target, pendingDrawCards);
            pendingDrawCards = 0;
            nextIdx = (clockwise ? nextIdx + 1 : nextIdx - 1);
            nextIdx = (nextIdx + playerCount) % playerCount;
            skipNextPlayer = false;
        } else if (skipNextPlayer) {
            addChatMessage("¡Turno saltado!");
            skipNextPlayer = false;
            nextIdx = (clockwise ? nextIdx + 1 : nextIdx - 1);
            nextIdx = (nextIdx + playerCount) % playerCount;
        }

        currentPlayerIdx = nextIdx;
        executeCurrentPlayerTurn();
    }

    /**
     * Ejecuta el turno del jugador actual (humano o CPU).
     */
    private void executeCurrentPlayerTurn() {
        if (game.isGameOver() || waitingForUno) return;
        Player current = game.getPlayers().get(currentPlayerIdx);
        addChatMessage("Turno de: " + current.getName());
        if (currentPlayerIdx == 0) {
            playerTurn = true;
            turnInProgress = false;
        } else {
            playerTurn = false;
            turnInProgress = true;
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(this::processCpuTurn);
            }).start();
        }
    }

    /**
     * Lógica del turno de una CPU: busca una carta jugable, la juega o roba.
     */
    private void processCpuTurn() {
        if (game.isGameOver() || currentPlayerIdx == 0 || waitingForUno) {
            turnInProgress = false;
            return;
        }

        Player cpu = game.getPlayers().get(currentPlayerIdx);
        Card currentCard = game.getDiscardPile().getTopCard();

        Card playable = null;
        for (Card card : cpu.getHand()) {
            boolean valid;
            if (forcedColor != null) {
                valid = card.getColor().equals(forcedColor) || card.getColor().equals("wild");
            } else {
                valid = game.getRuleEngine().isValidMove(card, currentCard);
            }
            if (valid) {
                playable = card;
                break;
            }
        }

        if (playable != null) {
            cpu.removeCard(playable);
            game.getDiscardPile().addCard(playable);
            addChatMessage(cpu.getName() + " tiró " + playable);
            applySpecialEffects(playable, cpu, false);
            if (!playable.getColor().equals("wild")) forcedColor = null;
            updateCenterCard();
            updatePlayerCards();

            if (cpu.hasWon()) {
                game.setWinner(cpu);
                JOptionPane.showMessageDialog(this, cpu.getName() + " HA GANADO");
                playerTurn = false;
                turnInProgress = false;
                return;
            }
            if (cpu.getHand().size() == 1) {
                addChatMessage(cpu.getName() + " dice UNO!");
            }
            afterMoveTransition();
        } else {
            Card drawn = game.getDeck().drawCard();
            if (drawn != null) {
                cpu.addCard(drawn);
                updatePlayerCards();
                addChatMessage(cpu.getName() + " roba una carta.");
                boolean canPlay;
                if (forcedColor != null) {
                    canPlay = drawn.getColor().equals(forcedColor) || drawn.getColor().equals("wild");
                } else {
                    canPlay = game.getRuleEngine().isValidMove(drawn, currentCard);
                }
                if (canPlay) {
                    addChatMessage(cpu.getName() + " puede jugar la carta robada.");
                    cpu.removeCard(drawn);
                    game.getDiscardPile().addCard(drawn);
                    addChatMessage(cpu.getName() + " tira " + drawn);
                    applySpecialEffects(drawn, cpu, false);
                    if (!drawn.getColor().equals("wild")) forcedColor = null;
                    updateCenterCard();
                    updatePlayerCards();
                    if (cpu.hasWon()) {
                        game.setWinner(cpu);
                        JOptionPane.showMessageDialog(this, cpu.getName() + " HA GANADO");
                        playerTurn = false;
                        turnInProgress = false;
                        return;
                    }
                    afterMoveTransition();
                } else {
                    afterMoveTransition();
                }
            } else {
                addChatMessage("No hay más cartas en el mazo.");
                afterMoveTransition();
            }
        }
    }

    /**
     * Realiza las acciones posteriores a un movimiento válido (avanza al siguiente jugador).
     */
    private void afterMoveTransition() {
        if (game.isGameOver() || waitingForUno) return;
        playerTurn = false;
        turnInProgress = false;
        advanceToNextPlayer();
    }

    // =====================================================
    // ACCIONES DEL HUMANO
    // =====================================================

    /**
     * Permite al humano robar una carta del mazo.
     * Si la carta robada es jugable, se juega automáticamente.
     */
    private void drawCard() {
        if (game.isGameOver() || waitingForUno) return;
        Player human = game.getPlayers().get(0);
        Card drawn = game.getDeck().drawCard();
        if (drawn == null) {
            addChatMessage("No hay cartas que robar.");
            return;
        }
        human.addCard(drawn);
        updatePlayerCards();
        addChatMessage(human.getName() + " roba una carta.");

        Card currentCard = game.getDiscardPile().getTopCard();
        boolean canPlay;
        if (forcedColor != null) {
            canPlay = drawn.getColor().equals(forcedColor) || drawn.getColor().equals("wild");
        } else {
            canPlay = game.getRuleEngine().isValidMove(drawn, currentCard);
        }

        if (canPlay) {
            addChatMessage("¡La carta robada es válida! Se juega automáticamente.");
            human.removeCard(drawn);
            game.getDiscardPile().addCard(drawn);
            addChatMessage(human.getName() + " tira " + drawn);
            applySpecialEffects(drawn, human, true);
            if (!drawn.getColor().equals("wild")) forcedColor = null;
            updateCenterCard();
            updatePlayerCards();
            if (human.hasWon()) {
                game.setWinner(human);
                JOptionPane.showMessageDialog(this, "¡HAS GANADO!");
                playerTurn = false;
                turnInProgress = false;
                return;
            }
            // Si después de jugar la carta robada le queda 1 carta, iniciar cuenta regresiva de UNO
            if (human.getHand().size() == 1 && !game.isGameOver()) {
                startUnoCountdown();
                return;
            }
            afterMoveTransition();
        } else {
            afterMoveTransition();
        }
    }

    /**
     * Intenta jugar una carta seleccionada por el humano.
     * Valida si es un movimiento legal y aplica los efectos especiales.
     *
     * @param selectedCard la carta que el humano quiere jugar
     */
    private void playCard(Card selectedCard) {
        if (game.isGameOver() || waitingForUno) return;
        Player human = game.getPlayers().get(0);
        Card currentCard = game.getDiscardPile().getTopCard();

        boolean validMove;
        if (forcedColor != null) {
            validMove = selectedCard.getColor().equals(forcedColor) || selectedCard.getColor().equals("wild");
        } else {
            validMove = game.getRuleEngine().isValidMove(selectedCard, currentCard);
        }

        if (!validMove) {
            JOptionPane.showMessageDialog(this, "Movimiento inválido");
            return;
        }

        human.removeCard(selectedCard);
        game.getDiscardPile().addCard(selectedCard);
        addChatMessage(human.getName() + " tiró " + selectedCard);

        applySpecialEffects(selectedCard, human, true);
        if (!selectedCard.getColor().equals("wild")) {
            forcedColor = null;
        }

        updateCenterCard();
        updatePlayerCards();

        if (human.getHand().size() == 1) {
            addChatMessage(human.getName() + " tiene una carta. ¡Debe decir UNO!");
        }

        if (human.hasWon()) {
            game.setWinner(human);
            JOptionPane.showMessageDialog(this, "¡HAS GANADO!");
            playerTurn = false;
            turnInProgress = false;
            return;
        }

        // Si después de jugar le queda 1 carta, iniciar cuenta regresiva
        if (human.getHand().size() == 1) {
            startUnoCountdown();
            return;
        }

        afterMoveTransition();
    }

    // =====================================================
    // MÉTODOS DE UNO Y CUENTA REGRESIVA
    // =====================================================

    /**
     * Inicia la cuenta regresiva de 5 segundos para que el humano diga UNO.
     * Si no lo hace a tiempo, recibe una penalización de 2 cartas.
     */
    private void startUnoCountdown() {
        if (waitingForUno) return;
        waitingForUno = true;
        playerTurn = false;
        turnInProgress = true;  // Bloquea a las CPUs
        countdownSeconds = 5;
        addChatMessage("Tienes 5 segundos para decir UNO");
        updateCountdownImage(countdownSeconds);
        countdownLabel.setVisible(true);
        // Centrar el label en la pantalla
        ImageIcon icon = loadSafeIcon("/sprites/tiempo_5_segundos.png");
        if (icon != null) {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            int x = (getWidth() - w) / 2;
            int y = (getHeight() - h) / 2;
            countdownLabel.setBounds(x, y, w, h);
            countdownLabel.setIcon(icon);
        }
        if (unoTimer != null) unoTimer.stop();
        unoTimer = new Timer(1000, e -> {
            countdownSeconds--;
            if (countdownSeconds >= 1) {
                updateCountdownImage(countdownSeconds);
            } else {
                // Tiempo agotado
                unoTimer.stop();
                countdownLabel.setVisible(false);
                waitingForUno = false;
                // Penalización: robar 2 cartas
                Player human = game.getPlayers().get(0);
                for (int i = 0; i < 2; i++) {
                    Card c = game.getDeck().drawCard();
                    if (c != null) human.addCard(c);
                }
                updatePlayerCards();
                addChatMessage("Se acabó el tiempo, penalización: 2 cartas");
                // Reanudar el juego
                turnInProgress = false;
                afterMoveTransition();
            }
        });
        unoTimer.start();
    }

    /**
     * Actualiza la imagen de cuenta regresiva según los segundos restantes.
     *
     * @param seconds segundos restantes (1 a 5)
     */
    private void updateCountdownImage(int seconds) {
        String path = "";
        switch (seconds) {
            case 5: path = "/sprites/tiempo_5_segundos.png"; break;
            case 4: path = "/sprites/tiempo_4_segundos.png"; break;
            case 3: path = "/sprites/tiempo_3_segundos.png"; break;
            case 2: path = "/sprites/tiempo_2_segundos.png"; break;
            case 1: path = "/sprites/tiempo_1_segundo.png"; break;
            default: return;
        }
        ImageIcon icon = loadSafeIcon(path);
        if (icon != null) {
            countdownLabel.setIcon(icon);
            // Ajustar tamaño del label al de la imagen (sin estirar)
            countdownLabel.setSize(icon.getIconWidth(), icon.getIconHeight());
        }
    }

    // =====================================================
    // EFECTOS ESPECIALES Y UTILIDADES
    // =====================================================

    /**
     * Aplica los efectos de una carta especial (reverse, skip, +2, +4, wild).
     *
     * @param card     la carta jugada
     * @param owner    el jugador que la jugó
     * @param isHuman  {@code true} si el jugador es humano, {@code false} si es CPU
     */
    private void applySpecialEffects(Card card, Player owner, boolean isHuman) {
        String value = card.getValue().toLowerCase();
        switch (value) {
            case "reverse":
                clockwise = !clockwise;
                addChatMessage("¡Sentido de juego cambiado!");
                break;
            case "skip":
                skipNextPlayer = true;
                addChatMessage("¡Siguiente jugador pierde el turno!");
                break;
            case "+2":
                pendingDrawCards += 2;
                skipNextPlayer = true;
                addChatMessage("El siguiente jugador roba 2 cartas y pierde el turno.");
                break;
            case "+4":
                pendingDrawCards += 4;
                skipNextPlayer = true;
                addChatMessage("El siguiente jugador roba 4 cartas y pierde el turno.");
                if (isHuman) chooseWildColor();
                else chooseCpuWildColor(owner);
                break;
            case "wild":
                if (isHuman) chooseWildColor();
                else chooseCpuWildColor(owner);
                break;
        }
    }

    /**
     * Hace que un jugador robe una cantidad determinada de cartas (efecto +2 o +4).
     *
     * @param player jugador afectado
     * @param count  número de cartas a robar
     */
    private void applyDrawToPlayer(Player player, int count) {
        for (int i = 0; i < count; i++) {
            Card c = game.getDeck().drawCard();
            if (c != null) player.addCard(c);
        }
        updatePlayerCards();
        addChatMessage(player.getName() + " ahora tiene " + player.getHand().size() + " cartas.");
    }

    /**
     * Muestra un diálogo para que el humano elija un color al jugar un comodín.
     */
    private void chooseWildColor() {
        String[] colors = {"Rojo", "Azul", "Verde", "Amarillo"};
        String chosen = (String) JOptionPane.showInputDialog(this, "Elige un color:", "Wild Card",
                JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);
        if (chosen == null) chosen = "Rojo";
        switch (chosen) {
            case "Rojo": forcedColor = "rojo"; break;
            case "Azul": forcedColor = "azul"; break;
            case "Verde": forcedColor = "verde"; break;
            case "Amarillo": forcedColor = "amarillo"; break;
        }
        addChatMessage("Color cambiado a " + forcedColor.toUpperCase());
    }

    /**
     * La CPU elige el color que más abunda en su mano al jugar un comodín.
     *
     * @param cpu la CPU que juega el comodín
     */
    private void chooseCpuWildColor(Player cpu) {
        int rojo = 0, azul = 0, verde = 0, amarillo = 0;
        for (Card card : cpu.getHand()) {
            switch (card.getColor()) {
                case "rojo": rojo++; break;
                case "azul": azul++; break;
                case "verde": verde++; break;
                case "amarillo": amarillo++; break;
            }
        }
        int max = Math.max(Math.max(rojo, azul), Math.max(verde, amarillo));
        if (max == rojo) forcedColor = "rojo";
        else if (max == azul) forcedColor = "azul";
        else if (max == verde) forcedColor = "verde";
        else forcedColor = "amarillo";
        addChatMessage(cpu.getName() + " cambió el color a " + forcedColor.toUpperCase());
    }
}