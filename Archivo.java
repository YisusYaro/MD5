import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class Archivo {
	public ArrayList<String[]> BaseDatos = new ArrayList<>();
	Archivo(){
    }

     public void leer(){       
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File ("archivo.txt");
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
 
			// Lectura del fichero
			String linea;
			while((linea=br.readLine())!=null){                      
                                StringTokenizer tokens = new StringTokenizer(linea," ");
                                String[] arregloUsuarios = new String[2];
                                arregloUsuarios[0] =  tokens.nextToken() ;
                                arregloUsuarios[1] =  tokens.nextToken() ;
                            BaseDatos.add(arregloUsuarios);                          
            }                       
        }
        catch(IOException | NumberFormatException e){
        }finally{
           try{
              if( null != fr ){
                 fr.close();
              }
           }catch (IOException e2){
           }
        }

        /*System.out.println("Imprimiendo array");
        for (int i = 0; i < BaseDatos.size(); i++){
        	System.out.println("Usuario: "+BaseDatos.get(i)[0]+"\t Contraseña: "+BaseDatos.get(i)[1]);
        }*/              
    } 

    public void escribir(){
        	FileWriter fichero = null;
        	PrintWriter pw = null;
        	try
        	{
            	fichero = new FileWriter("archivo.txt");
            	pw = new PrintWriter(fichero);

            	for (int i = 0; i < BaseDatos.size(); i++)
                	pw.println(BaseDatos.get(i)[0]+" "+BaseDatos.get(i)[1]);

        	} catch (Exception e) {
            	e.printStackTrace();
        	} finally {
           		try {
           		// Nuevamente aprovechamos el finally para 
           		// asegurarnos que se cierra el fichero.
           		if (null != fichero)
            	  	fichero.close();
            	} catch (Exception e2) {
            		  e2.printStackTrace();
           		}
        	}
    }
}
