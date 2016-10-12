
package com.WSR.Model;

import org.springframework.stereotype.Component;

@Component
public class ListaEnlazadaSimple<T> {

	private int contador;
	private NodoSimple<T> cabeza;
	private NodoSimple<T> ultimo;

	public int size() {
		return this.contador;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void add(T valor) {
		if (isEmpty()) {
			addFirst(valor);
		} else {
			NodoSimple<T> nuevo = new NodoSimple<T>(valor);
			ultimo.setSiguiente(nuevo);
			ultimo = nuevo;
			contador++;
		}
	}

	public void add(int i, T valor) throws Exception {
		if (i < 0 || i > size()) {
			throw new Exception("Indice no permitido");
		} else {
			if (i == 0) {
				addFirst(valor);
			} else if (i == size()) {
				add(valor);
			} else {
				NodoSimple<T> nuevo = new NodoSimple<T>(valor);
				NodoSimple<T> temp = cabeza;

				for (int j = 1; j < i; j++) {
					temp = temp.getSiguiente();
				}

				nuevo.setSiguiente(temp.getSiguiente());
				temp.setSiguiente(nuevo);
				contador++;
			}
		}
	}

	public void addFirst(T valor) {
		NodoSimple<T> nuevo = new NodoSimple<T>(valor);
		if (isEmpty()) {
			cabeza = ultimo = nuevo;
		} else {
			nuevo.setSiguiente(cabeza);
			cabeza = nuevo;
		}
		contador++;
	}

	public boolean remove(T valor) throws Exception {

		NodoSimple<T> temp = cabeza;

		for (int j = 0; j < size() - 1; j++) {
			if (temp.getValor() == valor) {
				try {
					remove(j);
					return true;
				} catch (Exception ex) {
					return false;
				}
			}
			temp = temp.getSiguiente();
		}

		return false;

	}

	public T remove(int indice) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			if (indice < 0 || indice >= size()) {
				throw new Exception("indice inválido");
			} else {
				if (indice == 0) {
					T temp = First();
					removeFirst();
					return temp;
				} else {
					if (indice == size() - 1) {
						T temp = Last();
						removeLast();
						return temp;
					} else {
						NodoSimple<T> temp = cabeza;
						T tempRetorno;
						for (int j = 1; j < indice; j++) {
							temp = temp.getSiguiente();
						}
						tempRetorno = (T) temp.getSiguiente().getValor();
						temp.setSiguiente(temp.getSiguiente().getSiguiente());
						contador--;
						return tempRetorno;
					}
				}
			}
		}
	}

	public void removeLast() throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		}
		if (size() == 1) {
			clear();
		} else {
			NodoSimple<T> temp = cabeza;
			for (int i = 0; i < size() - 2; i++) {
				temp = temp.getSiguiente();
			}
			temp.setSiguiente(null);
			ultimo = temp;
			contador--;
		}
	}

	public void removeFirst() throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		}
		if (size() == 1) {
			clear();
		} else {
			cabeza = cabeza.getSiguiente();
			contador--;
		}
	}

	public void clear() {
		cabeza = ultimo = null;
		contador = 0;
	}

	public T get(int indice) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			if (indice < 0 || indice > size()) {
				throw new Exception("Indice inválido");
			} else {
				NodoSimple<T> temp = cabeza;
				for (int i = 0; i < indice; i++) {
					temp = temp.getSiguiente();
				}
				return temp.getValor();
			}
		}
	}

	public T set(int indice, T val) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			if (indice < 0 || indice > size()) {
				throw new Exception("Indice inválido");
			} else {
				NodoSimple<T> temp = cabeza;
				for (int i = 1; i < indice; i++) {
					temp = temp.getSiguiente();
				}
				T tempRetorna = temp.getValor();
				temp.setValor(val);
				return tempRetorna;
			}
		}
	}

	public boolean contains(T elemento) {
		NodoSimple<T> temp = cabeza;
		if (isEmpty()) {
			return false;
		} else {
			while (temp != null) {
				if (temp.getValor() == elemento) {
					return true;
				}
				temp = temp.getSiguiente();
			}
			return false;
		}
	}

	public T First() throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			return cabeza.getValor();
		}
	}

	public T Last() throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			return ultimo.getValor();
		}
	}

	public int indexOf(T val) throws Exception {

		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			NodoSimple<T> temp = cabeza;
			int indice = 0;
			while (temp != null) {
				if (temp.getValor() == val) {
					return indice;
				}
				temp = temp.getSiguiente();
				indice++;
			}
			return -1;
		}

	}

	public int lastIndexOf(T val) throws Exception {
		int retorno = -1;
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			NodoSimple<T> temp = cabeza;
			int indice = 0;
			while (temp != null) {
				if (temp.getValor() == val) {
					retorno = indice;
				}
				temp = temp.getSiguiente();
				indice++;
			}
			return retorno;
		}
	}

	public T[] toArray() throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		} else {
			Object[] retorno = new Object[size()];
			NodoSimple<T> temp = cabeza;
			int ind = 0;
			while (temp.getSiguiente() != null) {
				retorno[ind] = temp.getValor();
				temp = temp.getSiguiente();
				ind++;
			}
			return (T[]) retorno;
			// probar el caseo de genericos como arreglo
		}
	}

	public T[] toArray(int indice1, int indice2) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		}

		if (indice1 < 0 || indice2 < 0 || indice1 <= size() || indice2 < size()) {
			throw new Exception("Indice inválido");
		}
		ListaEnlazadaSimple<T> temp = subList(indice1, indice2);
		return temp.toArray();
	}

	public ListaEnlazadaSimple<T> subList(int indice1, int indice2) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		}

		if (indice1 < 0 || indice2 < 0 || indice1 <= size() || indice2 < size()) {
			throw new Exception("Indice inválido");
		}
		ListaEnlazadaSimple retorno = new ListaEnlazadaSimple();
		for (int i = indice1; i <= indice2; i++) {
			retorno.add(get(i));
		}
		return retorno;
	}

	public void Swap(int indice1, int indice2) throws Exception {
		if (isEmpty()) {
			throw new Exception("Lista vacía");
		}

		if (indice1 < 0 || indice2 < 0 || indice1 <= size() || indice2 < size()) {
			throw new Exception("Indice inválido");
		}
		T temp1 = get(indice1);
		T temp2 = get(indice2);
		set(indice2, temp1);
		set(indice1, temp2);
	}

}
