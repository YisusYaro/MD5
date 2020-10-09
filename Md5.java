import java.io.*; 

import java.nio.ByteBuffer;
public class Md5{


	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}
	
	public static byte[] intToBytes(int i){
		byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
		return bytes;
	}
	
	public static String toHexString(byte[] b){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++)
		{
		  sb.append(String.format("%02X", b[i] & 0xFF));
		}
		return sb.toString();
	  }
	
	public static int F(int x, int y, int z){
		return (x & y) | (~x & z);
	}
	
	public static int G(int x, int y, int z){
		return (x & z) | (y & ~z);
	}
	
	public static int H(int x, int y, int z){
		return x ^ y ^ z;
	}
	
	public static int I(int x, int y, int z){
		return y ^ (x | ~z);
	}
	
	public static int T[];

	public static int [] generarTabla(){
		int [] T = new int [64];
		double valor = 4294967296L;
		for (int i = 0; i < 64; i++){
			double calculo = valor * Math.abs(Math.sin(i + 1));
			T[i] = (int) calculo;
			
		}
		return T;
	}
	
	public static int primeraEtapa(int a, int b, int c, int d, int k, int s, int i){
		return b + ((a + F(b,c,d) + k + T[i-1]) << s);
	}
	
	public static int segundaEtapa(int a, int b, int c, int d, int k, int s, int i){
		return  b + ((a + G(b,c,d) + k + T[i-1]) << s);
	}
	
	public static int terceraEtapa(int a, int b, int c, int d, int k, int s, int i){
		return a =   b + ((a + H(b,c,d) + k + T[i-1]) << s);
	}
	
	public static int cuartaEtapa(int a, int b, int c, int d, int k, int s, int i){
		return a =    b + ((a + I(b,c,d) + k + T[i-1]) << s);
	}
	
	public static int obteneValorKbit(byte b1, byte b2, int k){	
		
		k=15-k;
		
		int i1 = b1 &255;//System.out.println("valor entero del primer byte"+i1);
		i1 = i1 << 8;//System.out.println("valor entero del primer byte corrido"+i1);
		
		int i2 = b2 &255;//System.out.println("valor entero del segundo byte"+i2);
		
		i2 = i1 | i2; //System.out.println("valor entero de ambos byte"+i2);
		
		i2 = i2 >> k; //System.out.println("valor entero de ambos byte corrido no solo"+i2);
		
		if((i2%2)==0){
			return 0;
		}
		else{
			return 1;
		}

	}
	
	public static String calcularMd5(String mensaje){
		
			byte[] mensajeBytes = mensaje.getBytes(); 
			int contador = 0;
			for(byte b: mensajeBytes){
			   
			   contador++;
			}
			
			int numeroBytesOriginal=contador;
			
		
			
			while(contador%64!=0){
				contador++;
			}
		
			
			int numeroBytesRelleno=contador;
			
			byte[] mensajeBytesRelleno = new byte[numeroBytesRelleno];
			
			int iteradorBytesSinRelleno=0;
			for(byte b: mensajeBytes){
				mensajeBytesRelleno[iteradorBytesSinRelleno]=mensajeBytes[iteradorBytesSinRelleno];
				iteradorBytesSinRelleno++;
			}
			
			mensajeBytesRelleno[iteradorBytesSinRelleno]=(byte)128;
			
			
			
			long longitudEnBits = (long)(numeroBytesRelleno*8);
			
			byte[] longitud = Md5.longToBytes(longitudEnBits);
			
			for(int j=numeroBytesRelleno-1, k=7; j>numeroBytesRelleno-9; j--,k--){
				mensajeBytesRelleno[j]=longitud[k];
			}
			
			
			
			
			int A=0x01234567;
			int B=0x89abcdef;
			int C=0xfedcba98;
			int D=0x76543210;
			
			T = generarTabla();
			
			for(int i=0; i<numeroBytesRelleno; i+=64){
				
				for(int j=i; j<i+(64-1); j+=2){
					
					int AA=A;
					int BB=B;
					int CC=C;
					int DD=D;
					
					
					//PRIMERA ETAPA
					//[ABCD 0 7 1]
					A=primeraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 0),7,1);
					//[DABC 1 12 2]
					D=primeraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),12,2);
					//[CDAB 2 17 3]
					C=primeraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 2),17,3);
					//[BCDA 3 22 4]
					B=primeraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 3),22,4);
					//[ABCD 4 7 5]
					A=primeraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 4),7,5);
					//[DABC 5 12 6]
					D=primeraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 5),12,6);
					//[CDAB 6 17 7]
					C=primeraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 6),17,7);
					//[BCDA 7 22 8]
					B=primeraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 7),22,8);
					//[ABCD 8 7 9]
					A=primeraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 8),7,9);
					//[DABC 9 12 10]
					D=primeraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 9),12,10);
					//[CDAB 10 17 11]
					C=primeraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 10),17,11);
					//[BCDA 11 22 12]
					B=primeraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 11),22,12);
					//[ABCD 12 7 13]
					A=primeraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 12),7,13);
					//[DABC 13 12 14]
					D=primeraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 13),12,14);
					//[CDAB 14 17 15]
					C=primeraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 14),17,15);
					//[BCDA 15 22 16]
					B=primeraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 15),22,16);
					
					//SEGUNDA ETAPA
					//[ABCD 1 5 17]
					A=segundaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,17);
					//[DABC 6 9 18]
					D=segundaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 6),9,18);
					//[CDAB 11 14 19]
					C=segundaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 11),14,19);
					//[BCDA 0 20 20]
					B=segundaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 0),20,20);
					//[ABCD 5 5 21]
					A=segundaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 5),5,21);
					//[DABC 10 9 22]
					D=segundaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 10),9,22);
					//[CDAB 15 14 23]
					C=segundaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 15),14,23);
					//[BCDA 4 20 24]
					B=segundaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 4),20,24);
					//[ABCD 9 5 25]
					A=segundaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 9),5,25);
					//[DABC 14 9 26]
					D=segundaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 14),9,26);
					//[CDAB 3 14 27]
					C=segundaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 3),14,27);
					//[BCDA 8 20 28]
					B=segundaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 8),20,28);
					//[ABCD 13 5 29]
					A=segundaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 13),5,29);
					//[DABC 2 9 30]
					D=segundaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 2),9,30);
					//[CDAB 7 14 31]
					C=segundaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 7),14,31);
					//[BCDA 12 20 32]
					B=segundaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,32);
					
					//TERCERA ETAPA
					//[ABCD 5 4 33]
					A=terceraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 5),4,33);
					//[DABC 8 11 34]
					D=terceraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 8),11,34);
					//[CDAB 11 16 35]
					C=terceraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 11),16,35);
					//[BCDA 14 23 36]
					B=terceraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 14),23,36);
					//[ABCD 1 4 37]
					A=terceraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),4,37);
					//[DABC 4 11 38]
					D=terceraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 4),11,38);
					//[CDAB 7 16 39]
					C=terceraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 7),16,39);
					//[BCDA 10 23 40]
					B=terceraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,40);
					//[ABCD 13 4 41]
					A=terceraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 13),4,41);
					//[DABC 0 11 42]
					D=terceraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 0),11,42);
					//[CDAB 3 16 43]
					C=terceraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 3),16,43);
					//[BCDA 6 23 44]
					B=terceraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 6),23,44);
					//[ABCD 9 4 45]
					A=terceraEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 9),4,45);
					//[DABC 12 11 46]
					D=terceraEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 12),11,46);
					//[CDAB 15 16 47]
					C=terceraEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,47);
					//[BCDA 2 23 48]
					B=terceraEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,48);
					
					//CUARTA ETAPA
					//[ABCD 0 6 49]
					A=cuartaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),5,49);
					//[DABC 7 10 50]
					D=cuartaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 7),10,50);
					//[CDAB 14 15 51]
					C=cuartaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 14),15,51);
					//[BCDA 5 21 52]
					B=cuartaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 5),21,52);
					//[ABCD 12 6 53]
					A=cuartaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 12),6,53);
					//[DABC 3 10 54]
					D=cuartaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 3),10,54);
					//[CDAB 10 15 55]
					C=cuartaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 10),15,55);
					//[BCDA 1 21 56]
					B=cuartaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 1),21,56);
					//[ABCD 8 6 57]
					A=cuartaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 6),6,57);
					//[DABC 15 10 58]
					D=cuartaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 15),10,58);
					//[CDAB 6 15 59]
					C=cuartaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 6),15,59);
					//[BCDA 13 21 60]
					B=cuartaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 13),21,60);
					//[ABCD 4 6 61]
					A=cuartaEtapa(A,B,C,D,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 4),6,61);
					//[DABC 11 10 62]
					D=cuartaEtapa(D,A,B,C,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 11),10,62);
					//[CDAB 2 15 63]
					C=cuartaEtapa(C,D,A,B,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 2),15,63);
					//[BCDA 9 21 64]lkkjj
					B=cuartaEtapa(B,C,D,A,obteneValorKbit(mensajeBytesRelleno[j], mensajeBytesRelleno[j+1], 9),21,64);
					
					A = A + AA;
					B = B + BB;
					C = C + CC;
					D = D + DD;
				}
			}
	
			
			byte[] bytes = new byte[16];
			byte[] Abytes=intToBytes(A);
			byte[] Bbytes=intToBytes(B);
			byte[] Cbytes=intToBytes(C);
			byte[] Dbytes=intToBytes(D);
			
			bytes[0]=Dbytes[0]; bytes[1]=Dbytes[1]; bytes[2]=Dbytes[2]; bytes[3]=Dbytes[3];
			bytes[4]=Cbytes[0]; bytes[5]=Cbytes[1]; bytes[6]=Cbytes[2]; bytes[7]=Cbytes[3];
			bytes[8]=Bbytes[0]; bytes[9]=Bbytes[1]; bytes[10]=Bbytes[2]; bytes[11]=Bbytes[3];
			bytes[12]=Abytes[0]; bytes[13]=Abytes[1]; bytes[14]=Abytes[2]; bytes[15]=Abytes[3];
			

			
			
			return toHexString(bytes);
			

	}
	
	

}