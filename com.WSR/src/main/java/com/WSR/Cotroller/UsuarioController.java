package com.WSR.Cotroller;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.WSR.Model.Usuario;
import com.WSR.Repository.UsuarioRepository;


@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {
	
	
	
	@Autowired
	UsuarioRepository usuarioRepo;

	@RequestMapping(method = RequestMethod.POST, value= "/crearUsuario")
	
	public Map<String, Object> CrearUsuario(@RequestBody Map<String, Object> usuarioMap) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {
			Usuario usuario = new Usuario();
			
			
			usuario.setId(0);
			usuario.setNombreUsuario(usuarioMap.get("nombreUsuario").toString());
			
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    String hashedPassword = passwordEncoder.encode(usuarioMap.get("contrasena").toString());
			
			usuario.setContrasena(hashedPassword);
			usuario.setEmail((usuarioMap.get("correo").toString()));
			usuario.setRoles((usuarioMap.get("roles").toString()));
			
			Date currentTime = new Date(12-12-2-12);
			usuario.setUltimoCambioContrasena(currentTime);
			usuario.setActivo(true);
			usuario.setNombre((usuarioMap.get("nombre").toString()));
			usuario.setApellidos((usuarioMap.get("apellidos").toString()));

			usuarioRepo.save(usuario);
			response.put("message", "Usuario creado correctamente");
			response.put("status", 200);
			response.put("usuario", usuario);
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("usuario", null);
		}

		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/obtener/{idUsuario}")
	public Map<String, Object> Obtener(@PathVariable("idUsuario") Integer idUsuario) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Usuario usuario = new Usuario();

		try {

			usuario = usuarioRepo.findOne(idUsuario);

			if (usuario != null) {

				response.put("message", "Usuario encontrado");
				response.put("status", 200);
				response.put("usuario", usuario);
			} else {
				response.put("message", "Usuario no encontrado");
				response.put("status", 404);
				response.put("usuario", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("usuario", null);
		}

		return response;

	}
	
	@RequestMapping( method = RequestMethod.GET, value="/nombre/{nombreUsuario}")
	public Map<String, Object> ObtenerPorNombre(@PathVariable("nombreUsuario") String nombreUsuario) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Usuario usuario = new Usuario();

		try {

			usuario = usuarioRepo.findByNombreUsuario(nombreUsuario);

			if (usuario != null) {

				response.put("message", "Usuario encontrado");
				response.put("status", 200);
				response.put("usuario", usuario);
			} else {
				response.put("message", "Usuario no encontrado");
				response.put("status", 404);
				response.put("usuario", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("usuario", null);
		}

		return response;

	}
	
	
	@RequestMapping(method = RequestMethod.GET, value="/listar")
	public Map<String, Object> listarUsuarios() {
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {

			List<Usuario> listaUsuarios = (List<Usuario>) usuarioRepo.findAll();

			if (listaUsuarios != null && listaUsuarios.size() > 0) {

				response.put("message", "Usuarios encontrados: "+  listaUsuarios.size());
				response.put("status", 200);
				response.put("usuarios", listaUsuarios);
			} else {
				response.put("message", "No hay usuarios registrados");
				response.put("status", 404);
				response.put("usuarios", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("usuarios", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/actualizar/{idUsuario}")
	public Map<String, Object> EditarUsuario(@RequestBody Map<String, Object> usuarioMap, @PathVariable("idUsuario") Integer idUsuario) {
		
		 Map<String, Object> response = new HashMap<String, Object>();
		Usuario usuario;
        
        try {
        	
        	usuario = usuarioRepo.findOne(idUsuario);
        	
        	if(usuario != null) {
        		
        		if(usuarioMap.containsKey("nombreUsuario")) {
        			usuario.setNombreUsuario(usuarioMap.get("nombreUsuario").toString());
        		}
    			if(usuarioMap.containsKey("contrasena")) {
    				PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        		    String hashedPassword = passwordEncoder.encode(usuarioMap.get("contrasena").toString());
        			usuario.setContrasena(hashedPassword);
        			Date currentTime = new Date(12-12-2-12);
        			usuario.setUltimoCambioContrasena(currentTime);
    			}
    			if(usuarioMap.containsKey("email")) {
    				usuario.setEmail((usuarioMap.get("email").toString()));
    			}
    			if(usuarioMap.containsKey("roles")) {
    				usuario.setRoles((usuarioMap.get("roles").toString()));
    			}
    			if(usuarioMap.containsKey("nombre")) {
    				usuario.setNombre((usuarioMap.get("nombre").toString()));
    			}
    			if(usuarioMap.containsKey("apellidos")) {
    				usuario.setApellidos((usuarioMap.get("apellidos").toString()));
    			}
    			if(usuarioMap.containsKey("activo")) {
    				Boolean activo = Boolean.parseBoolean(usuarioMap.get("activo").toString());
    				usuario.setActivo(activo);
    			}
    			
        		
        		response.put("message", "Usuario actualizado");
        		response.put("status",200);
				response.put("usuario", usuarioRepo.save(usuario));
				
        	}else 
			{
        		response.put("message", "Usuario no encontrado");
				response.put("status", 404);
				response.put("usuario", null);
			}
			
		} catch (Exception ex) {
			 response.put("message", ex.getMessage());
             response.put("status", 500);
             response.put("usuario", null);
         }

       return response;
         
	}

	
	@RequestMapping(method = RequestMethod.DELETE, value="/{idUsuario}")
	public Map<String, Object> BorrarUsuario(@PathVariable("idUsuario") Integer idUsuario) {
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try{
			usuarioRepo.delete(idUsuario);
            response.put("message", "Usuario eliminado correctamente");
            response.put("status", 200);
            response.put("usuario", idUsuario);
        }
        catch (Exception ex){
            response.put("message", ex.getMessage());
            response.put("status", 500);
            response.put("usuario", idUsuario);
        }

      return response;

	}
	
}




