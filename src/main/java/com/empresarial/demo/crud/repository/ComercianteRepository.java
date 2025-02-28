package com.empresarial.demo.crud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresarial.demo.crud.entity.Comerciante;

@Repository
public interface ComercianteRepository extends JpaRepository<Comerciante, Integer> {
    Optional<Comerciante> findByRazonSocial(String razonSocial);
    boolean existsByRazonSocial(String razonSocial);
    
    @Query(value = "SELECT c.id, c.razon_social, c.municipio_id, c.telefono, c.correo, c.fecha_registro, c.estado,\n"
    		+ "               COUNT(e.id) AS cantidad_establecimientos,\n"
    		+ "               NVL(SUM(e.precio), 0) AS total_ingresos,\n"
    		+ "               NVL(SUM(e.numero_empleados), 0) AS cantidad_empleados\n"
    		+ "        FROM Comerciante c\n"
    		+ "        LEFT JOIN Establecimiento e ON c.id = e.comerciante_id\n"
    		+ "        WHERE c.estado = :estado\n"
    		+ "        GROUP BY c.id, c.razon_social, c.municipio_id, c.telefono, c.correo, c.fecha_registro, c.estado\n"
            , nativeQuery = true)
        List<Object[]> findComerciantesActivosConEstadisticas(@Param("estado") String estado);
        
        //@Procedure(name = "obtener_estadisticas_comerciantes")
        //List<Object[]> obtenerEstadisticasComerciantes(@Param("p_estado") String estado);
        
        @Query(value = "SELECT * FROM TABLE(obtener_estadisticas_comerciantes(:p_estado))", nativeQuery = true)
        List<Object[]> obtenerEstadisticasComerciantes(@Param("p_estado") String estado);
}
