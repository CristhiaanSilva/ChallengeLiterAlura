package com.aluracursos.Challenge2Cristhian.principal;

import com.aluracursos.Challenge2Cristhian.model.*;
import com.aluracursos.Challenge2Cristhian.repository.AutorRepository;
import com.aluracursos.Challenge2Cristhian.repository.LibroRepository;
import com.aluracursos.Challenge2Cristhian.service.ConsumoAPI;
import com.aluracursos.Challenge2Cristhian.service.ConvierteDatos;
import com.aluracursos.Challenge2Cristhian.model.Autor;
import com.aluracursos.Challenge2Cristhian.model.DatosLibro;
import com.aluracursos.Challenge2Cristhian.model.LibrosDatos;



import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books";
    private List<Libro> libroBuscado = new ArrayList<>();
    private List<Autor> autorBuscado = new ArrayList<>();
    private Menu menu = new Menu();

    //Inyeccion de dependencias
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void MuestraElMenu() {
        int option = 0;
        do {
            menu.menu();
            option = obtenerNumero();

            switch (option) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    mostrarListaAutoresPorAnio();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 6:
                    mostrarEstadisticas();
                    break;
                case 7:
                    mostrarTop10Libros();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación, gracias por usar LiterAlura");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        } while (option != 0);

    }

    public int obtenerNumero() {
        int number = 0;
        while (true) {
            try {
                number = teclado.nextInt();
                teclado.nextLine();
                if(number != 9){
                    return number;
                }
                System.out.println("Por favor, introduce un número válido.");
            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduce un número válido.");
                teclado.nextLine();
            }
        }
    }

    private String getStringFromUser(String message) {
        String data = "";
        while (true) {
            System.out.println(message);
            data = teclado.nextLine();
            if (!data.isEmpty()) {
                return data;
            }
        }

    }

    public String getDatosLibros(String title) {
        ConsumoAPI request = new ConsumoAPI();
        var url = URL_BASE + "/?search=" + title.replace(" ", "+");
        return request.getData(url);
    }

    public LibrosDatos jsonToDatosLibros(String data) {
        ConvierteDatos dataConversion = new ConvierteDatos();
        return dataConversion.convertData(data, LibrosDatos.class);
    }

    public DatosLibro getFirstBookWithAuthor(List<DatosLibro> libros) {
        return libros.stream()
                .filter(libro -> !libro.autor().isEmpty())
                .findFirst()
                .orElse(null);
    }

    public Libro searchOrSaveBook(Autor author, DatosLibro libro) {
        Libro bookToSave = null;
        List <Libro> books = author.getLibros();

        Optional <Libro> bookFromAuthor = books.stream()
                .filter(l -> l.getTitulo().equals(libro.titulo()))
                .findFirst();

        if (bookFromAuthor.isPresent()) {
            System.out.println("El libro ya registrado!");
            bookToSave = bookFromAuthor.get();
        } else {

            bookToSave = new Libro(libro.titulo(), author,
                    libro.idioma().get(0), libro.numeroDeDescargas());

            author.setLibros(bookToSave);
            libroRepository.save(bookToSave);

            System.out.println("Libro guardado!");
        }
        return bookToSave;
    }

    public Autor searchOrSaveAuthor(DatosLibro libro) {
        Optional<Autor> autorBuscado = autorRepository.findByNombre(libro.autor().get(0).nombre());
        Autor authorToSave = null;


        if (!autorBuscado.isPresent()) {
            authorToSave = new Autor(libro.autor().get(0).nombre(),
                    libro.autor().get(0).nacimiento(), libro.autor().get(0).muerte());
            autorRepository.save(authorToSave);
            System.out.println("Autor guardado!");
        } else {
            authorToSave = autorBuscado.get();
            System.out.println("Autor ya registrado!");
        }
        return authorToSave;

    }

    public void buscarLibroPorTitulo() {

        String message = "Introduce el titulo del libro a buscar: ";
        var title = getStringFromUser(message);

        String data = getDatosLibros(title);
        LibrosDatos libros = jsonToDatosLibros(data);

        if (!libros.libros().isEmpty()) {
            DatosLibro libro = getFirstBookWithAuthor(libros.libros());

            Autor author = searchOrSaveAuthor(libro);
            Libro book = searchOrSaveBook(author, libro);
            System.out.println(author);
            System.out.println(book);

        } else {
            System.out.println("No se encontraron resultados");
        }
    }

    private void mostrarLibrosBuscados() {

        libroBuscado = libroRepository.findAll();
        if (libroBuscado.isEmpty()) {
            System.out.println("No se encontraron libros registrados ");
        }
        libroBuscado.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println(libro.toString());
                });
    }

    private void mostrarAutoresBuscados() {
        autorBuscado = autorRepository.findAll();
        if (autorBuscado.isEmpty()) {
            System.out.println("No se encontraron autores registrados");
        }
        autorBuscado.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    System.out.println(autor.toString());
                    System.out.println(autor.getLibros());
                });
    }

    private void mostrarListaAutoresPorAnio() {
        System.out.println("Por favor ingrese el año: ");

        var year = obtenerNumero();
        List<Autor> autoresVivos = autorRepository.getAliveAuthors(year);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos registrados en ese año");
        } else {
            autoresVivos.stream()
                    .forEach(autor -> {
                        System.out.println(autor.toString());
                    });
        }

    }

    private void mostrarLibrosPorIdioma() {

        menu.menuIdioma();

        String message= "Por favor, introduce el idioma: ";
        String language = getStringFromUser(message);

        List<Libro> librosPorIdioma = libroRepository.findBookByLanguage(language);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No fue posible encontrar libros en ese idioma");
        } else {
            librosPorIdioma.stream()
                    .forEach(libro -> {
                        System.out.println(libro.toString());
                    });
        }
    }

    private void mostrarEstadisticas() {
        List<Libro> booksDownloads = libroRepository.findAll();
        DoubleSummaryStatistics stats = booksDownloads.stream()
                .filter(libro -> libro.getNumeroDeDescargas() >= 0)
                .collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));

        String msj = """
                -------------------------
                Estadisticas de descargas:
                    Promedio total de libros: %.2f
                    Libro menos descargado: %.2f
                    Libro mas descargado: %.2f
                    Total libros: %d
                -------------------------
                """.formatted(stats.getAverage(), stats.getMin(), stats.getMax(), stats.getCount());
        System.out.println(msj);
    }

    private void mostrarTop10Libros() {
        System.out.println("Top 10 libros mas descargados:");
        List<String> top10Books = libroRepository.findTop10Books();
        if (top10Books.isEmpty()) {
            System.out.println("No fue posible encontrar libros");
        } else {
            int i = 0;
            for (String libro : top10Books) {
                System.out.println(++i + ". " + libro);
            }
        }

    }

    private Autor selectAuthor(List<Autor> autores) {
        int i = 0;

        System.out.println("Autores encontrados: ");

        for (Autor autor : autores) {
            System.out.println(++i + ". \n" + autor.toString());
        }

        System.out.println("Seleccione un autor: ");
        int option = obtenerNumero();

        return autores.get(option - 1);
    }


    private void buscarAutorPorNombre() {
        String apellido = "";

        String message = "Introduce el nombre del autor: ";
        String data = getStringFromUser(message);

        String[] names = data.split(" ");
        apellido = names[names.length - 1];

        List<Autor> autores = autorRepository.findAutorByName(apellido);

        if (autores.isEmpty()) {
            System.out.println("No fue posible encontrar autores con ese apellido");
        } else {
            Autor autor = autores.size() > 1 ? selectAuthor(autores) : autores.get(0);
            System.out.println(autor.toString());
        }

    }
}
