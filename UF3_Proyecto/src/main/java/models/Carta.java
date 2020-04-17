package models;

public class Carta {

	private int id;
	private String tipo;
	private String nombre;
	private int coste;
	private int ataque;
	private int vida;
	private String habilidad;
	private String faccion;

	public Carta() {

	}

	public Carta(int id, String tipo, String nombre, int coste, int ataque, int vida, String habilidad,
			String faccion) {
		this.id = id;
		this.tipo = tipo;
		this.nombre = nombre;
		this.coste = coste;
		this.ataque = ataque;
		this.vida = vida;
		this.habilidad = habilidad;
		this.faccion = faccion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCoste() {
		return coste;
	}

	public void setCoste(int coste) {
		this.coste = coste;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public String getHabilidad() {
		return habilidad;
	}

	public void setHabilidad(String habilidad) {
		this.habilidad = habilidad;
	}

	public String getFaccion() {
		return faccion;
	}

	public void setFaccion(String faccion) {
		this.faccion = faccion;
	}

	@Override
	public String toString() {
		return "Carta [id=" + id + ", tipo=" + tipo + ", nombre=" + nombre + ", coste=" + coste + ", ataque=" + ataque
				+ ", vida=" + vida + ", habilidad=" + habilidad + ", faccion=" + faccion + "]";
	}
}
