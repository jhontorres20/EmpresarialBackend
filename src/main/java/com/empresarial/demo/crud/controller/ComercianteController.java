package com.empresarial.demo.crud.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.empresarial.demo.crud.dto.ComercianteDto;
import com.empresarial.demo.crud.dto.Mensaje;
import com.empresarial.demo.crud.entity.Comerciante;
import com.empresarial.demo.crud.entity.Municipio;
import com.empresarial.demo.crud.service.ComercianteService;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/comerciante")
@CrossOrigin(origins = "*")
public class ComercianteController {

    @Autowired
    ComercianteService comercianteService;

    @ApiOperation("Muestra listado de comerciantes")
    @GetMapping("/lista")
    public ResponseEntity<List<Comerciante>> list(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<Comerciante> list = comercianteService.listadoComerciantesPaginado(page, size);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @ApiIgnore
    @GetMapping("/detail/{id}")
    public ResponseEntity<Municipio> getById(@PathVariable("id") int id){
        if(!comercianteService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Comerciante comerciante = comercianteService.getOne(id).get();
        return new ResponseEntity(comerciante, HttpStatus.OK);
    }

    @ApiIgnore
    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<Municipio> getByNombre(@PathVariable("nombre") String nombre){
        if(!comercianteService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Comerciante comerciante = comercianteService.getByNombre(nombre).get();
        return new ResponseEntity(comerciante, HttpStatus.OK);
    }
    
    @ApiOperation("Registrar comerciante")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody Comerciante comerciante){        
        return comercianteService.save(comerciante);        
    }

    @ApiOperation("Actualizar comerciante")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id")int id, @RequestBody ComercianteDto comercianteDto){
        return comercianteService.update(id, comercianteDto);        
    }
    
    @ApiOperation("Eliminar comerciante")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id")int id){
    	return comercianteService.delete(id);
    }
    
    @ApiOperation("Habilitar y/o deshabilitar comerciante")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/estado/{id}")
    public ResponseEntity<String> habilitar(@PathVariable("id")int id,
    		@RequestParam(name = "estado") Boolean estado){
    	return comercianteService.cambiarEstado(id, estado);
    }
    
    @ApiOperation("Reporte comerciantes Activos")
    @GetMapping("/generarCsv")
    public ResponseEntity<FileSystemResource> generarCSVNative() throws IOException {
        String archivoRuta = comercianteService.generarArchivoCSVNative();
        FileSystemResource archivo = new FileSystemResource(archivoRuta);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo.getFilename())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(archivo);
    }
    
    @ApiOperation("Reporte comerciantes Activos")
    @GetMapping("/generarCsvProcedure")
    public ResponseEntity<FileSystemResource> generarCSV() throws IOException {
        String archivoRuta = comercianteService.generarArchivoCSV();
        FileSystemResource archivo = new FileSystemResource(archivoRuta);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo.getFilename())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(archivo);
    }


}
