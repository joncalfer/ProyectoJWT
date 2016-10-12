
package com.WSR.Model;

import org.springframework.stereotype.Component;

@Component
public class NodoSimple<T> {

    private T valor;
    private NodoSimple siguiente;

    public NodoSimple() {
    }

    public NodoSimple(T valor) {
        this.valor = valor;
    }

    public NodoSimple getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoSimple siguiente) {
        this.siguiente = siguiente;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
}
