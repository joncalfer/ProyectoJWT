package com.WSR.Repository;

import org.springframework.data.repository.CrudRepository;


import com.WSR.Model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	
	 public Usuario findByNombreUsuario(String nombreUsuario);

}
