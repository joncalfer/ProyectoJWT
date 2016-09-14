package com.WSR.Cotroller;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.WSR.Model.Curso;
import com.WSR.Repository.CursoRepository;






@RestController
@RequestMapping("/Cursos")
public class CursoController {
	
	
	
	@Autowired
	CursoRepository cursoRepo;

	@RequestMapping(method = RequestMethod.POST)
	
	public Map<String, Object> CrearCurso(@RequestBody Map<String, Object> cursoMap) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {
			Curso curso = new Curso();
			curso.setId(0);
			curso.setNombre(cursoMap.get("nombre").toString());
			curso.setSiglas(cursoMap.get("siglas").toString());
			int idProfesor = Integer.parseInt(cursoMap.get("idProfesor").toString());
			curso.setProfesores_id(idProfesor);
			

			cursoRepo.save(curso);
			response.put("message", "Curso creado correctamente");
			response.put("status", 200);
			response.put("curso", curso);
		} catch (Exception ex) {
			response.put("message", ex.getMessage()+"holi");
			response.put("status", 500);
			response.put("curso", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value="/{idCurso}")
	public Map<String, Object> Obtener(@PathVariable("idCurso") Integer idCurso) {

		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Curso curso = new Curso();

		try {

			curso = cursoRepo.findOne(idCurso);

			if (curso != null) {

				response.put("message", "Curso encontrado");
				response.put("status", 200);
				response.put("curso", curso);
			} else {
				response.put("message", "Curso no encontrado");
				response.put("status", 404);
				response.put("curso", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("curso", null);
		}

		return response;

	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> listarCursos() {
		
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		try {

			List<Curso> listaCursos = (List<Curso>) cursoRepo.findAll();

			if (listaCursos != null && listaCursos.size() > 0) {

				response.put("message", "Cursos encontrados: "+  listaCursos.size());
				response.put("status", 200);
				response.put("curso", listaCursos);
			} else {
				response.put("message", "No hay cursos registrados");
				response.put("status", 404);
				response.put("curso", null);
			}
		} catch (Exception ex) {
			response.put("message", ex.getMessage());
			response.put("status", 500);
			response.put("curso", null);
		}

		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value="/{idCurso}")
	public Map<String, Object> EditarCurso(@RequestBody Map<String, Object> cursoMap, @PathVariable("idCurso") Integer idCurso) {
		
		 Map<String, Object> response = new HashMap<String, Object>();
		 
		 Curso curso;
        
        try {
        	
        	curso = cursoRepo.findOne(idCurso);
        	
        	if(curso != null) {
        		
        		curso.setNombre(cursoMap.get("nombre").toString());
    			curso.setSiglas(cursoMap.get("siglas").toString());
    			int idProfesor = Integer.parseInt(cursoMap.get("idProfesor").toString());
    			curso.setProfesores_id(idProfesor);
        		
        		response.put("message", "Curso actualizado");
        		response.put("status",200);
				response.put("curso", cursoRepo.save(curso));
				
        	}else 
			{
        		response.put("message", "Curso no encontrado");
				response.put("status", 404);
				response.put("curso", null);
			}
			
		} catch (Exception ex) {
			 response.put("message", ex.getMessage());
             response.put("status", 500);
             response.put("curso", null);
         }

       return response;  
         
	}

	
	@RequestMapping(method = RequestMethod.DELETE, value="/{idCurso}")
	public Map<String, Object> BorrarCurso(@PathVariable("idCurso") Integer idCurso) {
		
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try{
			cursoRepo.delete(idCurso);
            response.put("message", "Curso eliminado correctamente");
            response.put("status", 200);
            response.put("curso", idCurso);
        }
        catch (Exception ex){
            response.put("message", ex.getMessage());
            response.put("status", 500);
            response.put("curso", null);
        }

      return response;

	}
	
	

}




