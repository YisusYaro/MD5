public class HuellaDigital{
	public static String generarHuellaDigital(String contrasena, String mensajeAleatorio){
		String huellaDigital="";
		
		
		int rango=-1;
		
		for (int i=0; i<contrasena.length(); i++){

			int esparcimiento=mensajeAleatorio.length()/contrasena.length();

			int cubo = (int)Math.pow(contrasena.codePointAt(i), 3); 
			
			int posicion = cubo % (esparcimiento-1);
			
			posicion = posicion + rango;
			
			//System.out.println("Se ha insetado: " +contrasena.charAt(i));
			
			//System.out.println("En la posicion " +posicion);
			
			mensajeAleatorio=addChar(mensajeAleatorio, contrasena.charAt(i), posicion);
			
			rango+=esparcimiento;
			
			//System.out.println("En el rango  " +rango);
			
			
		}
		huellaDigital=mensajeAleatorio;
		
		huellaDigital=Md5.calcularMd5(huellaDigital);
		
		return huellaDigital;
	}
	
	public static String addChar(String str, char ch, int position) {
    StringBuilder sb = new StringBuilder(str);
    sb.insert(position, ch);
    return sb.toString();
	}
}