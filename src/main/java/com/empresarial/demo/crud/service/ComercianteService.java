package com.empresarial.demo.crud.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresarial.demo.crud.dto.ComercianteDto;
import com.empresarial.demo.crud.dto.ComercianteEstadisticaDto;
import com.empresarial.demo.crud.dto.Mensaje;
import com.empresarial.demo.crud.entity.Comerciante;
import com.empresarial.demo.crud.entity.Municipio;
import com.empresarial.demo.crud.repository.ComercianteRepository;
import com.empresarial.demo.crud.repository.MunicipioRepository;

@Service
@Transactional
public class ComercianteService {

    @Autowired
    ComercianteRepository comercianteRepository;
    
    @Autowired
    MunicipioRepository municipioRepository;
    
    @Autowired
    private EntityManager entityManager;
    
    public List<Comerciante> list(){
        return comercianteRepository.findAll();
    }
    
    public Page<Comerciante> listadoComerciantesPaginado(int pagina, int tamano) {
        Pageable pageable = PageRequest.of(pagina, tamano);
        return comercianteRepository.findAll(pageable);
    }

    public Optional<Comerciante> getOne(int id){
        return comercianteRepository.findById(id);
    }

    public Optional<Comerciante> getByNombre(String nombre){
        return comercianteRepository.findByRazonSocial(nombre);
    }

    public ResponseEntity<String>save(Comerciante comerciante){
    	
   		if(StringUtils.isBlank(comerciante.getRazonSocial()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(comerciante.getCorreo()==null)
            return new ResponseEntity(new Mensaje("el correo es obligatorio"), HttpStatus.BAD_REQUEST);
        if(comercianteRepository.existsByRazonSocial(comerciante.getRazonSocial()))
            return new ResponseEntity(new Mensaje("Razon social ya existe"), HttpStatus.BAD_REQUEST);
	
    
        Comerciante comercianteNuevo = Comerciante.builder()
        		.correo(comerciante.getCorreo())
        		.estado(comerciante.getEstado())
        		.fechaRegistro(comerciante.getFechaRegistro())
        		.municipio(comerciante.getMunicipio())
        		.razonSocial(comerciante.getRazonSocial())
        		.telefono(comerciante.getTelefono())
        		.build();
    	
        comercianteRepository.save(comercianteNuevo);
        
        return new ResponseEntity(new Mensaje("Comerciante creado correctamente"), HttpStatus.OK);
    }
    
    public ResponseEntity<String> update(int id, ComercianteDto comercianteDto){
    	
    	Comerciante comercianteNuevo = null; 	
    	
		 if(!comercianteRepository.existsById(id))
	            return new ResponseEntity(new Mensaje("no existe el registro"), HttpStatus.NOT_FOUND);        
	        if(StringUtils.isBlank(comercianteDto.getRazonSocial()))
	            return new ResponseEntity(new Mensaje("el nombre para actualizar es obligatorio"), HttpStatus.BAD_REQUEST);
	            	        
	    Municipio municipio = municipioRepository.findById(comercianteDto.getMunicipio()).get();
	        
        comercianteNuevo = getOne(id).get();           
        comercianteNuevo.setCorreo(comercianteDto.getCorreo());
        comercianteNuevo.setEstado(comercianteDto.getEstado());
        comercianteNuevo.setFechaRegistro(comercianteDto.getFechaRegistro());
        comercianteNuevo.setMunicipio(municipio);
        comercianteNuevo.setRazonSocial(comercianteDto.getRazonSocial());
        comercianteNuevo.setTelefono(comercianteDto.getTelefono());
    	
        comercianteRepository.save(comercianteNuevo);
        
        return new ResponseEntity(new Mensaje("Comerciante actualizado correctamente"), HttpStatus.OK);
    }
    
    public ResponseEntity<String> cambiarEstado(int id, Boolean estado){    	    	        
	    Comerciante comercianteNuevo = getOne(id).get();           
        comercianteNuevo.setEstado(estado);
        comercianteRepository.save(comercianteNuevo);
        
        if(Boolean.TRUE.equals(estado)) {
        	return new ResponseEntity(new Mensaje("Comerciante habilitado correctamente"), HttpStatus.OK);
        }else {
        	return new ResponseEntity(new Mensaje("Comerciante deshabilitado"), HttpStatus.OK);
        }        
    }

    public ResponseEntity<String> delete(int id){    	
    	if(!existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
    	comercianteRepository.deleteById(id);
        return new ResponseEntity(new Mensaje("producto eliminado"), HttpStatus.OK);
    }

    public boolean existsById(int id){
        return comercianteRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre){
        return comercianteRepository.existsByRazonSocial(nombre);
    }
    
    
    public String generarArchivoCSVNative() throws IOException {
        List<Object[]> resultados = comercianteRepository.findComerciantesActivosConEstadisticas("1");

        String archivoRuta = "comerciantes_activos.csv";
        try (FileWriter writer = new FileWriter(archivoRuta)) {
            writer.append("Nombre|Municipio|Teléfono|Correo Electrónico|Fecha de Registro|Estado|Cantidad de Establecimientos|Total Ingresos|Cantidad de Empleados\n");

            for (Object[] fila : resultados) {
                writer.append(String.format("%s|%s|%s|%s|%s|%s|%d|%.2f|%d\n",
                        fila[1], // nombre
                        fila[2], // municipio
                        fila[3], // telefono
                        fila[4], // correo electronico
                        fila[5], // fecha registro
                        fila[6], // estado
                        ((Number) fila[7]).intValue(), // cantidad establecimientos
                        ((Number) fila[8]).doubleValue(), // total ingresos
                        ((Number) fila[9]).intValue() // cantidad empleados
                ));
            }
        }
        return archivoRuta;
    }
    
    public String generarArchivoCSVProcedure() throws IOException {
        List<Object[]> resultados = comercianteRepository.obtenerEstadisticasComerciantes("1");

        List<ComercianteEstadisticaDto> comerciantes = resultados.stream()
                .map(ComercianteEstadisticaDto::new)
                .collect(Collectors.toList());

        String archivoRuta = "comerciantes_activos.csv";
        try (FileWriter writer = new FileWriter(archivoRuta)) {
            writer.append("Nombre|Municipio|Teléfono|Correo Electrónico|Fecha de Registro|Estado|Cantidad de Establecimientos|Total Ingresos|Cantidad de Empleados\n");

            for (ComercianteEstadisticaDto comerciante : comerciantes) {
                writer.append(String.format("%s|%s|%s|%s|%s|%s|%d|%.2f|%d\n",
                        comerciante.getNombre(),
                        comerciante.getMunicipio(),
                        comerciante.getTelefono(),
                        comerciante.getCorreoElectronico(),
                        comerciante.getFechaRegistro(),
                        comerciante.getEstado(),
                        comerciante.getCantidadEstablecimientos(),
                        comerciante.getTotalIngresos(),
                        comerciante.getCantidadEmpleados()));
            }
        }
        return archivoRuta;
    }
    
    public List<ComercianteEstadisticaDto> obtenerEstadisticasComerciantes(String estado) {
        // Llamada directa a la función
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("obtener_estadisticas_comerciantes");

        // Parámetros de entrada y salida (ajuste clave)
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);  // p_estado
        query.registerStoredProcedureParameter(2, void.class, ParameterMode.REF_CURSOR);  // return_value

        // Setear el valor del parámetro de entrada
        query.setParameter(1, estado);
        
        // Ejecutar la función
        query.execute();

        // Obtener los resultados
        List<Object[]> resultados = query.getResultList();

        // Mapear a DTO
        return resultados.stream()
                .map(ComercianteEstadisticaDto::new)
                .collect(Collectors.toList());
    }

    public String generarArchivoCSV() throws IOException {
        List<ComercianteEstadisticaDto> comerciantes = obtenerEstadisticasComerciantes("1");

        String archivoRuta = "comerciantes_estadisticas.csv";
        try (FileWriter writer = new FileWriter(archivoRuta)) {
            writer.append("ID|Nombre|Municipio|Teléfono|Correo Electrónico|Fecha de Registro|Estado|Cantidad de Establecimientos|Total Ingresos|Cantidad de Empleados\n");

            for (ComercianteEstadisticaDto comerciante : comerciantes) {
                writer.append(String.format("%d|%s|%s|%s|%s|%s|%s|%d|%.2f|%d\n",
                        
                        comerciante.getNombre(),
                        comerciante.getMunicipio(),
                        comerciante.getTelefono(),
                        comerciante.getCorreoElectronico(),
                        comerciante.getFechaRegistro(),
                        comerciante.getEstado(),
                        comerciante.getCantidadEstablecimientos(),
                        comerciante.getTotalIngresos(),
                        comerciante.getCantidadEmpleados()));
            }
        }
        return archivoRuta;
    }
    
    
}
