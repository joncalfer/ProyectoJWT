package com.WSR.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.WSR.Model.Usuario;
import com.WSR.Model.UsuarioSeguro;
import com.WSR.Repository.UsuarioRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsuarioRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
    Usuario usuario = this.userRepository.findByNombreUsuario(nombreUsuario);

    if (usuario == null) {
      throw new UsernameNotFoundException(String.format("No se encontró el usuario con el nombre '%s'.", nombreUsuario));
    } else {
      return new UsuarioSeguro(usuario.getId(), 
    		  usuario.getNombreUsuario(),
    		  usuario.getContrasena(), 
    		  usuario.getEmail(), 
    		  usuario.getUltimoCambioContrasena(), AuthorityUtils.commaSeparatedStringToAuthorityList(usuario.getRoles()));

    }
  }

}
