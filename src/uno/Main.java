package uno;

import javax.swing.*;

/**
 * Clase principal que inicia el juego UNO.
 * Solicita el nombre del jugador y lanza la interfaz gráfica.
 */
public class Main {

    /**
     * Punto de entrada de la aplicación.
     * Muestra un diálogo para ingresar el nombre del jugador y crea la ventana principal.
     *
     * @param args argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String playerName =
                    JOptionPane.showInputDialog(
                            null,
                            "Ingresa tu nombre:"
                    );

            if(playerName == null
                    || playerName.trim().isEmpty()) {

                playerName = "P1";
            }

            new GUI(playerName);
        });
    }
}