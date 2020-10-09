import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteEco{
	public static void main(String[] args) {
		
		
		
		Scanner sc = new Scanner(System.in);
		//se obtiene el servidor
		String servidor = args[0];
		//se obtiene el puerto de conexion
		int puerto = Integer.parseInt(args[1]);
		//String servidor = "localhost";
		//int puerto = 9999;
		
		String opcion = args[2];
		String usuario;
		String contrasena;
		
		EncriptadoAES miEncriptadorAES = new EncriptadoAES();
		String claveAES = "gb";
		
		try{

			Socket socket = new Socket(servidor,puerto);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ),true );
			
			
			
			System.out.println(opcion);
			
			
			salida.println( miEncriptadorAES.encriptar(opcion,claveAES) );
			
			if( opcion.equals("registro") ){
				
			 	//Ingresar usuario
				System.out.println("Ingresa usuario:");
				usuario =  sc.nextLine();
				salida.println( miEncriptadorAES.encriptar(usuario,claveAES) );

				//Ingresa contraseña
				System.out.println("Ingresa contrasena:");
				contrasena =  sc.nextLine();
				salida.println( miEncriptadorAES.encriptar(contrasena,claveAES) );
				

				String resultado = miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
				System.out.println(resultado);
			
			}
			
			if( opcion.equals("ingreso") ){
				
				
				//Ingresar usuario
				System.out.println("Ingresa usuario:");
				usuario =  sc.nextLine();
				salida.println( miEncriptadorAES.encriptar(usuario,claveAES) );
				
				String resultado = miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
				
				if(resultado.equals("ERROR: El usuario no existe.")){
					System.out.println(resultado);
				}
				
				else{
					//Ingresa contraseña
					System.out.println("Ingresa contrasena:");
					contrasena =  sc.nextLine();
					salida.println( miEncriptadorAES.encriptar(HuellaDigital.generarHuellaDigital(contrasena, resultado),claveAES) );
					
					String resultadoFinal = miEncriptadorAES.desencriptar(entrada.readLine(),claveAES);
					
					System.out.println(resultadoFinal);
					
				}

			}
		}
		catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
        }
		
		
	}
	
	
}
