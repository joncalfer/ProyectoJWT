package com.WSR.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	private Integer id;
	private String nombreUsuario;
	private String contrasena;
	private String email;
	private String roles;
	private Date ultimoCambioContrasena;
	private Boolean activo;
	private String nombre;
	private String apellidos;
	

	public Usuario() {
	}


	public Usuario(Integer id, String nombreUsuario, String contrasena, String email, Date ultimoCambioContrasena, String roles, Boolean activo, String nombre, String apellidos) {
		super();
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
		this.email = email;
		this.roles = roles;
		this.ultimoCambioContrasena = ultimoCambioContrasena;
		this.activo = activo;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNombreUsuario() {
		return nombreUsuario;
	}


	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}


	public String getContrasena() {
		return contrasena;
	}


	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRoles() {
		return roles;
	}


	public void setRoles(String roles) {
		this.roles = roles;
	}


	public Date getUltimoCambioContrasena() {
		return ultimoCambioContrasena;
	}


	public void setUltimoCambioContrasena(Date ultimoCambioContrasena) {
		this.ultimoCambioContrasena = ultimoCambioContrasena;
	}


	public Boolean getActivo() {
		return activo;
	}


	public void setActivo(Boolean activo) {
		this.activo = activo;
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

	

}
