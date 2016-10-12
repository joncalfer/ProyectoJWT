package com.WSR.Cotroller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.WSR.Model.Usuario;
import com.WSR.Model.UsuarioSeguro;
import com.WSR.Model.UsuarioWeb;
import com.WSR.Repository.UsuarioRepository;
import com.WSR.Seguridad.TokenUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("${javatab.route.autenticacion}")
public class AutenticacionController {


  @Value("${javatab.token.header}")
  private String tokenHeader;

  @Autowired
  private AuthenticationManager authenticationManager;
  
 

  @Autowired
  private TokenUtils tokenUtils;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private UsuarioRepository usuarioRepo;

  @RequestMapping(value = "/iniciarSesion", method = RequestMethod.POST)
  public  Map<String, Object> autenticar(@RequestBody Map<String, Object> usuarioMap, Device dispositivo) throws AuthenticationException {
  
	  Map<String, Object> response = new LinkedHashMap<String, Object>();
	  
	  try {
		// Se realiza la autenticacion
		    Authentication authentication = this.authenticationManager.authenticate(
		      new UsernamePasswordAuthenticationToken(usuarioMap.get("nombreUsuario").toString(),
		    		  									usuarioMap.get("contrasena").toString()));
		    
		    SecurityContextHolder.getContext().setAuthentication(authentication);

		    // Actualizar la contraseña después de la autenticación para que podamos generar el token
		    UserDetails userDetails = this.userDetailsService.loadUserByUsername(usuarioMap.get("nombreUsuario").toString());
		    String token = this.tokenUtils.generarToken(userDetails, dispositivo);
		    
		    UsuarioWeb usuario = new UsuarioWeb(((UsuarioSeguro)userDetails).getNombre(), ((UsuarioSeguro)userDetails).getApellidos(), token);

		    response.put("message", "Usuario autenticado correctamente");
			response.put("status", 200);
			response.put("usuario", usuario);
			
			
			
			
	} catch (BadCredentialsException e) {
		response.put("message", "Nombre de usuario o contraseña incorrectos");
		response.put("status", 200);
		response.put("token", null);
		response.put("usuario", null);
	}
	  catch (Exception ex) {
		  response.put("message", "Ha ocurrido un error");
			response.put("status", 404);
			response.put("token", null);
			response.put("usuario", null);
	  }
    
	
	return response;
  }

  @RequestMapping(value = "${javatab.route.authentication.refrescar}", method = RequestMethod.GET)
  public Map<String, Object> refrescarToken(HttpServletRequest request) {
	Map<String, Object> response = new LinkedHashMap<String, Object>();
	  
    String token = request.getHeader(this.tokenHeader);
    String nombreUsuario = this.tokenUtils.getNombreUsuarioDelToken(token);
    UsuarioSeguro usuario = (UsuarioSeguro) this.userDetailsService.loadUserByUsername(nombreUsuario);
    if (this.tokenUtils.puedeSerRefrescadoElToken(token, usuario.getUltimoCambioContrasena())) {
    	String tokenRefrescado = this.tokenUtils.refrescarToken(token);
    	
      	response.put("message", "Token refrescado correctamente");
  		response.put("status", 200);
  		response.put("token", tokenRefrescado);
  		
    } else {
    	response.put("message", "No se pudo refrescar el token");
		response.put("status", 404);
		response.put("token", null);
    }
    return response;
  }

    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public Map<String, Object> registrarUsuario(@RequestBody Map<String, Object> usuarioMap) throws AuthenticationException {
    
    	Map<String, Object> response = new LinkedHashMap<String, Object>();
    	
        Date fechaActual = new Date(12-12-2-12);
        PasswordEncoder encriptadorContrasena = new BCryptPasswordEncoder();
        String contrasenaHash = encriptadorContrasena.encode(usuarioMap.get("contrasena").toString());
        
        Usuario usuario = new Usuario(0, usuarioMap.get("nombreUsuario").toString(), contrasenaHash, 
        		usuarioMap.get("email").toString(), fechaActual, usuarioMap.get("roles").toString(), true, 
        		usuarioMap.get("nombre").toString(), usuarioMap.get("apellidos").toString());
        
        
        try {
        usuarioRepo.save(usuario);
		response.put("message", "Usuario registrado correctamente");
		response.put("status", 200);
		response.put("estudiante", usuario);
	} catch (Exception ex) {
		response.put("message", ex.getMessage());
		response.put("status", 500);
		response.put("estudiante", null);
	}

	return response;
    }
    
    @RequestMapping(value = "/cerrarSesion", method = RequestMethod.GET)
    public Map<String, Object> cerrarSesion(ServletRequest request) throws Exception {
    
    	Map<String, Object> response = new LinkedHashMap<String, Object>();
    	
    	
    		HttpServletRequest httpRequest = (HttpServletRequest) request;
            String token = httpRequest.getHeader(this.tokenHeader);
        	this.tokenUtils.insertarTokenInvalido(token);
        	response.put("message", "Sesion cerrada");
    		response.put("status", 200);
    		
	

	return response;
    }

}
