package com.JWT.Seguridad;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.JWT.Modelo.ListaEnlazadaSimple;
import com.JWT.Modelo.TokenInvalido;
import com.JWT.Modelo.UsuarioSeguro;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

  private final Logger logger = Logger.getLogger(this.getClass());

  private final String AUDIENCE_UNKNOWN   = "unknown";
  private final String AUDIENCE_WEB       = "web";
  private final String AUDIENCE_MOBILE    = "mobile";
  private final String AUDIENCE_TABLET    = "tablet";
  
  ListaEnlazadaSimple<TokenInvalido> listaId;
  int contadorIdToken;
  long ultimoLimpidoBlacklist;
  
  public TokenUtils() {
	  ultimoLimpidoBlacklist = System.currentTimeMillis();
	  listaId = new ListaEnlazadaSimple<>(); 
  }
  public int obtenerSiguiente() {
		return this.contadorIdToken++;
	}
  
  public void insertarTokenInvalido(String token) throws Exception {
	  TokenInvalido invalido = new TokenInvalido();
	  invalido.setExpiracion(getFechaExpiracionDelToken(token));
	  invalido.setId(getIdDelToken(token));
	  this.listaId.add(invalido);
	  if(listaId.size() > 1) {
		  limpiarListaNegra();
	  }
	 
	}
  
  private boolean ContieneTokenInvalido(String token) {
	  int idToken = getIdDelToken(token);
	  for(int i=0; i < listaId.size(); i++) {
		  try {
			if(listaId.get(i).getId() == idToken) {
				  return true;
				  
			  }
		} catch (Exception e) {
			return false;
		}
		  
  }
	  return false;
  }
  
  
  private void limpiarListaNegra() throws Exception {
	  for(int i=0; i < listaId.size(); i++) {
		  if(listaId.get(i).getExpiracion().before(this.generarFechaActual())) {
			  listaId.remove(i);
		  }
	  }
	 
	  long comparacion = (System.currentTimeMillis() - this.ultimoLimpidoBlacklist) / 60000;
	  if( comparacion >  this.expiracionDelToken*2) {
		  this.contadorIdToken = 0;
	  }
  }

  @Value("${javatab.token.secret}")
  private String claveSecreta;

  @Value("${javatab.token.expiration}")
  private Long expiracionDelToken;

  public String getNombreUsuarioDelToken(String token) {
    String nombreUsuario;
    try {
      final Claims claims = this.getClaimsDelToken(token);
      nombreUsuario = claims.getSubject();
    } catch (Exception e) {
    	nombreUsuario = null;
    }
    return nombreUsuario;
  }

  public Date getFechaCreacionDelToken(String token) {
    Date creado;
    try {
      final Claims claims = this.getClaimsDelToken(token);
      creado = new Date((Long) claims.get("creado"));
    } catch (Exception e) {
    	creado = null;
    }
    return creado;
  }

  public Date getFechaExpiracionDelToken(String token) {
    Date expiracion;
    try {
      final Claims claims = this.getClaimsDelToken(token);
      expiracion = claims.getExpiration();
    } catch (Exception e) {
    	expiracion = null;
    }
    return expiracion;
  }

  public String getAudienciaDelToken(String token) {
    String audiencia;
    try {
      final Claims claims = this.getClaimsDelToken(token);
      audiencia = (String) claims.get("audiencia");
    } catch (Exception e) {
    	audiencia = null;
    }
    return audiencia;
  }
  
  public int getIdDelToken(String token) {
	    int id;
	    try {
	      final Claims claims = this.getClaimsDelToken(token);
	      id = (int) claims.get("id");
	    } catch (Exception e) {
	    	id = -1;
	    }
	    return id;
	  }

  private Claims getClaimsDelToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
        .setSigningKey(this.claveSecreta)
        .parseClaimsJws(token)
        .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private Date generarFechaActual() {
    return new Date(System.currentTimeMillis());
  }

  private Date generarFechaExpiracion() {
    return new Date(System.currentTimeMillis() + this.expiracionDelToken * 60000);
  }

  private Boolean tokenCaducado(String token) {
    final Date expiracion = this.getFechaExpiracionDelToken(token);
    return expiracion.before(this.generarFechaActual());
  }

  private Boolean creadoAntesDelUltimoCambioContrasena(Date creado, Date ultimoCambioContrasena) {
    return (ultimoCambioContrasena != null && creado.before(ultimoCambioContrasena));
  }

  private String generarAudiencia(Device dispositivo) {
    String audiencia = this.AUDIENCE_UNKNOWN;
    if (dispositivo.isNormal()) {
    	audiencia = this.AUDIENCE_WEB;
    } else if (dispositivo.isTablet()) {
    	audiencia = AUDIENCE_TABLET;
    } else if (dispositivo.isMobile()) {
    	audiencia = AUDIENCE_MOBILE;
    }
    return audiencia;
  }

  private Boolean ignorarExpriracionDelToken(String token) {
    String audiencia = this.getAudienciaDelToken(token);
    return (this.AUDIENCE_TABLET.equals(audiencia) || this.AUDIENCE_MOBILE.equals(audiencia));
  }

  public String generarToken(UserDetails userDetails, Device dispositivo) {
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("id", obtenerSiguiente());
    claims.put("sub", userDetails.getUsername());
    claims.put("audiencia", this.generarAudiencia(dispositivo));
    claims.put("creado", this.generarFechaActual());
    return this.generarToken(claims);
  }

  private String generarToken(Map<String, Object> claims) {
    return Jwts.builder()
      .setClaims(claims)
      .setExpiration(this.generarFechaExpiracion())
      .signWith(SignatureAlgorithm.HS512, this.claveSecreta)
      .compact();
  }

  public Boolean puedeSerRefrescadoElToken(String token, Date ultimoCambioContrasena) {
    final Date creado = this.getFechaCreacionDelToken(token);
    return (!(this.creadoAntesDelUltimoCambioContrasena(creado, ultimoCambioContrasena)) && (!(this.tokenCaducado(token)) || this.ignorarExpriracionDelToken(token)));
  }

  public String refrescarToken(String token) {
    String tokenRefrescado;
    try {
      final Claims claims = this.getClaimsDelToken(token);
      claims.put("creado", this.generarFechaActual());
      tokenRefrescado = this.generarToken(claims);
    } catch (Exception e) {
    	tokenRefrescado = null;
    }
    return tokenRefrescado;
  }

  public Boolean validarToken(String token, UserDetails userDetails) {
	    UsuarioSeguro usuario = (UsuarioSeguro) userDetails;
	    final String nombreUsuario = this.getNombreUsuarioDelToken(token);
	    final Date creado = this.getFechaCreacionDelToken(token);
	    final Date expiration = this.getFechaExpiracionDelToken(token);
	    return (!ContieneTokenInvalido(token) && nombreUsuario.equals(usuario.getUsername()) && !(this.tokenCaducado(token)) && !(this.creadoAntesDelUltimoCambioContrasena(creado, usuario.getUltimoCambioContrasena())));
	  }

}
