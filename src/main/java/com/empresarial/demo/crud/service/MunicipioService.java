package com.empresarial.demo.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresarial.demo.crud.entity.Municipio;
import com.empresarial.demo.crud.repository.MunicipioRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MunicipioService {

    @Autowired
    MunicipioRepository municipioRepository;
    
    @Cacheable("lista")
    public List<Municipio> list(){
        return municipioRepository.findAll();
    }

    public Optional<Municipio> getOne(int id){
        return municipioRepository.findById(id);
    }

    public Optional<Municipio> getByNombre(String nombre){
        return municipioRepository.findByNombre(nombre);
    }

    public void  save(Municipio municipio){
        municipioRepository.save(municipio);
    }

    public void delete(int id){
        municipioRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return municipioRepository.existsById(id);
    }

    public boolean existsByNombre(String nombre){
        return municipioRepository.existsByNombre(nombre);
    }
}
