package com.aluracursos.Challenge2Cristhian.service;

public interface IConvierteDatos {
    <T> T convertData(String data, Class<T> classType);

}
