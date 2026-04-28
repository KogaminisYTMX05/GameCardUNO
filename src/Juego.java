import java.util.*;

public class Juego {
    private Baraja mazo;
    private List<Carta> descarte;
    private List<Mano> jugadores;
    private int turnoActual;
    private int direccion; // 1 horario, -1 antihorario
    private Carta.Color colorActual;
    private int saltosPendientes;
    private boolean juegoTerminado;
    private Scanner scanner;

    public Juego(String nombreHumano) {
        mazo = new Baraja();
        descarte = new ArrayList<>();
        jugadores = new ArrayList<>();
        jugadores.add(new Mano(nombreHumano, true));
        jugadores.add(new Mano("Pepe", false));
        jugadores.add(new Mano("Antonia", false));
        jugadores.add(new Mano("Mari", false));
        scanner = new Scanner(System.in);
        direccion = 1;
        turnoActual = 0;
        saltosPendientes = 0;
        juegoTerminado = false;
    }

    public void iniciarJuego() {
        // Repartir 7 cartas a cada jugador
        for (Mano m : jugadores) {
            for (int i = 0; i < 7; i++) {
                m.agregarCarta(mazo.draw());
            }
        }

        // Carta inicial
        Carta primera = mazo.draw();
        if (primera == null) {
            System.out.println("Error al iniciar el mazo.");
            return;
        }
        descarte.add(primera);
        if (primera.getTipo() == Carta.Tipo.COMODIN) {
            colorActual = elegirColorAleatorio();
            System.out.println("Carta inicial comodín. Color elegido: " + colorActual);
        } else {
            colorActual = primera.getColor();
        }

        System.out.println("\nJuego de Cartas -UNO- v3.0 (4 jugadores - 1P vs 3CPU)");
        System.out.println("\nEste juego consiste en un enfrentamiento entre 3 CPU");
        System.out.println("y un jugador, de donde tienes que seleccionar una carta");
        System.out.println("adecuada en la mesa y asi consecutivamente, el jugador");
        System.out.println("que se quede sin cartas, es el ganador.\n");
        System.out.println("CONTIENE NUEVAS MEJORAS, PARA SABER MAS, VE A README.md\n");
        mostrarEstado();

        while (!juegoTerminado) {
            Mano jugadorActual = jugadores.get(turnoActual);
            System.out.println("\n--- Turno de " + jugadorActual.getNombre() + " ---");
            System.out.println("Carta en mesa: " + getUltimaCarta() + " (color actual: " + colorActual + ")");
            if (jugadorActual.esHumano()) {
                jugadorActual.mostrarCartas();
            }

            boolean turnoCompletado = false;
            while (!turnoCompletado && !juegoTerminado) {
                if (jugadorActual.esHumano()) {
                    turnoCompletado = procesarTurnoHumano(jugadorActual);
                } else {
                    turnoCompletado = procesarTurnoIA(jugadorActual);
                }
            }

            if (juegoTerminado) break;

            // Penalización por no decir UNO (solo humano, roba 2 cartas)
            if (jugadorActual.esHumano() && jugadorActual.getNumeroCartas() == 1 && !jugadorActual.haDichoUno()) {
                System.out.println("¡" + jugadorActual.getNombre() + " no dijo UNO! Penalización: roba 2 cartas.");
                for (int i = 0; i < 2; i++) {
                    Carta penal = robarCarta();
                    if (penal != null) {
                        jugadorActual.agregarCarta(penal);
                        System.out.println("Robaste: " + penal);
                    }
                }
            }
            jugadorActual.setDijoUno(false);

            // Verificar victoria
            if (jugadorActual.getNumeroCartas() == 0) {
                System.out.println("\n" + jugadorActual.getNombre() + " ha ganado!");
                juegoTerminado = true;
                break;
            }

            // Calcular siguiente turno
            int avance = direccion * (1 + saltosPendientes);
            turnoActual = (turnoActual + avance + jugadores.size()) % jugadores.size();
            saltosPendientes = 0;

            mostrarEstado();
        }
        scanner.close();
    }

    private Carta getUltimaCarta() { return descarte.get(descarte.size() - 1); }

    private void mostrarEstado() {
        System.out.println("\n=== ESTADO DEL JUEGO ===");
        System.out.println("Carta en mesa: " + getUltimaCarta() + " (color: " + colorActual + ")");
        System.out.println("Turno actual: " + jugadores.get(turnoActual).getNombre());
        System.out.println("Cartas por jugador:");
        for (Mano m : jugadores) {
            System.out.println("  " + m.getNombre() + ": " + m.getNumeroCartas() + " cartas");
        }
        System.out.println("========================\n");
    }

    private boolean procesarTurnoHumano(Mano jugador) {
        System.out.print("Elige una carta (numero), o 'NO' para robar: ");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("NO")) {
            if (jugador.tieneCartaJugable(getUltimaCarta(), colorActual)) {
                System.out.println("\nTienes cartas jugables. No puedes robar.");
                return false;
            } else {
                Carta robada = robarCarta();
                if (robada != null) {
                    jugador.agregarCarta(robada);
                    System.out.println("Robaste: " + robada);
                    // Si la carta robada es jugable, la juega
                    if (robada.esJugable(getUltimaCarta(), colorActual)) {
                        System.out.println("Juegas la carta robada.");
                        jugador.jugarCarta(jugador.getNumeroCartas() - 1);
                        descarte.add(robada);
                        aplicarEfectoCarta(robada, jugador);
                        // Después de jugar, verificar UNO
                        if (jugador.getNumeroCartas() == 1) {
                            declararUnoHumano(jugador);
                        }
                    }
                }
                return true;
            }
        }

        try {
            int idx = Integer.parseInt(input);
            if (idx < 0 || idx >= jugador.getNumeroCartas()) {
                System.out.println("\nERROR, ENTRADA NO VALIDA. Intenta de nuevo.");
                return false;
            }
            Carta elegida = jugador.getCartas().get(idx);
            if (!elegida.esJugable(getUltimaCarta(), colorActual)) {
                System.out.println("\nEsa carta no es jugable. Debe coincidir en color o numero.");
                return false;
            }
            // Regla +4: no se puede jugar si se tiene carta del color actual
            if (elegida.getTipo() == Carta.Tipo.COMODIN && elegida.getComodin() == Carta.Comodin.ROBA4) {
                boolean tieneColor = false;
                for (Carta c : jugador.getCartas()) {
                    if (c.getTipo() != Carta.Tipo.COMODIN && c.getColor() == colorActual) {
                        tieneColor = true;
                        break;
                    }
                }
                if (tieneColor) {
                    System.out.println("\nNo puedes jugar +4 si tienes una carta del color actual.");
                    return false;
                }
            }

            Carta jugada = jugador.jugarCarta(idx);
            descarte.add(jugada);
            System.out.println("Jugaste: " + jugada);
            aplicarEfectoCarta(jugada, jugador);

            // Después de jugar, si queda una carta, dar oportunidad de decir UNO
            if (jugador.getNumeroCartas() == 1) {
                declararUnoHumano(jugador);
            }
            return true;

        } catch (NumberFormatException e) {
            System.out.println("\nEntrada no válida.");
            return false;
        }
    }

    private void declararUnoHumano(Mano jugador) {
        System.out.print("\nTe queda una carta. Escribe 'UNO' para declararlo (o Enter para omitir): ");
        String respuesta = scanner.nextLine().trim();
        if (respuesta.equalsIgnoreCase("UNO")) {
            jugador.setDijoUno(true);
            System.out.println("Has dicho UNO.");
        } else {
            System.out.println("No declaraste UNO. Si termina el turno sin hacerlo, recibiras penalizacion.");
        }
    }

    private boolean procesarTurnoIA(Mano jugador) {
        Carta ultima = getUltimaCarta();
        Carta jugable = jugador.getPrimeraCartaJugable(ultima, colorActual);

        if (jugable != null) {
            // Regla +4: solo si no tiene carta del color actual
            if (jugable.getTipo() == Carta.Tipo.COMODIN && jugable.getComodin() == Carta.Comodin.ROBA4) {
                boolean tieneColor = false;
                for (Carta c : jugador.getCartas()) {
                    if (c.getTipo() != Carta.Tipo.COMODIN && c.getColor() == colorActual) {
                        tieneColor = true;
                        break;
                    }
                }
                if (tieneColor) {
                    // Buscar otra carta jugable que no sea +4
                    Carta alternativa = null;
                    for (Carta c : jugador.getCartas()) {
                        if (c != jugable && c.esJugable(ultima, colorActual)) {
                            alternativa = c;
                            break;
                        }
                    }
                    if (alternativa != null) jugable = alternativa;
                    else return robarTurnoIA(jugador);
                }
            }

            int indice = jugador.getCartas().indexOf(jugable);
            Carta jugada = jugador.jugarCarta(indice);
            descarte.add(jugada);
            System.out.println(jugador.getNombre() + " juega: " + jugada);
            aplicarEfectoCarta(jugada, jugador);
            if (jugador.getNumeroCartas() == 1) {
                jugador.setDijoUno(true);
                System.out.println(jugador.getNombre() + " dice UNO.");
            }
            return true;
        } else {
            return robarTurnoIA(jugador);
        }
    }

    private boolean robarTurnoIA(Mano jugador) {
        System.out.println(jugador.getNombre() + " no tiene carta, roba.");
        Carta robada = robarCarta();
        if (robada != null) {
            jugador.agregarCarta(robada);
            System.out.println(jugador.getNombre() + " roba: " + robada);
            if (robada.esJugable(getUltimaCarta(), colorActual)) {
                int indice = jugador.getNumeroCartas() - 1;
                Carta jugada = jugador.jugarCarta(indice);
                descarte.add(jugada);
                System.out.println(jugador.getNombre() + " juega la carta robada: " + jugada);
                aplicarEfectoCarta(jugada, jugador);
                if (jugador.getNumeroCartas() == 1) {
                    jugador.setDijoUno(true);
                    System.out.println(jugador.getNombre() + " dice UNO.");
                }
            } else {
                System.out.println(jugador.getNombre() + " no puede jugarla.");
            }
        }
        return true;
    }

    private void aplicarEfectoCarta(Carta carta, Mano jugador) {
        // Actualizar color si no es comodín
        if (carta.getTipo() != Carta.Tipo.COMODIN) {
            colorActual = carta.getColor();
        }

        switch (carta.getTipo()) {
            case ACCION:
                switch (carta.getAccion()) {
                    case REVERSA:
                        direccion *= -1;
                        System.out.println("Sentido invertido!");
                        break;
                    case SALTO:
                        saltosPendientes++;
                        System.out.println("Salta turno!");
                        break;
                    case ROBA2:
                        Mano siguiente = getSiguienteJugador();
                        for (int i = 0; i < 2; i++) {
                            Carta r = robarCarta();
                            if (r != null) siguiente.agregarCarta(r);
                        }
                        System.out.println(siguiente.getNombre() + " roba 2 cartas y pierde turno.");
                        saltosPendientes++;
                        break;
                }
                break;
            case COMODIN:
                switch (carta.getComodin()) {
                    case CAMBIO_COLOR:
                        colorActual = jugador.esHumano() ? elegirColorHumano() : elegirColorAleatorio();
                        System.out.println("Color elegido: " + colorActual);
                        break;
                    case ROBA4:
                        colorActual = jugador.esHumano() ? elegirColorHumano() : elegirColorAleatorio();
                        System.out.println("Color elegido: " + colorActual);
                        Mano sig = getSiguienteJugador();
                        for (int i = 0; i < 4; i++) {
                            Carta r = robarCarta();
                            if (r != null) sig.agregarCarta(r);
                        }
                        System.out.println(sig.getNombre() + " roba 4 cartas y pierde turno.");
                        saltosPendientes++;
                        break;
                }
                break;
            default:
                break;
        }
    }

    private Mano getSiguienteJugador() {
        int sig = (turnoActual + direccion + jugadores.size()) % jugadores.size();
        return jugadores.get(sig);
    }

    private Carta.Color elegirColorAleatorio() {
        Random rand = new Random();
        return Carta.Color.values()[rand.nextInt(Carta.Color.values().length)];
    }

    private Carta.Color elegirColorHumano() {
        while (true) {
            System.out.print("\nElige un color (ROJO, AMARILLO, VERDE, AZUL): ");
            try {
                return Carta.Color.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Color no valido.");
            }
        }
    }

    private Carta robarCarta() {
        if (mazo.isEmpty()) {
            if (descarte.size() <= 1) {
                System.out.println("No quedan cartas para robar.");
                return null;
            }
            Carta top = descarte.remove(descarte.size() - 1);
            List<Carta> resto = new ArrayList<>(descarte);
            descarte.clear();
            descarte.add(top);
            mazo.addCards(resto);
            System.out.println("Mazo rellenado con descarte.");
        }
        return mazo.draw();
    }
}