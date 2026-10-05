package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Avistamiento;
import com.upc.faunascan.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    // HU-24: historial del usuario, con especie y ubicacion ya cargadas
    @Query("select a from Avistamiento a " +
           "join fetch a.especie e join fetch a.ubicacion u " +
           "where a.usuario.idUsuario = :idUsuario " +
           "order by a.fechaAvistamiento desc")
    List<Avistamiento> findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(@Param("idUsuario") Long idUsuario);

    List<Avistamiento> findByEspecie_IdEspecie(Long idEspecie);

    List<Avistamiento> findByUbicacion_IdUbicacion(Long idUbicacion);

    // HU-50: cola de revision del investigador
    @Query("select a from Avistamiento a " +
           "join fetch a.usuario us join fetch a.especie e " +
           "where a.estadoValidacion = :estado order by a.fechaRegistro asc")
    List<Avistamiento> findByEstadoValidacion(@Param("estado") String estadoValidacion);

    boolean existsByIdAvistamientoAndUsuario_Correo(Long idAvistamiento, String correo);

    // HU-14/HU-42: marcadores del mapa (solo avistamientos validados)
    @Query("select new com.upc.faunascan.dto.MapaAvistamientoDTO(a.idAvistamiento, e.nombreComun, c.nombre, u.latitud, u.longitud) " +
           "from Avistamiento a join a.especie e left join e.categoriaConservacion c join a.ubicacion u " +
           "where a.estadoValidacion = 'validado'")
    List<MapaAvistamientoDTO> listarParaMapa();

    // HU-17: marcadores filtrados por categoria de conservacion
    @Query("select new com.upc.faunascan.dto.MapaAvistamientoDTO(a.idAvistamiento, e.nombreComun, c.nombre, u.latitud, u.longitud) " +
           "from Avistamiento a join a.especie e join e.categoriaConservacion c join a.ubicacion u " +
           "where a.estadoValidacion = 'validado' and lower(c.nombre) = lower(:estado)")
    List<MapaAvistamientoDTO> listarParaMapaPorEstado(@Param("estado") String estado);

    // HU-15: coordenadas para el mapa de calor
    @Query("select new com.upc.faunascan.dto.CoordenadaDTO(u.latitud, u.longitud) " +
           "from Avistamiento a join a.ubicacion u where a.estadoValidacion = 'validado'")
    List<CoordenadaDTO> listarCoordenadasValidadas();

    // HU-21: ultimos avistamientos validados de otros usuarios
    @Query("select new com.upc.faunascan.dto.AvistamientoComunidadDTO(a.idAvistamiento, us.nombre, us.apellido, " +
           "e.nombreComun, c.nombre, u.direccion, u.latitud, u.longitud, a.fechaAvistamiento) " +
           "from Avistamiento a join a.usuario us join a.especie e left join e.categoriaConservacion c join a.ubicacion u " +
           "where a.estadoValidacion = 'validado' and us.idUsuario <> :idUsuario " +
           "order by a.fechaAvistamiento desc")
    List<AvistamientoComunidadDTO> listarRecientesDeComunidad(@Param("idUsuario") Long idUsuario, Pageable pageable);

    // HU-25: historial con filtros opcionales (cadena vacia = sin filtro)
    @Query("select a from Avistamiento a join fetch a.especie e join fetch a.ubicacion u " +
           "where a.usuario.idUsuario = :idUsuario " +
           "and (:especie = '' or lower(e.nombreComun) like lower(concat('%', :especie, '%'))) " +
           "and (:zona = '' or lower(u.direccion) like lower(concat('%', :zona, '%'))) " +
           "and (:estado = '' or a.estadoValidacion = :estado) " +
           "order by a.fechaAvistamiento desc")
    List<Avistamiento> filtrarHistorial(@Param("idUsuario") Long idUsuario, @Param("especie") String especie,
                                        @Param("zona") String zona, @Param("estado") String estado);

    // HU-23/HU-53: progreso del usuario
    @Query("select new com.upc.faunascan.dto.ProgresoDTO(count(a), count(distinct a.especie.idEspecie), count(distinct u.direccion)) " +
           "from Avistamiento a join a.ubicacion u where a.usuario.idUsuario = :idUsuario")
    ProgresoDTO calcularProgreso(@Param("idUsuario") Long idUsuario);

    // HU-54: contribucion del usuario por estado de validacion
    @Query("select new com.upc.faunascan.dto.ContribucionDTO(count(a), " +
           "coalesce(sum(case when a.estadoValidacion = 'validado' then 1L else 0L end), 0L), " +
           "coalesce(sum(case when a.estadoValidacion = 'pendiente' then 1L else 0L end), 0L), " +
           "coalesce(sum(case when a.estadoValidacion = 'rechazado' then 1L else 0L end), 0L)) " +
           "from Avistamiento a where a.usuario.idUsuario = :idUsuario")
    ContribucionDTO calcularContribucion(@Param("idUsuario") Long idUsuario);

    // HU-52: metricas del panel del investigador
    long countByEstadoValidacion(String estadoValidacion);

    long countByInvestigadorValidador_IdUsuario(Long idUsuario);

    // HU-18: especies registradas por area y periodo
    @Query("select new com.upc.faunascan.dto.ItemBiodiversidadDTO(e.nombreComun, c.nombre, count(a)) " +
           "from Avistamiento a join a.especie e left join e.categoriaConservacion c join a.ubicacion u " +
           "where a.estadoValidacion = 'validado' and a.fechaAvistamiento between :desde and :hasta " +
           "and (:area = '' or lower(u.direccion) like lower(concat('%', :area, '%'))) " +
           "group by e.nombreComun, c.nombre order by count(a) desc")
    List<ItemBiodiversidadDTO> reporteBiodiversidad(@Param("area") String area, @Param("desde") LocalDateTime desde,
                                                    @Param("hasta") LocalDateTime hasta);

    // HU-19: resumen por mes
    @Query("select new com.upc.faunascan.dto.ResumenMensualDTO(year(a.fechaAvistamiento), month(a.fechaAvistamiento), " +
           "count(a), count(distinct a.especie.idEspecie)) " +
           "from Avistamiento a where a.estadoValidacion = 'validado' " +
           "group by year(a.fechaAvistamiento), month(a.fechaAvistamiento) " +
           "order by year(a.fechaAvistamiento) desc, month(a.fechaAvistamiento) desc")
    List<ResumenMensualDTO> resumenMensual();

    @Query("select count(distinct a.especie.idEspecie) from Avistamiento a where a.estadoValidacion = 'validado'")
    long contarEspeciesValidadas();

    // HU-57: tendencia mensual por especie
    @Query("select new com.upc.faunascan.dto.TendenciaEspecieDTO(e.nombreComun, year(a.fechaAvistamiento), month(a.fechaAvistamiento), count(a)) " +
           "from Avistamiento a join a.especie e where a.estadoValidacion = 'validado' " +
           "group by e.nombreComun, year(a.fechaAvistamiento), month(a.fechaAvistamiento) " +
           "order by e.nombreComun, year(a.fechaAvistamiento), month(a.fechaAvistamiento)")
    List<TendenciaEspecieDTO> tendenciaPorEspecie();

    // HU-57: zonas con mayor actividad
    @Query("select new com.upc.faunascan.dto.ZonaActividadDTO(u.direccion, count(a)) " +
           "from Avistamiento a join a.ubicacion u where a.estadoValidacion = 'validado' " +
           "group by u.direccion order by count(a) desc")
    List<ZonaActividadDTO> zonasConMayorActividad();
}
