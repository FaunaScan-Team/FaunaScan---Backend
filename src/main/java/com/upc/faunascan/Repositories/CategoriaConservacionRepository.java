package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.CategoriaConservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaConservacionRepository extends JpaRepository<CategoriaConservacion, Long> {
}
