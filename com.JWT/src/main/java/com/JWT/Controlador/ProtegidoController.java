package com.JWT.Controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${javatab.route.protected}")
public class ProtegidoController {

 
  //se pueden hacer combinaciones con los roles
  @RequestMapping(method = RequestMethod.GET)
  //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
  //@PreAuthorize("hasAuthority('ADMIN') and hasAuthority('USER')")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> getDaHoney() {
    return ResponseEntity.ok(":O");
  }

}
