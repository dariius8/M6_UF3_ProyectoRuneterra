package main;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import metodos.Metodos;

public class Main {

	static int i = 0;
	static Scanner lector = new Scanner(System.in);

	public static void main(String[] args) {
		disableLogs();
		menuPrincipal();
	}

	private static void disableLogs() {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		mongoLogger.getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
		mongoLogger.getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
	}

	public static void menuPrincipal() {
		i = 0;
		while (i != 3) {
			System.out.println("\nMENU PRINCIPAL");
			System.out.println("   1. Cargar la coleccion de cartas");
			System.out.println("   2. Login");
			System.out.println("   3. Salir");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 4) {
				switch (i) {
				case 1:
					Metodos.cargarCartas();
					break;
				case 2:
					menuLogin();
					break;
				default:
					System.out.println("\nAdios!");
					System.exit(0);
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuLogin() {
		i = 0;
		while (i != 3) {
			System.out.println("\nLOGIN");
			System.out.println("   1. Crear usuario");
			System.out.println("   2. Iniciar sesion");
			System.out.println("   3. Volver al menu principal");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 4) {
				switch (i) {
				case 1:
					Metodos.crearUsuario();
					break;
				case 2:
					Metodos.iniciarSesion();
					break;
				default:
					menuPrincipal();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuUsuario() {
		i = 0;
		while (i != 4) {
			System.out.println("\nFUNCIONES USUARIO");
			System.out.println("   1. Comprar cartas");
			System.out.println("   2. Ver cartas compradas");
			System.out.println("   3. Gestionar mazos");
			System.out.println("   4. Cerrar sesion");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 5) {
				switch (i) {
				case 1:
					Metodos.comprarCarta();
					menuCartas();
					break;
				case 2:
					Metodos.verCartasCompradas();
					break;
				case 3:
					menuMazo();
					break;
				default:
					System.out.println("\nSesion finalizada.");
					menuLogin();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuCartas() {
		i = 0;
		while (i != 2) {
			System.out.println("\nCOMPRAR CARTAS");
			System.out.println("   1. Comprar otra carta");
			System.out.println("   2. Volver al menu usuario");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 3) {
				switch (i) {
				case 1:
					Metodos.comprarCarta();
					break;
				default:
					menuUsuario();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuMazo() {
		i = 0;
		while (i != 4) {
			System.out.println("\nMAZOS");
			System.out.println("   1. Importar mazo");
			System.out.println("   2. Crear mazo");
			System.out.println("   3. Editar mazo");
			System.out.println("   4. Volver al menu usuario");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 5) {
				switch (i) {
				case 1:
					Metodos.importarMazo();
					break;
				case 2:
					Metodos.addCartaMazo();
					menuCrearMazo();
					break;
				case 3:
					int id_mazo = Metodos.editarMazo();
					if (id_mazo != 0)
						menuEditarMazo(id_mazo);
					break;
				default:
					menuUsuario();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuCrearMazo() {
		i = 0;
		while (i != 2) {
			System.out.println("\nCREAR MAZO");
			System.out.println("   1. Insertar otra carta al mazo");
			System.out.println("   2. Guardar mazo");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 3) {
				switch (i) {
				case 1:
					Metodos.addCartaMazo();
					break;
				default:
					Metodos.guardarMazo();
					menuMazo();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}

	public static void menuEditarMazo(int id_mazo) {
		i = 0;
		while (i != 5) {
			System.out.println("\nEDITAR MAZO");
			System.out.println("   1. Insertar carta al mazo");
			System.out.println("   2. Eliminar carta del mazo");
			System.out.println("   3. Renombrar mazo");
			System.out.println("   4. Eliminar mazo");
			System.out.println("   5. Volver al menu mazo");
			System.out.print("Elige una opcion: ");
			i = lector.nextInt();
			if (i > 0 && i < 6) {
				switch (i) {
				case 1:
					Metodos.modificarMazo_add(id_mazo);
					break;
				case 2:
					Metodos.modificarMazo_remove(id_mazo);
					break;
				case 3:
					Metodos.renombrarMazo(id_mazo);
					break;
				case 4:
					Metodos.eliminarMazo(id_mazo);
					menuMazo();
				default:
					menuMazo();
					break;
				}
			} else
				System.out.println("\nError! Valor incorrecto.");
		}
	}
}
