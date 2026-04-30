import java.util.Scanner;

/**
 * Punto de entrada del juego UNO.
 * <p>
 * Solicita el nombre del jugador humano y lanza una partida nueva.
 * </p>
 */
public class Main {
    /**
     * Método principal.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) nombre = "P1";

        Game juego = new Game(nombre);
        juego.iniciar();
        sc.close();
    }
}