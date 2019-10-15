import java.util.*;

public class Nodo {
	Vector<Integer> conexionOut = new Vector<Integer>();
	Vector<Integer> conexionIn = new Vector<Integer>();
	Vector<Integer> conexion = new Vector<Integer>();
	
	Vector<float[]> conexionCosto = new Vector<float[]>();
	Vector<float[]> conexionOutCosto = new Vector<float[]>();
	int numero;
	int ent,sal,tot;
	float costo;
	boolean check=false;
	
	public Nodo(int i,int sal, int ent) {
		this.numero = i+1;
		this.ent = ent;
		this.sal = sal;
		this.tot=ent+sal;
		//System.out.println("Se creo un objeto tipo nodo, el nodo es: "+i);
	}
	public Nodo(int i,int tot) {
		this.numero = i+1;
		this.tot = tot;
		//System.out.println("Se creo un objeto tipo nodo, el nodo es: "+i);
	}
	public Nodo(int i) {
		this.numero = i;
		//System.out.println("Se creo un objeto tipo nodo, el nodo es: "+i);
	}
	public Nodo() {
		
	}

}