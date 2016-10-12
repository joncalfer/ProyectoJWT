package com.JWT.Modelo;

public class UsuarioWeb {
	
	private String nombre;
	private String apellidos;
	private String token;
	
	public UsuarioWeb(String nombre, String apellidos, String token) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.token = token;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
	
}
