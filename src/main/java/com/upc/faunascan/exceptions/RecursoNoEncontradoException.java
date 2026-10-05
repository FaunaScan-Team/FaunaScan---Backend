package com.upc.faunascan.exceptions;

// Se lanza cuando un registro buscado por id o correo no existe (responde 404)
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
