import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean jugarDeNuevo = true;
        
        System.out.println("Juego de Cartas -UNO- v1.0\n");
        System.out.println("Este juego consiste en un enfrentamiento entre la CPU");
        System.out.println("y el jugador, de donde tienes que seleccionar una carta");
        System.out.println("adecuada en la mesa y asi consecutivamente, el jugador");
        System.out.println("que se quede sin cartas, es el ganador.");
        
        while (jugarDeNuevo) {
            // Crear y iniciar el juego
            Juego juego = new Juego();
            juego.iniciarJuego();
            
            // Preguntar si quiere jugar otra vez
            System.out.print("\n¿Quieres jugar otra partida? (s/n): ");
            String respuesta = scanner.next().toLowerCase();
            jugarDeNuevo = respuesta.equals("s") || respuesta.equals("si");
            
            System.out.println();
        }
        System.out.println("Muchas gracias por jugar.");
        scanner.close();
    }
}