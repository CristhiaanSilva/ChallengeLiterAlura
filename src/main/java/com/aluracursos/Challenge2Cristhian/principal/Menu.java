package com.aluracursos.Challenge2Cristhian.principal;

public class Menu {
    public void menu() {
            String menu = """
                    ****=============================****
                    \tBienvenido a LiterAlura by Cristhian
                           
                    1. Buscar libro por titulo
                    2. Mostrar libros buscados
                    3. Mostrar autores buscados
                    4. Mostrar lista de autores vivos por año
                    5. Mostrar libros por idioma
                    6. Mostrar estadísticas                                 
                    7. Top 10 libros mas descargados
                    8. Buscar autor por nombre
                    0. Salir
                    ****==============================****
                    Elige una opción:
                    """;
            System.out.println(menu);
        }

        public void menuIdioma () {
            String msjIdioma = """
                    ---------------------
                    Idiomas disponibles:
                                    
                      -en  (Inglés)
                      -es  (Español)
                      -fr  (Francés)
                      -de  (Alemán)
                      -it  (Italiano)
                      -pt  (Portugués)
                      -ja  (Japonés)
                    --------------------
                    """;
            System.out.println(msjIdioma);
        }
    }