package com.WSR.Cotroller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.WSR.Model.Profesor;

import com.WSR.Repository.ProfesorRepository;

@RestController
@RequestMapping("/Profesores")
public class ProfesorController {
	
	
	
	@Autowired
	ProfesorRepository profeRepo;

	@RequestMapping(method = RequestMethod.POST)
	
	public Map<String, Object> CrearProfesor(@RequestBody Map<String, Object> profeMap) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {
			Profesor profesor = new Profesor();
			profesor.setId(0);
			profesor.setNombre(profeMap.get("nombre").toString());
			profesor.setApellidos((profeMap.get("apellidos").toString()));
			profesor.setCorreo((profeMap.get("correo").toString()));
			
			String fechaNacimiento = (profeMap.get("fechaNacimiento").toString());
			profesor.setFechaNacimiento(DateFormatter(fechaNacimiento));
			profesor.setActivo(true);
			

			profeRepo.save(profesor);
			response.put("message", "Profesor creado correctamente");
			response.put("status", 200);
			response.put("profesor", profesor);
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("profesor", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value="/{idProfesor}")
	public Map<String, Object> Obtener(@PathVariable("idProfesor") Integer idProfesor) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Profesor profesor = new Profesor();

		try {

			profesor = profeRepo.findOne(idProfesor);

			if (profesor != null) {

				response.put("message", "Profesor encontrado");
				response.put("status", 200);
				response.put("profesor", profesor);
			} else {
				response.put("message", "Profesor no encontrado");
				response.put("status", 404);
				response.put("profesor", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("profesor", null);
		}

		return response;

	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> listarProfesores() {
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		

		try {

			List<Profesor> listaProfesores = (List<Profesor>) profeRepo.findAll();

			if (listaProfesores != null && listaProfesores.size() > 0) {

				response.put("message", "Profesores encontrados: "+  listaProfesores.size());
				response.put("status", 200);
				response.put("profesor", listaProfesores);
			} else {
				response.put("message", "No hay profesores registrados");
				response.put("status", 404);
				response.put("profesor", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("profesor", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{idProfe}")
	public Map<String, Object> EditarProfe(@RequestBody Map<String, Object> profeMap, @PathVariable("idProfe") Integer idProfe) {
		
		 Map<String, Object> response = new HashMap<String, Object>();
        Profesor profesor;
        
        try {
        	
        	profesor = profeRepo.findOne(idProfe);
        	
        	if(profesor != null) {
        		
        		profesor.setNombre(profeMap.get("nombre").toString());
    			profesor.setApellidos((profeMap.get("apellidos").toString()));
    			profesor.setCorreo((profeMap.get("correo").toString()));
    			
    			String fechaNacimiento = (profeMap.get("fechaNacimiento").toString());
    			profesor.setFechaNacimiento(DateFormatter(fechaNacimiento));
    			Boolean activo = Boolean.parseBoolean(profeMap.get("activo").toString());
    			profesor.setActivo(activo);
        		
        		response.put("message", "Profesor actualizado");
        		response.put("status",200);
				response.put("profesor", profeRepo.save(profesor));
				
        	}else 
			{
        		response.put("message", "Profesor no encontrado");
				response.put("status", 404);
				response.put("profesor", null);
			}
			
		} catch (Exception ex) {
			 response.put("message", ex.getMessage());
             response.put("status", 500);
             response.put("profesor", null);
         }

       return response;
         
	}

	
	@RequestMapping(method = RequestMethod.DELETE, value="/{idProfesor}")
	public Map<String, Object> BorrarProfesor(@PathVariable("idProfesor") Integer idProfesor) {
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try{
			profeRepo.delete(idProfesor);
            response.put("message", "Profesor eliminado correctamente");
            response.put("status", 200);
            response.put("profesor", idProfesor);
        }
        catch (Exception ex){
            response.put("message", ex.getMessage());
            response.put("status", 500);
            response.put("profesor", idProfesor);
        }

      return response;

	}
	
	private Date DateFormatter(String pDate){
		
		Date finalDate = new Date();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			finalDate = format.parse(pDate);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return finalDate;		
	}

}




