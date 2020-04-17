package models;

import java.util.ArrayList;

public class Usuario {

	private int id;
	private String nombre;
	private String password;
	private ArrayList<Integer> mazos;
	private ArrayList<Integer> cartas;

	public Usuario() {

	}

	public Usuario(int id, String nombre, String password, ArrayList<Integer> mazos, ArrayList<Integer> cartas) {
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.mazos = mazos;
		this.cartas = cartas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Integer> getMazos() {
		return mazos;
	}

	public void setMazos(ArrayList<Integer> mazos) {
		this.mazos = mazos;
	}

	public ArrayList<Integer> getCartas() {
		return cartas;
	}

	public void setCartas(ArrayList<Integer> cartas) {
		this.cartas = cartas;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", password=" + password + ", mazos=" + mazos + ", cartas="
				+ cartas + "]";
	}
}
