package com.JWT.Repositorio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.JWT.Modelo.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	
	//este no sirve
	//@Query("select u from User u where u.email = ?1")
	 public Usuario findByNombreUsuario(String nombreUsuario);
	 
	 //Todos los métodos comienzan por findBy y una propiedad del objeto buscado, 
	 //seguido de más opciones que se especifican mediante palabras clave. 
	// Las palabras clave son: And, Or, Between, LessThan, GreaterThan, After, 
	// Before, IsNull, (Is)NotNull, Like, NotLike, StartingWith, EndingWith, 
	// Containing, OrderBy, Not, In, NotIn, True, False, IgnoreCase.

	 
	//findByLastnameAndFirstname … where x.lastname = ?1 and x.firstname = ?2 
	//findByAgeOrderByLastnameDesc … where x.age = ?1 order by x.lastname desc findByActiveTrue … where x.active = true
	 
	 //https://dzone.com/articles/calling-stored-procedures-from-spring-data-jpa
	 //Este si funciona
	 //http://stackoverflow.com/questions/26551831/call-stored-procedure-with-repository
}
