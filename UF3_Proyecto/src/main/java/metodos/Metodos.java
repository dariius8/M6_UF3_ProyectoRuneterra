package metodos;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import main.Main;
import models.Carta;
import models.Mazo;
import models.Usuario;

public class Metodos {

	static MongoClientURI uri = new MongoClientURI(
			"mongodb+srv://darius:1234@clusterdarius-1alxv.mongodb.net/test?retryWrites=true&w=majority");
	static Scanner lectorInt = new Scanner(System.in);
	static Scanner lectorString = new Scanner(System.in);
	static ArrayList<Integer> cartas_mazo = new ArrayList<Integer>();
	static Usuario u = new Usuario();

	public static void cargarCartas() {
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Cartas
		MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
		// eliminamos la collection si ya existe
		coll_Cartas.drop();
		// json parser
		JSONParser parser = new JSONParser();
		try {
			System.out.println("\nCargando todas las cartas...");
			// leemos el archivo json y lo parseamos en un array json
			FileReader reader = new FileReader("..\\UF3_Proyecto\\src\\main\\resources\\Cartas.json");
			JSONArray jsonArray = (JSONArray) parser.parse(reader);
			// iterator recorriendo el array y guardamos los datos en un object
			Iterator<?> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				// creamos el objeto carta con los setters
				JSONObject obj = (JSONObject) iterator.next();
				Carta c = new Carta();
				c.setId(Integer.parseInt(obj.get("id").toString()));
				c.setTipo((String) obj.get("tipo"));
				c.setNombre((String) obj.get("nombre_carta"));
				c.setCoste(Integer.parseInt(obj.get("coste_invocacion").toString()));
				c.setAtaque(Integer.parseInt(obj.get("ataque").toString()));
				c.setVida(Integer.parseInt(obj.get("vida").toString()));
				c.setHabilidad((String) obj.get("habilidad_especial"));
				c.setFaccion((String) obj.get("faccion"));
				// document de cada carta con los getters
				Document doc_carta = new Document("id", c.getId()).append("tipo", c.getTipo())
						.append("nombre_carta", c.getNombre()).append("coste_invocacion", c.getCoste())
						.append("ataque", c.getAtaque()).append("vida", c.getVida())
						.append("habilidad_especial", c.getHabilidad()).append("faccion", c.getFaccion());
				// insertar cada document en la collection
				coll_Cartas.insertOne(doc_carta);
				System.out.println("Carta " + c.getId() + " '" + c.getNombre() + "' cargada.");
			}
			System.out.println("\nTodas las cartas cargadas correctamente.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// cerrar conexion
		mongoClient.close();
	}

	public static void crearUsuario() {
		// datos objeto usuario
		System.out.println("\n---Creando usuario---");
		System.out.println("Inserta el id del usuario:");
		u.setId(lectorInt.nextInt());
		System.out.println("Inserta el nombre del usuario:");
		u.setNombre(lectorString.nextLine());
		System.out.println("Inserta el password del usuario:");
		u.setPassword(lectorString.nextLine());
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// creamos document con los datos
		Document doc_usuario = new Document("id", u.getId()).append("nombre", u.getNombre())
				.append("password", u.getPassword()).append("mazos", Arrays.asList()).append("cartas", Arrays.asList());
		// lo insertamos
		coll_Usuarios.insertOne(doc_usuario);
		System.out.println("\nUsuario '" + u.getNombre() + "' creado correctamente.");
		// cerrar conexion
		mongoClient.close();
	}

	public static void iniciarSesion() {
		// datos objeto usuario
		System.out.println("\n---Iniciando sesion---");
		System.out.println("Inserta el nombre del usuario:");
		u.setNombre(lectorString.nextLine());
		System.out.println("Inserta el password del usuario:");
		u.setPassword(lectorString.nextLine());
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar usuario por nombre y password
		Document buscarUsuario = new Document("nombre", u.getNombre()).append("password", u.getPassword());
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
		// cerrar conexion
		mongoClient.close();
		// si existe el usuario
		if (resultUsuario.hasNext()) {
			System.out.println("\nSesion iniciada como usuario '" + u.getNombre() + "'");
			Main.menuUsuario();
		} else
			System.out.println("\nError! Nombre o password incorrectos.");
	}

	public static void comprarCarta() {
		ArrayList<Integer> cartas_compradas = new ArrayList<Integer>();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar usuario por nombre
		Document buscarUsuario = new Document("nombre", u.getNombre());
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
		// guardamos las cartas que ya tenia el usuario en un arraylist
		cartas_compradas = (ArrayList<Integer>) resultUsuario.next().get("cartas");
		// vemos si ya tenia cartas compradas
		if (cartas_compradas.size() > 0)
			u.setCartas(cartas_compradas);
		// objeto carta
		Carta c = new Carta();
		System.out.println("\nInserta id de la carta:");
		int id = lectorInt.nextInt();
		// collection Cartas
		MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
		// buscar carta por id
		Document buscarCarta = new Document("id", id);
		// cursor para recorrer documents
		MongoCursor<Document> resultCarta = coll_Cartas.find(buscarCarta).iterator();
		// si la carta existe
		if (resultCarta.hasNext()) {
			Document d = resultCarta.next();
			c.setId(d.getInteger("id"));
			c.setTipo(d.getString("tipo"));
			c.setNombre(d.getString("nombre_carta"));
			c.setCoste(d.getInteger("coste_invocacion"));
			c.setAtaque(d.getInteger("ataque"));
			c.setVida(d.getInteger("vida"));
			c.setHabilidad(d.getString("habilidad_especial"));
			c.setFaccion(d.getString("faccion"));
			// añadimos la nueva carta
			cartas_compradas.add(c.getId());
			u.setCartas(cartas_compradas);
			System.out.println("\nCarta " + c.getId() + " '" + c.getNombre() + "' comprada.");
		} else
			System.out.println("\nError! No existe ninguna carta con ese id.");
		// actualizamos el arraylist de cartas
		Document actualizarUsuario = new Document("$set", new Document("cartas", u.getCartas()));
		coll_Usuarios.findOneAndUpdate(buscarUsuario, actualizarUsuario);
		// cerrar conexion
		mongoClient.close();
	}

	public static void verCartasCompradas() {
		ArrayList<Integer> cartas_compradas = new ArrayList<Integer>();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar usuario por nombre
		Document buscarUsuario = new Document("nombre", u.getNombre());
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
		// si hay cartas ya compradas o no
		cartas_compradas = (ArrayList<Integer>) resultUsuario.next().get("cartas");
		if (cartas_compradas.size() == 0)
			System.out.println("\nNo hay ninguna carta comprada!");
		else {
			System.out.println("\n---Cartas compradas por el usuario " + u.getNombre() + "---");
			for (int id : cartas_compradas) {
				// collection Cartas
				MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
				// buscar carta por id
				Document buscarCarta = new Document("id", id);
				// cursor para recorrer documents
				MongoCursor<Document> resultCarta = coll_Cartas.find(buscarCarta).iterator();
				while (resultCarta.hasNext()) {
					String nombre_carta = (String) resultCarta.next().get("nombre_carta");
					System.out.println("Carta " + id + " '" + nombre_carta + "'");
				}
			}
		}
		// cerrar conexion
		mongoClient.close();
	}

	public static void addCartaMazo() {
		System.out.println("\n---Creando mazo---");
		System.out.println("Inserta id de la carta:");
		int id = lectorInt.nextInt();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar carta por id
		Document buscarCarta = new Document("cartas", id);
		// cursor para recorrer documents
		MongoCursor<Document> resultCarta = coll_Usuarios.find(buscarCarta).iterator();
		// si existe la carta
		if (resultCarta.hasNext()) {
			// añadimos el id de la carta al arraylist si hay menos de 20
			if (cartas_mazo.size() < 20) {
				cartas_mazo.add(id);
				System.out.println("\nCarta con id " + id + " insertada en el mazo.");
			} else
				System.out.println("\nError! Maximo 20 cartas.");

		} else
			System.out.println("\nError! El usuario no tiene comprada una carta con ese id.");
		// cerrar conexion
		mongoClient.close();
	}

	public static void guardarMazo() {
		ArrayList<Integer> id_mazo = new ArrayList<Integer>();
		int coste_mazo = 0;
		// datos objeto mazo
		Mazo m = new Mazo();
		System.out.println("\nIntroduce nombre del mazo");
		m.setNombreMazo(lectorString.nextLine());
		System.out.println("Introduce id del mazo");
		m.setIdMazo(lectorInt.nextInt());
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Cartas
		MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
		// foreach id de las cartas
		for (int id : cartas_mazo) {
			// buscar carta por id
			Document buscarCarta = new Document("id", id);
			// cursor para recorrer documents
			MongoCursor<Document> resultCarta = coll_Cartas.find(buscarCarta).iterator();
			if (resultCarta.hasNext()) {
				int coste_carta = (int) resultCarta.next().get("coste_invocacion");
				coste_mazo = coste_mazo + coste_carta;
			}
			m.setValorMazo(coste_mazo);
		}
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar usuario por nombre
		Document buscarUsuario = new Document("nombre", u.getNombre());
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
		// si el usuario ya tiene mazos
		id_mazo = (ArrayList<Integer>) resultUsuario.next().get("mazos");
		// se añade el id del nuevo mazo al arraylist
		id_mazo.add(m.getIdMazo());
		u.setMazos(id_mazo);
		// actualizamos el arraylist de mazos
		Document actualizarUsuario = new Document("$set", new Document("mazos", u.getMazos()));
		coll_Usuarios.findOneAndUpdate(buscarUsuario, actualizarUsuario);
		// collection Mazos
		MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
		// creamos document mazo con los datos
		Document doc_mazo = new Document("nombre", m.getNombreMazo()).append("valor", m.getValorMazo())
				.append("id", m.getIdMazo()).append("cartas", cartas_mazo);
		// lo insertamos
		coll_Mazos.insertOne(doc_mazo);
		System.out.println("\nMazo '" + m.getNombreMazo() + "' creado correctamente.");
		cartas_mazo.clear();
		// cerrar conexion
		mongoClient.close();
	}

	public static void importarMazo() {
		ArrayList<Integer> mazos_asignados = new ArrayList<Integer>();
		System.out.println("\nInserta id del mazo predefinido (1 o 2):");
		int id = lectorInt.nextInt();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Mazos
		MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
		// buscar mazo por id
		Document buscarMazo = new Document("id", id);
		// cursor para recorrer documents
		MongoCursor<Document> resultMazo = coll_Mazos.find(buscarMazo).iterator();
		// obtenemos los datos
		if (resultMazo.hasNext()) {
			// collection Usuarios
			MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
			// buscar usuario por nombre
			Document buscarUsuario = new Document("nombre", u.getNombre());
			// cursor para recorrer documents
			MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
			mazos_asignados = (ArrayList<Integer>) resultUsuario.next().get("mazos");
			mazos_asignados.add(id);
			// actualizamos el arraylist de mazos
			Document actualizarUsuario = new Document("$set", new Document("mazos", mazos_asignados));
			coll_Usuarios.findOneAndUpdate(buscarUsuario, actualizarUsuario);
			System.out.println("\nMazo predefinido con id " + id + " asignado al usuario '" + u.getNombre() + "'");
		} else
			System.out.println("\nError! No hay ningun mazo con ese id.");
		// cerrar conexion
		mongoClient.close();
	}

	public static int editarMazo() {
		ArrayList<Integer> mazos = new ArrayList<Integer>();
		System.out.println("\n---Editando mazo---");
		System.out.println("Inserta id del mazo:");
		int id_mazo = lectorInt.nextInt();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar usuario por nombre
		Document buscarUsuario = new Document("nombre", u.getNombre());
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(buscarUsuario).iterator();
		// obtenemos los mazos del usuario
		mazos = (ArrayList<Integer>) resultUsuario.next().get("mazos");
		// si el usuario tiene ese mazo
		if (mazos.contains(id_mazo)) {
			mongoClient.close();
			return id_mazo;
		} else {
			System.out.println("\nError! El usuario no tiene ningun mazo con ese id.");
			mongoClient.close();
			return 0;
		}
	}

	public static void modificarMazo_add(int id_mazo) {
		ArrayList<Integer> cartas = new ArrayList<Integer>();
		System.out.println("\n---Insertando carta al mazo---");
		System.out.println("Inserta id de la carta:");
		int id_carta = lectorInt.nextInt();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar carta por id
		Document buscarCarta = new Document("cartas", id_carta);
		// cursor para recorrer documents
		MongoCursor<Document> resultCarta = coll_Usuarios.find(buscarCarta).iterator();
		// si existe la carta
		if (resultCarta.hasNext()) {
			// collection Cartas
			MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
			// buscar carta por id
			Document doc_carta = new Document("id", id_carta);
			// cursor para recorrer documents
			MongoCursor<Document> cursor_cartas = coll_Cartas.find(doc_carta).iterator();
			// obtenemos el valor de la carta
			int coste_invocacion = cursor_cartas.next().getInteger("coste_invocacion");
			// collection Mazos
			MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
			// buscar mazo por id
			Document buscarMazo = new Document("id", id_mazo);
			// cursor para recorrer documents
			MongoCursor<Document> resultMazo = coll_Mazos.find(buscarMazo).iterator();
			Document d = resultMazo.next();
			Mazo m = new Mazo();
			// insertamos la nueva carta en el array
			cartas = d.get("cartas", ArrayList.class);
			cartas.add(id_carta);
			m.setCartas(cartas);
			// obtenemos el valor del mazo mas el coste de la carta
			m.setValorMazo(d.getInteger("valor") + coste_invocacion);
			m.setNombreMazo(d.getString("nombre"));
			// actualizamos el arraylist cartas y el valor del mazo
			Document actualizarMazo = new Document("$set",
					new Document("cartas", m.getCartas()).append("valor", m.getValorMazo()));
			coll_Mazos.findOneAndUpdate(buscarMazo, actualizarMazo);
			System.out.println("\nCarta con id " + id_carta + " insertada al mazo '" + m.getNombreMazo() + "'.");
		} else
			System.out.println("\nError! El usuario no tiene comprada una carta con ese id.");
		// cerrar conexion
		mongoClient.close();
	}

	public static void modificarMazo_remove(int id_mazo) {
		ArrayList<Integer> cartas = new ArrayList<Integer>();
		System.out.println("\n---Eliminando carta del mazo---");
		System.out.println("Inserta id de la carta:");
		int id_carta = lectorInt.nextInt();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Cartas
		MongoCollection<Document> coll_Cartas = database.getCollection("Cartas");
		// buscar carta por id
		Document buscarCarta = new Document("id", id_carta);
		// cursor para recorrer documents
		MongoCursor<Document> resultCarta = coll_Cartas.find(buscarCarta).iterator();
		// obtenemos el coste de la carta
		int coste_invocacion = resultCarta.next().getInteger("coste_invocacion");
		// collection Mazos
		MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
		// buscar mazo por id
		Document buscarMazo = new Document("id", id_mazo);
		// cursor para recorrer documents
		MongoCursor<Document> resultMazo = coll_Mazos.find(buscarMazo).iterator();
		Document d = resultMazo.next();
		Mazo m = new Mazo();
		// obtenemos el arraylist de cartas y borramos la que pide
		cartas = d.get("cartas", ArrayList.class);
		// si existe la carta en el mazo
		if (cartas.contains(id_carta)) {
			// obtenemos la posicion de la carta y la borramos
			int posicionCarta = cartas.indexOf(id_carta);
			cartas.remove(posicionCarta);
			m.setCartas(cartas);
			// obtenemos el valor del mazo menos el coste de la carta
			m.setValorMazo(d.getInteger("valor") - coste_invocacion);
			m.setNombreMazo(d.getString("nombre"));
			// actualizamos el arraylist cartas y el valor del mazo
			Document actualizarMazo = new Document("$set",
					new Document("cartas", m.getCartas()).append("valor", m.getValorMazo()));
			coll_Mazos.findOneAndUpdate(buscarMazo, actualizarMazo);
			System.out.println("\nCarta con id " + id_carta + " eliminada del mazo '" + m.getNombreMazo() + "'.");
		} else
			System.out.println("\nError! No existe ninguna carta en el mazo con ese id.");
		// cerrar conexion
		mongoClient.close();
	}

	public static void renombrarMazo(int id_mazo) {
		System.out.println("\n---Renombrando mazo---");
		System.out.println("Inserta el nuevo nombre del mazo:");
		String nombreMazo = lectorString.nextLine();
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Mazos
		MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
		// buscar mazo por id
		Document buscarMazo = new Document("id", id_mazo);
		// actualizamos el nombre del mazo
		Document actualizarMazo = new Document("$set", new Document("nombre", nombreMazo));
		coll_Mazos.findOneAndUpdate(buscarMazo, actualizarMazo);
		System.out.println("\nMazo renombrado con el nombre '" + nombreMazo + "'.");
		// cerrar conexion
		mongoClient.close();
	}

	public static void eliminarMazo(int id_mazo) {
		ArrayList<Integer> mazos = new ArrayList<Integer>();
		System.out.println("\nEliminando mazo...");
		// conexion
		MongoClient mongoClient = new MongoClient(uri);
		// database Runeterra
		MongoDatabase database = mongoClient.getDatabase("Runeterra");
		// collection Mazos
		MongoCollection<Document> coll_Mazos = database.getCollection("Mazos");
		// buscar mazo por id
		Document buscarMazo = new Document("id", id_mazo);
		// cursor para recorrer documents
		MongoCursor<Document> resultMazo = coll_Mazos.find(buscarMazo).iterator();
		String nombreMazo = resultMazo.next().getString("nombre");
		// buscarlo en la collection Mazos y borrarlo
		coll_Mazos.findOneAndDelete(buscarMazo);
		// collection Usuarios
		MongoCollection<Document> coll_Usuarios = database.getCollection("Usuarios");
		// buscar mazo por id
		Document doc_mazo = new Document("mazos", id_mazo);
		// cursor para recorrer documents
		MongoCursor<Document> resultUsuario = coll_Usuarios.find(doc_mazo).iterator();
		// obtenemos todos los mazos en un arraylist
		mazos = (ArrayList<Integer>) resultUsuario.next().get("mazos");
		// obtenemos la posicion del mazo y la borramos del array
		int posicionMazo = mazos.indexOf(id_mazo);
		mazos.remove(posicionMazo);
		// actualizamos los mazos del usuario sin el que eliminamos
		Document actualizarUsuario = new Document("$set", new Document("mazos", mazos));
		coll_Usuarios.findOneAndUpdate(doc_mazo, actualizarUsuario);
		System.out.println("\nMazo '" + nombreMazo + "' eliminado correctamente.");
		// cerrar conexion
		mongoClient.close();
	}
}
