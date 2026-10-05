package com.upc.faunascan.Services;

import com.upc.faunascan.dto.EvaluacionIADTO;
import com.upc.faunascan.dto.IdentificarEspecieDTO;
import com.upc.faunascan.dto.ResultadoIdentificacionDTO;

public interface IAService {
    ResultadoIdentificacionDTO identificarEspecie(IdentificarEspecieDTO dto);
    EvaluacionIADTO evaluarPrecision();
}
