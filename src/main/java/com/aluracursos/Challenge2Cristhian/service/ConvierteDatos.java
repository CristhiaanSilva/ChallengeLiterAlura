package com.aluracursos.Challenge2Cristhian.service;


import com.fasterxml.jackson.databind.ObjectMapper;


public class ConvierteDatos implements IConvierteDatos {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T convertData(String data, Class<T> classType) {
        try {
            return objectMapper.readValue(data, classType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
