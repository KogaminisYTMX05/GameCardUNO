import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Juego {
    private Baraja mazo;
    private List<Carta> descarte;
    private Mano manoHumana;
    private Mano manoCPU;
    private boolean turnoHumano;
    private boolean juegoTerminado;
    private Scanner scanner;

    public Juego() {
        mazo = new Baraja();
        descarte = new ArrayList<>();
        // Se puede cambiar el nombre del jugador manualmente aquí
        manoHumana = new Mano("P1");
        manoCPU = new Mano("CPU");
        scanner = new Scanner(System.in);
        turnoHumano = true;
        juegoTerminado = false;
    }

    public void iniciarJuego() {
        // Repartir 7 cartas a cada mano
        for (int i = 0; i < 7; i++) {
            manoHumana.agregarCarta(mazo.draw());
            manoCPU.agregarCarta(mazo.draw());
        }
        // Carta inicial en la mesa
        Carta primera = mazo.draw();
        if (primera != null) {
            descarte.add(primera);
        } else {
            System.out.println("Error al iniciar el mazo.");
            return;
        }

        System.out.println("Juego de Cartas -UNO- v1.0\n");
        System.out.println("Este juego consiste en un enfrentamiento entre la CPU");
        System.out.println("y el jugador, de donde tienes que seleccionar una carta");
        System.out.println("adecuada en la mesa y asi consecutivamente, el jugador");
        System.out.println("que se quede sin cartas, es el ganador.\n");
        System.out.println("Carta inicial: " + getTopDescarte());

        while (!juegoTerminado) {
            if (turnoHumano) {
                turnoHumano();
            } else {
                turnoCPU();
            }
            // Verificar si alguien ganó
            if (manoHumana.getNumeroCartas() == 0) {
                System.out.println("\nCONGRATULATIONS, eres el vencedor!");
                juegoTerminado = true;
            } else if (manoCPU.getNumeroCartas() == 0) {
                System.out.println("\nOH NO, la CPU te ha vencido! Suerte pa'l proximo intento.");
                juegoTerminado = true;
            }
            turnoHumano = !turnoHumano; // Cambiar turno
        }
        scanner.close();
    }

    private Carta getTopDescarte() {
        return descarte.isEmpty() ? null : descarte.get(descarte.size() - 1);
    }

    private void turnoHumano() {
        System.out.println("\n--- Turno del Humano ---");
        System.out.println("Carta en mesa: " + getTopDescarte());
        manoHumana.mostrarCartas();

        boolean turnoValido = false;
        while (!turnoValido && !juegoTerminado) {
            System.out.print("Elige una carta (numero) o escribe 'NO' para robar (si no tienes jugable): ");
            String input = scanner.nextLine().trim();

            // Comprobar si es un número negativo (no hace nada)
            try {
                int numero = Integer.parseInt(input);
                if (numero < 0) {
                    System.out.println("\nComando no valido (negativo), coloca la opcion correcta.");
                    continue;
                }
                // Número positivo: debe ser un índice válido
                int indice = numero - 1;
                if (indice < 0 || indice >= manoHumana.getNumeroCartas()) {
                    System.out.println("\nNumero fuera de rango. Intenta de nuevo.");
                    continue;
                }
                Carta elegida = manoHumana.getCartas().get(indice);
                if (elegida.esJugable(getTopDescarte())) {
                    Carta jugada = manoHumana.jugarCarta(indice);
                    descarte.add(jugada);
                    System.out.println("Has jugado: " + jugada);
                    turnoValido = true;
                } else {
                    System.out.println("\nEsa carta no es jugable. Debe coincidir en color o número.");
                }
            } catch (NumberFormatException e) {
                // No es un número: puede ser "NO"
                if (input.equalsIgnoreCase("NO")) {
                    if (manoHumana.tieneCartaJugable(getTopDescarte())) {
                        System.out.println("\nTienes cartas jugables. No puedes robar. Debes jugar una carta.");
                    } else {
                        Carta robada = robarCarta();
                        if (robada != null) {
                            manoHumana.agregarCarta(robada);
                            System.out.println("Has robado: " + robada);
                        }
                        turnoValido = true; // Termina el turno después de robar
                    }
                } else {
                    System.out.println("\nComando no reconocido. Intenta de nuevo.");
                }
            }
        }
    }

    private void turnoCPU() {
        System.out.println("\n--- Turno de la CPU ---");
        Carta top = getTopDescarte();
        Carta jugable = manoCPU.getPrimeraCartaJugable(top);

        if (jugable != null) {
            // Juega la primera carta jugable que encuentra
            int indice = manoCPU.getCartas().indexOf(jugable);
            Carta jugada = manoCPU.jugarCarta(indice);
            descarte.add(jugada);
            System.out.println("La CPU juega: " + jugada);
        } else {
            System.out.println("La CPU no tiene carta jugable, roba una carta.");
            Carta robada = robarCarta();
            if (robada != null) {
                manoCPU.agregarCarta(robada);
                System.out.println("La CPU roba: " + robada);
                // Si la carta robada es jugable, la juega inmediatamente
                if (robada.esJugable(top)) {
                    int indice = manoCPU.getCartas().indexOf(robada);
                    Carta jugada = manoCPU.jugarCarta(indice);
                    descarte.add(jugada);
                    System.out.println("Y la juega: " + jugada);
                } else {
                    System.out.println("No puede jugarla, termina su turno.");
                }
            }
        }
    }

    private Carta robarCarta() {
        if (mazo.isEmpty()) {
            // Rebarajar el descarte excepto la última carta
            if (descarte.size() <= 1) {
                System.out.println("No quedan cartas en el mazo ni en el descarte para robar.");
                return null;
            }
            Carta top = descarte.remove(descarte.size() - 1);
            List<Carta> resto = new ArrayList<>(descarte);
            descarte.clear();
            descarte.add(top);
            mazo.addCards(resto);
            System.out.println("El mazo se ha rellenado con el descarte (excepto la última carta).");
        }
        return mazo.draw();
    }
}