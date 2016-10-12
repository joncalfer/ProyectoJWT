package com.JWT.Modelo;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "curso")
public class Curso {
	
	@Id
	private Integer id;
	private String nombre;
	private String siglas;
	private int profesores_id;

	public Curso() {
	}
	
	

	public Curso(Integer id, String nombre, String siglas, int profesores_id) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.siglas = siglas;
		this.profesores_id = profesores_id;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSiglas() {
		return siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}


	public int getProfesores_id() {
		return profesores_id;
	}

	public void setProfesores_id(int profesores_id) {
		this.profesores_id = profesores_id;
	}

	

}
