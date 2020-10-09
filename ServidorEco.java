import java.io.*;
import java.net.*;
import java.util.Random;

public class ServidorEco {  
	public static void main(String[] args) {
		ServerSocket ss = null;
		Socket s = null;

		try {
			ss = new ServerSocket(9999);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println("Escuchando en el puerto 9999: " + ss);
        
		while(true){
			try {
				s = ss.accept();
				System.out.println("Nueva conexion aceptada: " + s);
				new GestorPeticion(s).start();
				s = null;
			} 
			catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		/*
		try {
			s = ss.accept();
			System.out.println("Nueva conexion aceptada: " + s);
			new GestorPeticion(s).start();
			s = null;
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		*/
	}
}


class GestorPeticion extends Thread {
	BufferedReader entrada = null;
	PrintWriter salida = null;
	Socket s;
	//Variables agregadas 
	int contador=0;
	String usuario=null;
	String contras=null;
	boolean existe=false;
	Archivo archivo = new Archivo();
	EncriptadoAES miEncriptadorAES = new EncriptadoAES();
	String claveAES = "gb";


	public GestorPeticion(Socket s){
		this.s = s;
	}

	public void run(){ 
		archivo.leer();
		try{
			entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			while (true){
				
				
				String opcion = miEncriptadorAES.desencriptar(entrada.readLine(),claveAES) ;
				
				//System.out.println(opcion);
				if( opcion.equals("registro") ){
					System.out.println();
					System.out.println("Un usuario esta registeandose: ");
					//System.out.println("he entrado");
					String usuario =  miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
					//System.out.println(usuario);
					String contrasena = miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
					//System.out.println(contrasena);
					
					
					if(existeUsuario(usuario)){
						salida.println( miEncriptadorAES.encriptar("ERROR: Usuario ya registrado.",claveAES) );
						System.out.println("ERROR: Usuario ya registrado.");
						break;
					}
					else{
						String [] arregloUsuarios = new String[2];
						arregloUsuarios[0] = usuario;
						arregloUsuarios[1] = contrasena;
						archivo.BaseDatos.add(arregloUsuarios);
						salida.println( miEncriptadorAES.encriptar("Registro exitoso.",claveAES) );
						System.out.println("Usuario registrado.");
						break;
					}
					
				}
				
				if( opcion.equals("ingreso") ){
					System.out.println();
					System.out.println("Un usuario esta intentando ingresar: ");
					String usuario =  miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
					//System.out.println(usuario);
					
					if(existeUsuario(usuario)){
						String miMensajeAleatorio=mensajeAleatorio();
						salida.println( miEncriptadorAES.encriptar(miMensajeAleatorio,claveAES) );
						//System.out.println("encontrado");
						
						String huellaDigitalServidor = HuellaDigital.generarHuellaDigital(obtenerConstrasena(usuario), miMensajeAleatorio);
						
						String huellaDigitalCliente =  miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
						
						if(huellaDigitalServidor.equals(huellaDigitalCliente)){
							salida.println( miEncriptadorAES.encriptar("Ha ingresado correctamente al sistema",claveAES) );
							System.out.println("Usuario ha ingresado.");
							 
						}
						else{
							salida.println( miEncriptadorAES.encriptar("ERROR: contrasena incorrecta",claveAES) );
							System.out.println("ERROR: contrasena incorrecta");
						}
						
						break;
						
					}
					else{
						salida.println( miEncriptadorAES.encriptar("ERROR: El usuario no existe.",claveAES) );
						System.out.println("ERROR: El usuario no existe.");
						
						break;
					}
					
					
					
					
				}
				
			}
			
			salida.close();
			entrada.close();
			archivo.escribir();
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
        public static String mensajeAleatorio(){
		char caracter;
    	String mensajeAleatorio = "";
    	Random rnd = new Random();
    		for (int i = 0; i < 4000; i++)
    		{
        		int x= (int)(rnd.nextDouble() * 94 + 32);
        		//System.out.println("Numero random= "+x);
             	caracter = (char)(x);
             	mensajeAleatorio += caracter;
    		}
    	
    	return mensajeAleatorio;
    }
	
	public boolean existeUsuario(String usuario){
		boolean existe=false;
		for (int i = 0; i < archivo.BaseDatos.size(); i++){
			if(archivo.BaseDatos.get(i)[0].equals(usuario)){
				//System.out.println("Usuario encontrado");
				existe=true;
			}
		}
		
		return existe;
	}
	
	public String obtenerConstrasena(String usuario){
		
		for (int i = 0; i < archivo.BaseDatos.size(); i++){
			if(archivo.BaseDatos.get(i)[0].equals(usuario)){
				return archivo.BaseDatos.get(i)[1];
			}
		}
		
		return "";
	}
	
}
