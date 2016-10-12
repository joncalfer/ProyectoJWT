package com.JWT.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class UsuarioSeguro implements UserDetails {

  private int id;
  private String nombreUsuario;
  private String contrasena;
  private String email;
  private Date ultimoCambioContrasena;
  private Collection<? extends GrantedAuthority> roles;
  private Boolean accountNonExpired = true;
  private Boolean accountNonLocked = true;
  private Boolean credentialsNonExpired = true;
  private Boolean activo = true;
  private String nombre;
  private String apellidos;

  public UsuarioSeguro() {
    super();
  }

  public UsuarioSeguro(int id, String nombreUsuario, String contrasena, String email, Date ultimoCambioContrasena, Collection<? extends GrantedAuthority> roles, String nombre, String apellidos) {
    this.setId(id);
    this.setNombreUsuario(nombreUsuario);
    this.setContrasena(contrasena);
    this.setEmail(email);
    this.setUltimoCambioContrasena(ultimoCambioContrasena);
    this.setAuthorities(roles);
    this.nombre = nombre;
    this.apellidos = apellidos;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

 

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }



  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @JsonIgnore
  public Date getUltimoCambioContrasena() {
    return this.ultimoCambioContrasena;
  }

  public void setUltimoCambioContrasena(Date ultimoCambioContrasena) {
    this.ultimoCambioContrasena = ultimoCambioContrasena;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  public void setAuthorities(Collection<? extends GrantedAuthority> roles) {
    this.roles = roles;
  }

  @JsonIgnore
  public Boolean getAccountNonExpired() {
    return this.accountNonExpired;
  }

  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.getAccountNonExpired();
  }

  @JsonIgnore
  public Boolean getAccountNonLocked() {
    return this.accountNonLocked;
  }

  public void setAccountNonLocked(Boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.getAccountNonLocked();
  }

  @JsonIgnore
  public Boolean getCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.getCredentialsNonExpired();
  }

  @JsonIgnore
  public Boolean getActivo() {
    return this.activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  @Override
  public boolean isEnabled() {
    return this.getActivo();
  }

  @JsonIgnore
@Override
public String getPassword() {
	
	return this.contrasena;
}

@Override
public String getUsername() {
	
	return this.nombreUsuario;
}

public String getApellidos() {
	return apellidos;
}

public void setApellidos(String apellidos) {
	this.apellidos = apellidos;
}

public String getNombreUsuario() {
	return nombreUsuario;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}



}
