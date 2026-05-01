package juegouno;

import java.util.List;

public class TurnManager {
    private final List<Player> jugadores;
    private int indiceJugadorActual;
    private int direccion;      // 1 = horario, -1 = antihorario
    private int saltosPendientes;

    public TurnManager(List<Player> jugadores) {
        this.jugadores = jugadores;
        this.indiceJugadorActual = 0;
        this.direccion = 1;
        this.saltosPendientes = 0;
    }

    public Player getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }

    public void siguienteTurno() {
        int pasos = direccion * (1 + saltosPendientes);
        indiceJugadorActual = (indiceJugadorActual + pasos) % jugadores.size();
        if (indiceJugadorActual < 0) indiceJugadorActual += jugadores.size();
        saltosPendientes = 0;
    }

    public Player getSiguienteJugador() {
        int siguienteIndice = (indiceJugadorActual + direccion) % jugadores.size();
        if (siguienteIndice < 0) siguienteIndice += jugadores.size();
        return jugadores.get(siguienteIndice);
    }

    public void invertirDireccion() { direccion *= -1; }
    public void agregarSalto() { saltosPendientes++; }
    public void agregarSaltos(int cantidad) { saltosPendientes += cantidad; }
    public int getDireccion() { return direccion; }
}