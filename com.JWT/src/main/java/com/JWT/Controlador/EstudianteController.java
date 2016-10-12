package com.JWT.Controlador;

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

import com.JWT.Modelo.Estudiante;
import com.JWT.Repositorio.EstudianteRepository;






@RestController
@RequestMapping("/Estudiantes")
public class EstudianteController {
	
	
	
	@Autowired
	EstudianteRepository estudianteRepo;

	@RequestMapping(method = RequestMethod.POST)
	
	public Map<String, Object> CrearEstudiante(@RequestBody Map<String, Object> estudianteMap) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {
			Estudiante estudiante = new Estudiante();
			
			
			estudiante.setId(0);
			estudiante.setNombre(estudianteMap.get("nombre").toString());
			estudiante.setApellidos((estudianteMap.get("apellidos").toString()));
			estudiante.setCorreo((estudianteMap.get("correo").toString()));
			
			String fechaNacimiento = (estudianteMap.get("fechaNacimiento").toString());
			estudiante.setFechaNacimiento(DateFormatter(fechaNacimiento));
			estudiante.setActivo(true);
			

			estudianteRepo.save(estudiante);
			response.put("message", "Estudiante creado correctamente");
			response.put("status", 200);
			response.put("estudiante", estudiante);
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("estudiante", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value="/{idEstudiante}")
	public Map<String, Object> Obtener(@PathVariable("idEstudiante") Integer idEstudiante) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Estudiante estudiante = new Estudiante();

		try {

			estudiante = estudianteRepo.findOne(idEstudiante);

			if (estudiante != null) {

				response.put("message", "Estudiante encontrado");
				response.put("status", 200);
				response.put("estudiante", estudiante);
			} else {
				response.put("message", "Estudiante no encontrado");
				response.put("status", 404);
				response.put("estudiante", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("estudiante", null);
		}

		return response;

	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> listarEstudiantes() {
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		

		try {

			List<Estudiante> listaEstudiantes = (List<Estudiante>) estudianteRepo.findAll();

			if (listaEstudiantes != null && listaEstudiantes.size() > 0) {

				response.put("message", "Estudiantes encontrados: "+  listaEstudiantes.size());
				response.put("status", 200);
				response.put("estudiante", listaEstudiantes);
			} else {
				response.put("message", "No hay estudiantes registrados");
				response.put("status", 404);
				response.put("estudiante", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("estudiante", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{idEstudiante}")
	public Map<String, Object> EditarEstudiante(@RequestBody Map<String, Object> estudianteMap, @PathVariable("idEstudiante") Integer idEstudiante) {
		
		 Map<String, Object> response = new HashMap<String, Object>();
		 Estudiante estudiante;
        
        try {
        	
        	estudiante = estudianteRepo.findOne(idEstudiante);
        	
        	if(estudiante != null) {
        		
        		estudiante.setNombre(estudianteMap.get("nombre").toString());
        		estudiante.setApellidos((estudianteMap.get("apellidos").toString()));
        		estudiante.setCorreo((estudianteMap.get("correo").toString()));
    			
    			String fechaNacimiento = (estudianteMap.get("fechaNacimiento").toString());
    			estudiante.setFechaNacimiento(DateFormatter(fechaNacimiento));
    			Boolean activo = Boolean.parseBoolean(estudianteMap.get("activo").toString());
    			estudiante.setActivo(activo);
        		
        		response.put("message", "Estudiante actualizado");
        		response.put("status",200);
				response.put("estudiante", estudianteRepo.save(estudiante));
				
        	}else 
			{
        		response.put("message", "Estudiante no encontrado");
				response.put("status", 404);
				response.put("estudiante", null);
			}
			
		} catch (Exception ex) {
			 response.put("message", ex.getMessage());
             response.put("status", 500);
             response.put("estudiante", null);
         }

       return response;
         
         
         
         
	}

	
	@RequestMapping(method = RequestMethod.DELETE, value="/{idEstudiante}")
	public Map<String, Object> BorrarEstudiante(@PathVariable("idEstudiante") Integer idEstudiante) {
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try{
			estudianteRepo.delete(idEstudiante);
            response.put("message", "Estudiante eliminado correctamente");
            response.put("status", 200);
            response.put("estudiante", idEstudiante);
        }
        catch (Exception ex){
            response.put("message", ex.getMessage());
            response.put("status", 500);
            response.put("estudiante", idEstudiante);
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




