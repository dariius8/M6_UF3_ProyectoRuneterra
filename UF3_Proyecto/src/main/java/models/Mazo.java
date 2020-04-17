package models;

import java.util.ArrayList;

public class Mazo {

	private String nombreMazo;
	private int valorMazo;
	private int idMazo;
	private ArrayList<Integer> cartas;

	public Mazo() {

	}

	public Mazo(String nombreMazo, int valorMazo, int idMazo, ArrayList<Integer> cartas) {
		this.nombreMazo = nombreMazo;
		this.valorMazo = valorMazo;
		this.idMazo = idMazo;
		this.cartas = cartas;
	}

	public String getNombreMazo() {
		return nombreMazo;
	}

	public void setNombreMazo(String nombreMazo) {
		this.nombreMazo = nombreMazo;
	}

	public int getValorMazo() {
		return valorMazo;
	}

	public void setValorMazo(int valorMazo) {
		this.valorMazo = valorMazo;
	}

	public int getIdMazo() {
		return idMazo;
	}

	public void setIdMazo(int idMazo) {
		this.idMazo = idMazo;
	}

	public ArrayList<Integer> getCartas() {
		return cartas;
	}

	public void setCartas(ArrayList<Integer> cartas) {
		this.cartas = cartas;
	}

	@Override
	public String toString() {
		return "Mazo [nombreMazo=" + nombreMazo + ", valorMazo=" + valorMazo + ", idMazo=" + idMazo + ", cartas="
				+ cartas + "]";
	}
}
