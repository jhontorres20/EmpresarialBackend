package com.empresarial.demo.crud.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.empresarial.demo.crud.dto.Mensaje;
import com.empresarial.demo.crud.dto.MunicipioDto;
import com.empresarial.demo.crud.entity.Municipio;
import com.empresarial.demo.crud.service.MunicipioService;

import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/municipio")
@CrossOrigin(origins = "*")
public class MunicipioController {

    @Autowired
    MunicipioService municipioService;

    @ApiOperation("Muestra una lista de municipios")
    @GetMapping("/lista")
    public ResponseEntity<List<Municipio>> list(){
        List<Municipio> list = municipioService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @ApiIgnore
    @GetMapping("/detail/{id}")
    public ResponseEntity<Municipio> getById(@PathVariable("id") int id){
        if(!municipioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Municipio municipio = municipioService.getOne(id).get();
        return new ResponseEntity(municipio, HttpStatus.OK);
    }

    @ApiIgnore
    @GetMapping("/detailname/{nombre}")
    public ResponseEntity<Municipio> getByNombre(@PathVariable("nombre") String nombre){
        if(!municipioService.existsByNombre(nombre))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Municipio municipio = municipioService.getByNombre(nombre).get();
        return new ResponseEntity(municipio, HttpStatus.OK);
    }
    
    @ApiOperation("Crea municipios")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MunicipioDto municipioDto){
        if(StringUtils.isBlank(municipioDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);        
        if(municipioService.existsByNombre(municipioDto.getNombre()))
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        Municipio product = new Municipio();
        product.setNombre(municipioDto.getNombre());
        municipioService.save(product);
        return new ResponseEntity(new Mensaje("Municipio creado"), HttpStatus.OK);
    }
    
    @ApiOperation("Actualizar municipio")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id, @RequestBody MunicipioDto municipioDto){
        if(!municipioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        if(municipioService.existsByNombre(municipioDto.getNombre()) && municipioService.getByNombre(municipioDto.getNombre()).get().getId() != id)
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        if(StringUtils.isBlank(municipioDto.getNombre()))
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Municipio municipio = municipioService.getOne(id).get();
        municipio.setNombre(municipioDto.getNombre());
        municipioService.save(municipio);
        return new ResponseEntity(new Mensaje("Municipio actualizado"), HttpStatus.OK);
    }
    
    @ApiOperation("Eliminar municipio")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!municipioService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        municipioService.delete(id);
        return new ResponseEntity(new Mensaje("Municipio eliminado"), HttpStatus.OK);
    }


}
