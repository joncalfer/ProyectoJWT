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
	

	public Usuario() {
	}


	public Usuario(Integer id, String nombreUsuario, String contrasena, String email, Date ultimoCambioContrasena, String roles, Boolean activo) {
		super();
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
		this.email = email;
		this.roles = roles;
		this.ultimoCambioContrasena = ultimoCambioContrasena;
		this.activo = activo;
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

	

}
