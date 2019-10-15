import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

public class Grafo {
	String name;		//nombre
	int n;				//nodos todos los grafos
	int m;				//aristas ErdosRenyi
	int d;				//aristas Barabasi
	float r;			//distancia geografico
	float p;			//probabilidad Gilbert
	boolean dirigido;	//dirigido 
	boolean auto;		//auto
	Map<Integer, Nodo> nodo = new HashMap<Integer, Nodo>(); 		//Nodos del Grafo
	Map<Integer, Arista> arista = new HashMap<Integer, Arista>();	//Aristas del Grafo

	int contador_aristas=1;
	
	public Grafo() { //constructor
		System.out.println("Se creo un objeto tipo grafo");
	}
	public Grafo(int n,String name) { //constructor
		this.name=name;
		this.n=n;
		System.out.println("Se creo un objeto tipo grafo");
	}

	
	 public Grafo ErdosRenyi(String name, int n, int m, boolean dirigido, boolean auto) {
		Grafo grafoErdos = new Grafo(n,name);
		grafoErdos.name=name; grafoErdos.n=n; grafoErdos.m=m; grafoErdos.dirigido=dirigido; grafoErdos.auto=auto;
		int[] num = new int[2];     									//Crear una pareja de numeros=arista (a,b)
		boolean aux=true;
		num=pares(n,auto);												//Genera pares aleatorios 
		Arista a1 = new Arista(num[0],num[1]);
		grafoErdos.arista.put(1,a1);
		int t=1;
			do {
				num=pares(n,auto);
				aux=true;
				for(int i=0;i<t;i++) {
					Arista ai = grafoErdos.arista.get(i+1);
					if(ai.a==num[0] && ai.b==num[1] || (dirigido==false && ai.a==num[1] && ai.b==num[0])) {
						aux=false;
						break;
					}	
				}
				if(aux==true) {
					Arista at = new Arista(num[0], num[1]);
					grafoErdos.arista.put(t+1,at);
					t++;
				}
				
			}while(t<m);
			grafoErdos.AtributosNodos(grafoErdos);
			return(grafoErdos);
	 }
	 
	 public Grafo Gilbert(String name, int n, float p, boolean dirigido, boolean auto) {
		 Grafo grafoGilbert = new Grafo(n,name);
		 grafoGilbert.name=name; grafoGilbert.n=n; grafoGilbert.p=p; grafoGilbert.dirigido=dirigido; grafoGilbert.auto=auto;
		 
		 int actual=1;
			Random rand1 = new Random();
			Random rand2 = new Random();
			Random rand3 = new Random();
			for(int i=1; i<=n;i++) {				//Creacion del nodo a (a--b)
				for(int j=i; j<=n;j++) {				//Creacion del nodo b (a--b)
					if(i!=j) {
						if(p>rand1.nextFloat() ) {//prob para a--b 
							//Se crea arista de i-j
							Arista ait = new Arista(i,j);
							grafoGilbert.arista.put(actual,ait);
							actual++;
						}
						if(p>rand2.nextFloat() && dirigido) {//prob para b--a
							//Se crea arista de t-i
							Arista ati = new Arista(j,i);
							grafoGilbert.arista.put(actual,ati);
							actual++;
						}
					}
					if(p>rand3.nextFloat() && auto && i==j) {//prob para a--b 
							//Se crea arista de t-t
							Arista ait = new Arista(i,i);
							grafoGilbert.arista.put(actual,ait);
							actual++;
					}
				}
			}
			grafoGilbert.AtributosNodos(grafoGilbert);
			return(grafoGilbert);
			
	 }
	 
	 public Grafo Geografico(String name, int n, float r, boolean dirigido, boolean auto) {
		Grafo grafoGeo = new Grafo(n,name);
		grafoGeo.name=name; grafoGeo.n=n; grafoGeo.r=r; grafoGeo.dirigido=dirigido; grafoGeo.auto=auto;
		Random rand1 = new Random();
		Random rand2 = new Random();
		int actual=1;
		float[][] coord = new float[n][2];
		//Creacion de coordenadas de los n-nodos
		for(int x=0; x<n;x++) {
			coord[x][0]=rand1.nextFloat();
			coord[x][1]=rand2.nextFloat(); 
		}	
		for(int i=0;i<n;i++) {
			if(auto) {
				Arista aii = new Arista(i+1,i+1);
				grafoGeo.arista.put(actual,aii);
				actual++;
			}
			for (int j=i+1;j<n;j++) {
				if(distancia(coord[i][0],coord[i][1],coord[j][0],coord[j][1])<=r) {
					Arista aij = new Arista(i+1,j+1);
					grafoGeo.arista.put(actual,aij);
					actual++;
					if(dirigido) {
						Arista aji = new Arista(j+1,i+1);
						grafoGeo.arista.put(actual,aji);
						actual++;
					}
				}
			}
		}
		grafoGeo.AtributosNodos(grafoGeo);
		return(grafoGeo);
	 }

	 public Grafo BarabasiAlbert(String name, int n, int d, boolean dirigido, boolean auto) {
		 Grafo grafoBara = new Grafo(n,name);
		 grafoBara.name=name; grafoBara.n=n; grafoBara.d=d; grafoBara.dirigido=dirigido; grafoBara.auto=auto;
		 int[][] auxnodo = new int[n][3];  //matriz de nodos ent,sal,total
			for(int i=0;i<n;i++) {
				auxnodo[i][0]=0; //salida del nodo
				auxnodo[i][1]=0; //entrada del nodo
				auxnodo[i][2]=0; //total salidas entradas
			}
			Random rand1 = new Random();
			Random rand2 = new Random();
			int actual=1;
			for(int i=0;i<n;i++) {//empezamos desde la "creacion" del nodo 2
				for(int j=0;j<=i;j++) { //hacemos el barrido de nodos previos a i
					
					float prs = rand1.nextFloat();
					float pre = rand2.nextFloat();
					
					if(i!=j && prs<=(1-((float)auxnodo[j][2]/d)) && auxnodo[i][2]<d) { // union j--i
						auxnodo[j][0]++; //Crea una salida del nodo j
						auxnodo[i][1]++; //Crea una entrada del nodo i
						auxnodo[j][2]++;
						auxnodo[i][2]++;
						Arista aji = new Arista(j+1,i+1);
						grafoBara.arista.put(actual,aji);
						actual++;
					}
					if(i!=j && dirigido && (pre<=((float)1-(auxnodo[i][2]/d))) && auxnodo[j][2]<d) { //union i--j
						auxnodo[i][0]++; //Crea una salida del nodo i
						auxnodo[j][1]++; //Crea una entrada del nodo j
						auxnodo[j][2]++;
						auxnodo[i][2]++;
						Arista aij = new Arista(i+1,j+1);
						grafoBara.arista.put(actual,aij);
						actual++;
					}
					if(i==j && auto && (prs<=(1-((float)auxnodo[j][2]/d)))) {
						auxnodo[j][0]++; //Crea una salida del nodo j
						auxnodo[j][1]++; //Crea una entrada del nodo j
						auxnodo[j][2]=auxnodo[j][2]+2;
						Arista aii = new Arista(i+1,i+1);
						grafoBara.arista.put(actual,aii);
						actual++;
					}
					
				}
				
			}
			grafoBara.AtributosNodos(grafoBara);
			return(grafoBara);
	 }

	 public Grafo CreateBFS(int s) {
		 Grafo grafoBFS = new Grafo(n,"ArbolBFS_"+this.name);
		 Map<Integer, Nodo> nodoBFS = this.nodo; 		//Nodos del Grafo
		 Map<Integer, Map<Integer,Boolean>> capas = new HashMap<Integer,Map<Integer,Boolean>>(); //Lista de Lista de Nodos (capas)
		 Map<Integer, Boolean> capa0 = new HashMap<Integer,Boolean>(); 		//Lista de nodos de la capa 0
		 int capa_actual=0;													//contador de capas Li
			int contador_aristas=1;												//contador de aristas del nuevo grafoBFS
			Nodo S = nodoBFS.get(s);											//Obtener el nodo S del grafooriginal
			capa0.put(S.numero,true);											//Capa 0 contiene nodo s
			capas.put(capa_actual,capa0);  										//Lista de capas, agregamos capa0
			S.check=true;														//Marcar Nodo s como checado
			nodoBFS.put(S.numero,S);
			int X = 0;		
			do {
				X = 0;
				Map<Integer, Boolean> capai = capas.get(capa_actual); 			// Cerar Lista de Nodos Actual
				Map<Integer, Boolean> capaj = new HashMap<Integer,Boolean>(); 	// Cerar Lista de Nodos Siguiente
				for(Entry<Integer, Boolean> entry:capai.entrySet()){ 			// Todos los nodos de la capa i
					int u = entry.getKey();										//# del nodo u de la capa i
					Nodo U = nodoBFS.get(u);									// Nodo U
					ListIterator<?> lit = U.conexion.listIterator();
					while(lit.hasNext()) {
						int v = (int) lit.next();
						Nodo V = nodoBFS.get(v);
						if(V.check==false) {  									//Si un nodo no ha sido agregado a una capa
							Arista auv = new Arista(u,v);						//Creamos la arista u,v (que existe en el grafo actual)
							grafoBFS.arista.put(contador_aristas,auv);
							contador_aristas++;
							V.check=true;										//Decimos que el nodo ya esta agregado	
							nodoBFS.put(v,V);									//Actualizamos la informacion del nodo del grafo
							capaj.put(v,true);									//agregamos nodo a la capa siguiente
						}
					}
				}
				capa_actual++;
				capas.put(capa_actual,capaj);				//Se agrega la capa Li+1 a la lista de capas
				X = capaj.size();
			}while(X!=0);
			//grafoBFS.AtributosNodos(grafoBFS);
			return(grafoBFS);
	 }
	 
	 public Grafo CreateDFS_R(int u) {
		 	Grafo grafoDFS_R = new Grafo(n,"ArbolDFS_R_"+this.name);
		 	grafoDFS_R.nodo = this.nodo; 		//Nodos del Grafo
			Nodo U = grafoDFS_R.nodo.get(u);										// Obtener nodo U del grafo
			U.check=true;													// Marcar el nodo U como revisado
			grafoDFS_R.nodo.put(u,U);												// Actualizar nodo en el grafo
			ListIterator<?> lit = U.conexion.listIterator();
			while(lit.hasNext()) {
				int v = (int) lit.next();									
				Nodo V = grafoDFS_R.nodo.get(v);									// Nodo V conectado desde U
				if(V.check==false) {										// Si no se ha revisado este nodo
					Arista UV = new Arista(u,v);							// Crea arista u--v
					grafoDFS_R.arista.put(contador_aristas,UV);				// Añade arista al grafo DFS
					contador_aristas=contador_aristas+1;					// Se suma el contador de aristas
					grafoDFS_R=CreateDFS_R(v);	// Se llama CreateDFS con el nodo v<--u
				}
			}
			AtributosNodos(grafoDFS_R);
			return(grafoDFS_R);
	 }
	 
	 public Grafo Dijkstra (int s) {
		 Vector<Integer> S = new Vector<Integer>();					//Conjunto S arbol Dijkstra
		 Grafo grafoD = new Grafo(n,"ArbolDijkstra_"+this.name);
		 grafoD.nodo = this.nodo;
		 grafoD.dirigido=this.dirigido;
		 Nodo U = grafoD.nodo.get(s);
		 U.costo=0;  	U.check=true;
		 grafoD.nodo.put(U.numero,U);
		 S.add(U.numero);
		 int tamaño = 0,cta = 0;
		 do {
			 Vector<float[]> NodosAdy = new Vector <float []>(0);		//Lista de los nodos adyacentes a S
			 ListIterator<?> lit = S.listIterator();
				while(lit.hasNext()) {
					int v = (int) lit.next();						//Obtenemos los ID Nodos del conjunto S
					Nodo V = grafoD.nodo.get(v);					//LLamamos al nodo ID de V
					ListIterator<?> lit2;
					if(this.dirigido) {
						lit2 = V.conexionOutCosto.listIterator();
					}
					else {
						lit2 = V.conexionCosto.listIterator();
					}
					
					while(lit2.hasNext()) {
						float[] w = (float[]) lit2.next();						//Nodos adyacente [nodoID,costo_hacia_el_nodo]
						Nodo W = grafoD.nodo.get((int)w[0]);
						if(W.check==false) {									//Nodo adyacente que no pertenece a S
							float[] na = {V.numero,W.numero,V.costo+w[1]};		//Vector [a,b,c] a-b arista, c el costo
							NodosAdy.add(na);
						}
					}
				}
				tamaño=NodosAdy.size();
				if(tamaño!=0) {
					float[] addToS = NodosAdy.firstElement();				//Considero el primer elemento como el de menor costso
					ListIterator<?> lit3 = NodosAdy.listIterator();
					while(lit3.hasNext()) {
						float[] w = (float[]) lit3.next();				//Nodos adyacente [nodoID,costo_hacia_el_nodo]
						if(addToS[2]>w[2]) {
							addToS[0]=w[0];
							addToS[1]=w[1];
							addToS[2]=w[2];
						}
					}
					Nodo W = grafoD.nodo.get((int)addToS[1]);			//Nodo b que no pertenece a S
					W.check=true;									//Lo marcamos como explorado
					W.costo=addToS[2];								//Le agregamos el costo que tuvo para llegar
					S.add(W.numero);								//Agregamos el ID de b a S
					Nodo V = grafoD.nodo.get((int)addToS[0]);
//					System.out.println(S);
					grafoD.nodo.put(W.numero, W);					//Actualizamos nodo B
					Arista SnoS = new Arista((int)addToS[0],(int)addToS[1]);  //Arista a,b (a pertenece a S y b no pertenece a S)
					SnoS.c=addToS[2]-V.costo;
					grafoD.arista.put(cta,SnoS);					//Actualizamos las aristas del grafoD
					cta=cta+1;
				}
		 }while(tamaño!=0);
		 return(grafoD);
	 }

	 public Grafo CreateDFS_I(int u) {
		 	Grafo grafoDFS_I = new Grafo(n,"ArbolDFS_I_"+this.name);
		 	Map<Integer, Nodo> nodoDFS_I = this.nodo;
		 	Map<Integer, Integer> Memoria = new HashMap<Integer, Integer>();  //Creamos una memoria
			Nodo U = nodoDFS_I.get(u);											// Obtener nodo U del grafo
			U.check=true;														// Marcar el nodo U como revisado
			nodoDFS_I.put(u,U);												// Actualizar nodo en el grafo
			int tamaño = 0;
			int contador_aristas=1;												// Empezar a contar aristas para el arbol
			int ct = 1;														
			Memoria.put(ct, u);													// Almacenar en memoria (1,U)
			do {
				tamaño = U.conexion.size();
				if(tamaño!=0) {		
						int v= U.conexion.firstElement();
						Nodo V = nodoDFS_I.get(v);								//Obtenemos el nodo v del grafo original
						if(V.check==false) {	
							V.check=true;										//Marcamos el nodo V como checado
							nodoDFS_I.put(v,V);								//Refrescamos el nodo dentro del grafo
							u = U.numero;	
							Arista UV = new Arista(u,v);						//Se crea la arista uv
							grafoDFS_I.arista.put(contador_aristas, UV);
							contador_aristas++;
							ct=ct+1;
							Memoria.put(ct,v);
						}
						U.conexion.removeElement(v);						// Se elimina la conexion de U con V		
						nodoDFS_I.put(u,U);								// Se actualiza el valor en el grafo
					}
				else {
					ct=ct-1;
				}
				if(ct!=0) {
					int x = Memoria.get(ct);								// Obtener el nodo de la memoria
					U = nodoDFS_I.get(x);									// Actualizamos el nodo U
				}
				
			}while(ct!=0);
			AtributosNodos(grafoDFS_I);
			return(grafoDFS_I);
	 }
	 
	 public void CreateGV(boolean W, boolean Arbol) {
		 try
			{
			File archivo=new File(name+".gv");
			FileWriter escribir=new FileWriter(archivo,true);
			if(dirigido) {
				escribir.write("di");
			}
			escribir.write("graph {");
			escribir.write("\r\n");
			for(Map.Entry<Integer, Arista> entry:arista.entrySet()){      
		        Arista b=entry.getValue();
		        Nodo A = nodo.get(b.a);
	        	Nodo B = nodo.get(b.b);
	        	if(Arbol == true) {
	        		String Acosto=(String.format("(%.2f)",A.costo));
	        		String Bcosto=(String.format("(%.2f)",B.costo));
	        		if(dirigido==false){ 
	    		        escribir.write(b.a+Acosto+"--"+b.b+Bcosto);
	    		        }
	    		        else{
	    		        	escribir.write(b.a+Acosto+"->"+b.b+Bcosto);
	    		        }
	        	}
	        	else {
	        		if(dirigido==false){ 
	        			escribir.write(b.a+"--"+b.b);
	        		}
	        		else{
	        			escribir.write(b.a+"->"+b.b);
	        		}
	        	}
		        if(W) {
		        	String bc=(String.format("%.2f",b.c));
		        	escribir.write("[label="+bc+"]");
		        }
				escribir.write("\r\n");
		    }
			escribir.write("}");
			escribir.close();
			System.out.println("Se creo: "+name+".gv");
			}
			catch(Exception e)
			{
			System.out.println("Error al escribir");
			}
	 }
	 
	 public void CreateAristasCostos(float min,float max) {
		 this.name=name+"Costos";
		 Random rnd = new Random();
		 for(int i=0;i<n;i++) {
			 Nodo Y = new Nodo(i+1);
			 nodo.put(i+1,Y);
		 }
		 for(Map.Entry<Integer, Arista> entry:arista.entrySet()){
			int key = entry.getKey();
			Arista Acosto=entry.getValue();
			float r1 = (float)(rnd.nextInt(100)+1)/100;
			Acosto.c	=	(max-min)*rnd.nextFloat()+min;
			arista.put(key, Acosto);
			Nodo A = nodo.get(Acosto.a);
	        Nodo B = nodo.get(Acosto.b);
	        if(Acosto.a!=Acosto.b) {
	        	float[] bc = {Acosto.b,Acosto.c};
	        	float[] ac = {Acosto.a,Acosto.c};
    			if(dirigido==false){ 
        			A.conexionCosto.add(bc);
        			B.conexionCosto.add(ac);
        		}
        		//Aristas de la forma A->B
        		if(dirigido) {
        			A.conexionOutCosto.add(bc);
        		}
        	}
	        nodo.put(Acosto.a,A);
	        nodo.put(Acosto.b,B);  
		 }
	 }
	 
//////    FUNCIONES    /////////	 
	 public void AtributosNodos(Grafo G) { 
		 for(int i=0;i<n;i++) {
			 Nodo Y = new Nodo(i+1);
			 G.nodo.put(i+1,Y);
		 }
		 for(Map.Entry<Integer, Arista> entry:G.arista.entrySet()){      
		        Arista X=entry.getValue(); 
		        Nodo A = G.nodo.get(X.a);
	        	Nodo B = G.nodo.get(X.b);
		        // Aristasa de la forma A--B
	        	if(X.a!=X.b) {
	        		if(dirigido==false){ 
	        			A.conexion.add(X.b);
	        			B.conexion.add(X.a);
	        		}
	        		//Aristas de la forma A->B
	        		if(dirigido) {
	        			A.conexionOut.add(X.b);
	        			B.conexionIn.add(X.a);
	        		}
	        	}
		        G.nodo.put(X.a,A);
		        G.nodo.put(X.b,B);
		    }
	 }
	 public void MuestraNodos() {
		 System.out.println("Grafo: "+name);
		 for(int i=1;i<=this.n;i++) {
			 Nodo X = nodo.get(i);
			 System.out.println("Nodo "+X.numero);
			 if(dirigido==false) {
				 System.out.print(X.conexion);
			 }
			 if(dirigido) {
				 System.out.println("Salidas = "+X.conexionOut);
				 System.out.println("Entradas = "+X.conexionIn);
			 }
			 System.out.println("");
		 	}
		 
	 }
	 public void MuestraNodosCosto() {
		 System.out.println("Grafo: "+name);
		 for(int i=1;i<=this.n;i++) {
			 Nodo X = nodo.get(i);
			 System.out.println("Nodo "+X.numero);
			 if(dirigido==false) {
				 System.out.print(X.conexionCosto);
			 }
			 if(dirigido) {
				 System.out.println("Salidas = "+X.conexionOutCosto);
				// System.out.println("Entradas = "+X.conexionIn);
			 }
			 System.out.println("");
		 	}
		 
	 }
	 public static int[] pares(int i, boolean x ){
			int[] ab = new int[2];
			Random rnd = new Random();
			boolean y;
			do {
				y=false;
				for (int k=0; k<2;k++) {
				ab[k] = (rnd.nextInt(i) + 1);
					}
				if(x==false && ab[0]==ab[1]) {
					y=true;
				}
				}while(y==true);
		   return(ab);            
		}
	 public static float distancia(float ax, float ay, float bx, float by){
			float d= (float) Math.sqrt(Math.pow(ax-bx,2)+Math.pow(ay-by,2));
		   return(d);            
		}
}
