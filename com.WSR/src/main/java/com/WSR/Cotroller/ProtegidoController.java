package com.WSR.Cotroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${javatab.route.protected}")
public class ProtegidoController {

  /**
  Este es un ejemplo de algunos de los diferentes tipos de restricción granular para end-points. Puede utilizar las expresiones SPEL incorporadas
   en @PreAuthorize tales como 'hasRole ()' para determinar si un usuario tiene acceso. Sin embargo, si usted requiere la lógica más allá de los métodos
   Spring ofrece a continuación, se puede encapsular en un servicio y registrarlo como un bean de usarlo dentro de la anotación como
   se demuestra a continuación con 'SecurityService
  **/
  @RequestMapping(method = RequestMethod.GET)
  //@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
  //@PreAuthorize("@securityService.hasProtectedAccess()")
  public ResponseEntity<?> getDaHoney() {
    return ResponseEntity.ok(":O");
  }

}
