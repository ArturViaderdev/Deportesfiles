/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deportesfiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author arturv
 */
public class Metodos {
    //Se delcara lista de deportes

    static ArrayList<String> deportes = new ArrayList<String>();
    //Se declara lista de alumnos
    static ArrayList<Alumno> alumnos = new ArrayList<>();
    //Variable con ruta de la carpeta donde se guardan los archivos
    static final String carpeta = "." + File.separator + "datos";

    public static void creadeportesdefecto() {
        deportes.add("Futbol");
        deportes.add("Futbol sala");
        deportes.add("Basquet");
        deportes.add("Badminton");
        deportes.add("Voley playa");
    }

    //Solicita los dados al usuario para eliminar una inscripción
    public static void eliminarinscripcion() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        String nombre, apellidos, deporte;

        int posborra;
        try {
            //pedimos al usuario el nombre y apellids del usuario a eliminar
            System.out.println("Vamos a introducir los datos del alumno a eliminar.");
            System.out.println("Introduce el nombre.");
            nombre = lector.readLine();

            System.out.println("Introduce los apellidos.");
            apellidos = lector.readLine();
            //se busca el alumno
            posborra = consultaralumno(nombre, apellidos);
            if (posborra >= 0) {
                //se obtiene el deporte del alumno a borrar
                deporte = alumnos.get(posborra).getdeporte();
                //se borra el alumno de la lista en memoria
                alumnos.remove(posborra);
                System.out.println("Alumno eliminado.");
                //se regraba el fichero del deporte de ese alumno
                //si no quedan alumnos de ese fichero el fichero quedará vacio
                regrabafichero(deporte);
                System.out.println("Archivo del deporte " + deporte + " regrabado.");

            } else {
                //el alumno no existe
                System.out.println("El alumno no existe.");
            }
        } catch (IOException ex) {
            System.out.println("Error entrada");
        }

    }

    //Se da de alta un nuevo alumno introduciendo el usuario los datos
    public static void registraralumno() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        Scanner entrada = new Scanner(System.in);
        String nombre, apellidos, curso, sexos, deporte;
        Alumno alumnoanade;
        int posdeporte;
        boolean sexo, puestosexo, entraint;
        sexo = false;
        int edad;
        entraint = false;
        try {
            //Introducimos los datos del nuevo alumno
            System.out.println("Vamos a introducir los datos del nuevo alumno.");

            //no permitimos que se introduzca el campo vacio
            do {
                System.out.println("Introduce el nombre.");
                nombre = lector.readLine();
            } while (nombre.isEmpty() || nombre == null);

            do {
                System.out.println("Introduce los apellidos.");
                apellidos = lector.readLine();
            } while (apellidos.isEmpty() || apellidos == null);

            if (consultaralumno(nombre, apellidos) == -1) {
                //si el alumno existe
                //se pide el curso
                do {
                    System.out.println("Introduce el curso.");
                    curso = lector.readLine();
                } while (curso.isEmpty() || curso == null);

                puestosexo = false;
                do {
                    //se introduce el sexo
                    System.out.println("Introduce el sexo. H/M.");
                    sexos = lector.readLine();
                    if (sexos.length() == 1) {
                        if (sexos.charAt(0) == 'h' || sexos.charAt(0) == 'H') {
                            sexo = true;
                            puestosexo = true;
                        } else if (sexos.charAt(0) == 'M' || sexos.charAt(0) == 'm') {
                            sexo = false;
                            puestosexo = true;
                        } else {
                            System.out.println("Error introduciendo sexo.");
                        }
                    } else {
                        System.out.println("Error introduciendo sexo.");
                    }
                } while (!puestosexo);

                //se solicita la edad
                System.out.println("Introduce la edad.");
                entraint = true;
                edad = entrada.nextInt();
                entraint = false;
                System.out.println("Elige un deporte.");
                //se muestra la lista de deportes y se permite al usuario seleccionar uno
                muestralistadeportes();
                posdeporte = entrada.nextInt();
                posdeporte--;
                if (posdeporte >= 0 && posdeporte < deportes.size()) {
                    //si el usuario ha introducido un deporte correcto
                    deporte = deportes.get(posdeporte);

                    //añade alumno a la lista
                    alumnoanade = new Alumno(nombre, apellidos, curso, sexo, edad, deporte);
                    alumnos.add(alumnoanade);
                    //añade el alumno en el  archivo del deporte correspondiente
                    anadeaarchivo(alumnoanade);
                    System.out.println("Alumno añadido.");
                } else {
                    System.out.println("Error introduciendo deporte. Alta cancelada.");
                }
            } else {
                System.out.println("El alumno ya existe. No se pueden dar de alta alumnos con el mismo nombre y apellidos.");
            }

        } catch (IOException ex) {
            System.out.println("Error introduciendo datos.");
            //se busca solucionar un error cuando no se introduce un número
            if (entraint) {
                entraint = false;
                entrada.nextLine();
            }
        }

    }

    //Crea la linea a grabar en el fichero a partir de un objeto alumno
    //Parámetros Alumno elalumno - Objeto del tipo alumno que será convertido en string
    //Retorno String: Devuelve la linea creada, lista para grabar en archivo
    private static String crealinea(Alumno elalumno) {
        String linea;
        char sexo = 'h';
        if (elalumno.getsexo()) {
            sexo = 'h';
        } else {
            sexo = 'm';
        }
        //se utiliza un asterisco para separar los campos
        linea = elalumno.getnombre() + "*" + elalumno.getapellidos() + "*" + elalumno.getcurso() + "*" + elalumno.getedad() + "*" + sexo + System.lineSeparator();
        return linea;
    }

    //Regraba el fichero de un deporte en concreto
    //Parámetros: String deporte - El nombre del deporte para el cual se regrabará el fichero
    private static void regrabafichero(String deporte) {
        BufferedWriter writer = null;
        boolean primero = true;
        try {
            File archivo = new File(carpeta + File.separator + deporte + ".txt");
            writer = new BufferedWriter(new FileWriter(archivo, false));
            for (Alumno elalumno : alumnos) {
                //se recorren todos los alumnos
                //se escriben en el fichero todos aquellos inscritos a ese deporte
                if (elalumno.getdeporte().equals(deporte)) {
                    writer.write(crealinea(elalumno));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error escribiendo archivo.");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }

    //Añade al archivo correspondiente un alumno
    //Parámetros: Alumno alumnoanade - El alumno que se va a añadir al archivo
    //El archivo del deporte se elige según el deporte del alumno
    private static void anadeaarchivo(Alumno alumnoanade) {
        BufferedWriter writer = null;
        try {
            //Se configura la ruta del archivo dependiendo del deporte
            File archivo = new File(carpeta + File.separator + alumnoanade.getdeporte() + ".txt");
            writer = new BufferedWriter(new FileWriter(archivo, true));
            //se escribe el alumno en el archivo
            writer.write(crealinea(alumnoanade));

        } catch (IOException ex) {
            System.out.println("Error escribiendo archivo.");
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {

            }
        }
    }

    //Interpreta o entiende una linea de alumno como las que hay en los archivos
    //Parámetros: String linea - Linea a interpretar
    //String deporte - Nombre del deporte del alumno o del archivo donde se ha encontrado
    private static void interpretalinea(String linea, String deporte) {
        String[] datos;
        datos = linea.split("\\*");
        //Divide los campos en un array de string
        boolean sexo;
        // Falta al grabar porner h o m hombre o mujer e interpretarlo
        if (datos[4].equals("h")) {
            sexo = true;
        } else {
            sexo = false;
        }
        //Crea el objeto alumno con los datos y lo añade a la lista en memoria.
        Alumno alumnoanade = new Alumno(datos[0], datos[1], datos[2], sexo, Integer.parseInt(datos[3]), deporte);
        alumnos.add(alumnoanade);

    }

    //Lee todos los ficheros de los deportes
    public static void leeficheros() {
        File archivo;

        BufferedReader lector;
        String linea;

        for (String deporte : deportes) {
            //para cada deporte se lee su fichero
            archivo = new File(carpeta + File.separator + deporte + ".txt");
            try {
                lector = new BufferedReader(new FileReader(archivo));
                try {
                    do {
                        linea = lector.readLine();
                        if (linea != null) {
                            //Cada linea se interpreta y añade a la lista en memoria
                            //le pasamos el deporte
                            interpretalinea(linea, deporte);
                        }
                    } while (linea != null);
                    
                } catch (IOException ex) {
                    System.out.println("Error leyendo archivo.");
                }
                finally
                {
                    try
                    {
                        lector.close();
                    }
                    catch(IOException ex)
                    {
                        
                    }
                    
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Archivo del deporte " + deporte + " no encontrado.");
            }
        }
    }

    //Muestra un alumno situado en una posición de la lista.
    //Este método es una alternativa para ahorrar memoria.
    //En lugar de pasar el objeto alumno paso solo la posición del alumno en la lista
    //Parámetros: int pos - Posición del alumno en la lista
    private static void muestraalumnopos(int pos) {
        String sexo;
        System.out.println("Nombre -" + alumnos.get(pos).getnombre());
        System.out.println("Apellidos -" + alumnos.get(pos).getapellidos());
        System.out.println("Edad -" + alumnos.get(pos).getedad());

        System.out.println("Curso -" + alumnos.get(pos).getcurso());
        System.out.println("Deporte -" + alumnos.get(pos).getdeporte());
        if (alumnos.get(pos).getsexo()) {
            sexo = "Hombre";
        } else {
            sexo = "Mujer";
        }
        System.out.println("Sexo -" + sexo);
        System.out.println("**************");
    }

    //Muestra un alumno contenido en un objeto del tipo alumno
    //Parámetro Alumno elalumno - Alumno a mostrar
    private static void muestraalumno(Alumno elalumno) {
        String sexo;
        System.out.println("Nombre -" + elalumno.getnombre());
        System.out.println("Apellidos -" + elalumno.getapellidos());
        System.out.println("Edad -" + elalumno.getedad());
        System.out.println("Curso -" + elalumno.getcurso());
        System.out.println("Deporte -" + elalumno.getdeporte());
        if (elalumno.getsexo()) {
            sexo = "Hombre";
        } else {
            sexo = "Mujer";
        }
        System.out.println("Sexo -" + sexo);
        System.out.println("***************");
    }

    //Muestra toda la lista de alumnos
    public static void vertodalista() {
        for (Alumno elalumno : alumnos) {
            muestraalumno(elalumno);
        }
    }

    //devuelve la cantidad de alumnos en memoria
    public static int cuantosalumnos() {
        return alumnos.size();
    }

    //Devuelve una lista de los cursos que hay
    //Obtiene la lista revisando en los alumnos
    //Retorno - ArrayList<String> - Lista de cursos
    private static ArrayList<String> listacursos() {
        boolean sal = false;
        boolean encontrado = false;
        int cont = 0;
        ArrayList<String> cursos = new ArrayList<String>();
        for (Alumno elalumno : alumnos) {
            //recorre todos los alumnos
            sal = false;
            encontrado = false;
            cont = 0;
            //Comprueba si el curso del alumno está en la lista de cursos que se está construyendo
            while (!sal) {
                if (cont < cursos.size()) {
                    if (elalumno.getcurso().equals(cursos.get(cont))) {
                        encontrado = true;
                        sal = true;
                    } else {
                        cont++;
                    }
                } else {
                    sal = true;
                }
            }
            //Si no se ha encontrado el curso en la lista que estamos creando se añade a esta
            if (!encontrado) {
                cursos.add(elalumno.getcurso());
            }
        }
        //devolvemos la lista de cursos
        return cursos;
    }

    //Muestra los alumnos de un curso concreto
    //Parámetros - String curso - El nombre del curso para el cual se quieren buscar alumnos
    private static void muestraalumnosdecurso(String curso) {
        int cont = 0;
        int contacurso = 0;
        for (cont = 0; cont < alumnos.size(); cont++) {
            //para cada alumno
            if (alumnos.get(cont).getcurso().equals(curso)) {
                //Si el curso es el que se busca se muestra el alumno
                muestraalumnopos(cont);
                contacurso++;
            }
        }
        //Se informa de cuanto cursos se han encontrado.
        System.out.println("Se han encontrado " + contacurso + " alumnos del curso " + curso);
        /* 
        for(Alumno elalumno:alumnos)
        {
            if(elalumno.getcurso().equals(curso))
            {
                muestraalumno(elalumno);
            }
        }*/
    }

    //Se solicita al usuario que seleccione un curso para luego ver los alumnos de ese curso
    public static void consultaralumnoscurso() {
        Scanner entrada = new Scanner(System.in);
        //se leen los cursos
        ArrayList<String> cursos = listacursos();
        int elegido;
        int cont;
        if (cursos.size() > 0) {
            System.out.println("Cursos:");
            //Se muestran los cursos
            for (cont = 0; cont < cursos.size(); cont++) {
                System.out.println(cont + 1 + " -  " + cursos.get(cont));
            }
            System.out.println("Elige un curso introduciendo el número.");
            try {
                //el usuario elige un curso
                elegido = entrada.nextInt();
                if (elegido > 0 && elegido <= cursos.size()) {
                    //se muestran los alumnos del curso elegido
                    muestraalumnosdecurso(cursos.get(elegido - 1));
                }

            } catch (InputMismatchException e) {
                System.out.println("Error de entrada.");
            }
        } else {
            System.out.println("No existen cursos.");
        }

    }

    //Cuenta cuantos alumnos tiene un deporte
    //Parámetros String deporte - El nombre del deporte
    //Retorno int - Número de alumnos de ese deporte
    private static int cuentaalumnosdeporte(String deporte) {
        int conta = 0;
        for (Alumno elalumno : alumnos) {
            if (elalumno.getdeporte().equals(deporte)) {
                conta++;
            }
        }
        return conta;
    }

    //Consulta que no haya incidencias
    public static void consultaincidencia() {
        int numalumnos;
        boolean problema;
        for (String deporte : deportes) {
            //para cada deporte
            //se muestra el deporte
            System.out.println(deporte);
            problema = false;

            //se cuentan los alumnos que tiene ese deporte
            numalumnos = cuentaalumnosdeporte(deporte);

            if (numalumnos == 0) {
                //Si los alumnos son 0 ese deporte no se puede realizar
                System.out.println("El deporte " + deporte.toLowerCase() + " no se puede realizar porque no tiene alumnos inscritos.");
            } else {
                if (deporte.equals("Badminton")) {
                    //Bádminton no puede tener menos de dos jugadores ni ser impares.
                    if (numalumnos < 2) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque deben haber almenos dos alumnos inscritos.");
                        problema = true;
                    } else if (numalumnos % 2 != 0) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque no tiene un número de alumnos par.");
                        problema = true;
                    }

                } else if (deporte.equals("Futbol")) {
                    //Fútbol no puede tener menos de 22 jugadores
                    if (numalumnos < 22) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque no hay almenos 22 alumnos inscritos.");
                        problema = true;
                    }

                } else if (deporte.equals("Futbol sala") || deporte.equals("Básket")) {
                    //Basket no puede tener menos de 10 jugadores
                    if (numalumnos < 10) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque no hay almenos 10 alumnos inscritos.");
                        problema = true;
                    }

                } else if (deporte.equals("Voley playa")) {
                    //Voley playa no puede tener menos de 4 jugadores ni ser impares
                    if (numalumnos < 4) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque deben haber almenos cuatro alumnos inscritos.");
                        problema = true;
                    } else if (numalumnos % 2 != 0) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque no tiene un número de alumnos par.");
                        problema = true;
                    }
                } else if (deporte.equals("Basquet")) {
                    //Basket no puede tener menos de 10 jugadores
                    if (numalumnos < 10) {
                        System.out.println("No se puede realizar el deporte " + deporte.toLowerCase() + " porque no tiene un número de alumnos par.");
                        problema = true;
                    }

                }
                if (!problema) {
                    System.out.println("El deporte " + deporte.toLowerCase() + " se puede realizar Ok.");
                }
            }

        }
    }

    //Consulta si un alumno existe por el nombre y apellidos
    //Parámetros - String nombre - Nombre del alumno
    //String apellidos - Apellidos del alumno
    //Retorno int - Posición del alumno o -1 si no se encuentra
    private static int consultaralumno(String nombre, String apellidos) {
        boolean sal = false;
        boolean encontrado = false;
        int cont = 0;
        int devuelve;
        while (!sal) {
            if (cont < alumnos.size()) {
                //si se encuentra un alumno con ese nombre y apellidos se para de buscar
                if (alumnos.get(cont).getnombre().equals(nombre) && alumnos.get(cont).getapellidos().equals(apellidos)) {
                    encontrado = true;
                    sal = true;
                } else {
                    cont++;
                }
            } else {
                sal = true;
            }
        }
        if (encontrado) {
            //si se ha encontrado el alumno se devuelve la posición
            devuelve = cont;
        } else {
            //si no se ha encontrado el alumno se devuelve -1
            devuelve = -1;
        }
        return devuelve;
    }

    //Muestra los alumnos de un deporte ordenador
    //Parámetros String deporte - El nombre del deporte por el que buscar
    private static void muestraalumnosdeporteordenados(String deporte) {
        ArrayList<Alumno> alumnosdeporte = new ArrayList<>();
        System.out.println("Deporte " + deporte.toLowerCase());
        //se añaden los alumnos de ese deporte a una lista
        for (Alumno elalumno : alumnos) {
            if (elalumno.getdeporte().equals(deporte)) {
                alumnosdeporte.add(elalumno);
            }
        }
        //se ordena esa lista
        Collections.sort(alumnosdeporte);
        //se muestra el número de participantes de ese deporte
        System.out.println("Número de participantes " + alumnosdeporte.size());
        //se muestran los alumnos ordenados del deporte
        for (Alumno elalumno : alumnosdeporte) {
            muestraalumno(elalumno);
        }
        //se borra la lista
        alumnosdeporte.clear();
    }

    //Muestra una listas ordenadas de los alumnos de cada deporte
    public static void listaordenada() {

        if (deportes.size() > 0) {
            for (String deporte : deportes) {
                muestraalumnosdeporteordenados(deporte);
            }
        } else {
            System.out.println("No existen deportes.");
        }

    }

    //Elimina todos los alumnos de un deporte en concreto de la lista en memoria
    //Parámetros - String deporte - Nombre del deporte a eliminar
    private static void eliminaalumnosdeporte(String deporte) {
        int cont = 0;
        //se recorren los alumnos
        while (cont < alumnos.size()) {
            if (alumnos.get(cont).getdeporte().equals(deporte)) {
                //Si un alumno tiene ese deporte se elimina el alumno de la lista
                //y no se incrementa el contador para poder encontrar el siguiente
                alumnos.remove(cont);
            } else {
                cont++;
            }
        }
    }

    //Elimina del disco el archivo de un deporte en concreto
    //Esto sucede cuando se elimina el deporte
    //Parámetros String deporte - Nombre del deporte a eliminar
    private static void borrarchivodeporte(String deporte) {

        File archivo;
        archivo = new File(carpeta + File.separator + deporte + ".txt");

        if (archivo.delete()) {
            System.out.println("Archivo del deporte " + deporte + " eliminado.");
        } else {
            System.out.println("Error borrando archivo.");
        }
    }

    //Se solicita al usuario para eliminar un deporte
    public static void anulardeporte() {
        Scanner entrada = new Scanner(System.in);
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        String seguro = "";
        int opcion;
        System.out.println("Selecciona un deporte para eliminar.");
        //se muestran los deportes
        muestralistadeportes();

        try {
            //el usuario elige un deporte a eliminar
            opcion = entrada.nextInt();
            if (opcion > 0 && opcion <= deportes.size()) {
                System.out.println("Los siguientes alumnos se eliminarán.");
                //se muestran los alumnos del deporte
                muestraalumnosdeporteordenados(deportes.get(opcion - 1));
                //se pide confirmación
                System.out.println("¿Estás seguro de eliminar el deporte " + deportes.get(opcion - 1) + "? s/n");
                seguro = lector.readLine();
                if (seguro.charAt(0) == 's' || seguro.charAt(0) == 'S') {
                    //se eliminan todos los alumnnos de ese deporte de la memoria
                    eliminaalumnosdeporte(deportes.get(opcion - 1));
                    System.out.println("Alumnos del deporte " + deportes.get(opcion - 1) + " eliminados.");
                    //se elimina del disco del archivo del deporte
                    borrarchivodeporte(deportes.get(opcion - 1));
                    //se elimina el deporte de la lista de deportes
                    deportes.remove(opcion - 1);
                    System.out.println("Deporte eliminado.");
                } else if (seguro.charAt(0) == 'n' || seguro.charAt(0) == 'N') {
                    System.out.println("Eliminación cancelada");
                } else {
                    System.out.println("Opción incorrecta.");
                }

            } else {
                System.out.println("Opción incorrecta.");
            }
        } catch (Exception ex) {
            System.out.println("Error de entrada.");

        }

    }

    //Dice si existe la carpeta de datos
    //retorno boolean - Indica si la carpeta existe o no
    public static boolean existecarpeta() {
        File f = new File("." + File.separator + "datos");
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    //crea la carpeta con todos los ficheros de deportes en caso de no existir
    public static void creacarpetayficheros() {
        File f;
        //se crea la carpeta
        new File(carpeta).mkdirs();
        System.out.println("Carpeta creada.");

        for (String deporte : deportes) {
            //para cada deporte se crea su fichero
            f = new File(carpeta + File.separator + deporte + ".txt");
            try {
                f.createNewFile();
                System.out.println("Archivo " + deporte + ".txt creado.");
            } catch (IOException ex) {
                System.out.println("Ha habido un problema creando el fichero " + deporte + ".txt");
            }

        }
    }

    //Comprueba que si existen los ficheros de deportes
    //En caso de no existir un fichero se considera eliminado el deporte
    public static void compruebadeportes() {
        ArrayList<String> borra = new ArrayList<String>();
        File f;
        for (String deporte : deportes) {
            //Para cada deporte
            f = new File(carpeta + File.separator + deporte + ".txt");
            //Se mira si existe el archivo
            if (f.exists() && f.isFile()) {
                System.out.println("Archivo " + deporte + ".txt encontrado.");
            } else {
                //si el archivo no existe se añade el deporte a una lista para borrar
                System.out.println("El fichero " + deporte + ".txt no existe. Se ha eliminado el deporte antes.");
                //Me apunto el deporte a borrar
                borra.add(deporte);
            }
        }
        //Procedo a eliminar los deportes a borrar
        for (String cualborra : borra) {
            deportes.remove(cualborra);
        }
        System.out.println("Lista de deportes activos:");
        //se muestra la lista de deportes
        muestralistadeportes();
    }

    //muestra la lista de deportes
    private static void muestralistadeportes() {
        for (int cont = 0; cont < deportes.size(); cont++) {
            System.out.println(cont + 1 + " " + deportes.get(cont));
        }

    }

    //Cambia nombre de curso en la lista de alumnos y regraba los ficheros correspondientes
    private static int cambiaalumnoscursoyregraba(String origen, String destino) {
        boolean sal, encontrado;
        int cont;
        int contalum = 0;

        ArrayList<String> deportesescribe = new ArrayList<String>();
        //Se recorren todos los alumnos
        for (Alumno elalumno : alumnos) {
            //Si el alumno tiene el curso que queremos cambiar
            if (elalumno.getcurso().equals(origen)) {
                //le cambiamos el curso al alumno
                elalumno.setcurso(destino);
                //incrementamos el contador de alumnos con curso cambiado
                contalum++;
                cont = 0;
                sal = false;
                encontrado = false;
                //Añadimos el deporte del alumno cambiado a una lista
                //antes miramos si ya lo teníamos en la lista
                while (!sal) {
                    if (cont < deportesescribe.size()) {
                        if (elalumno.getdeporte().equals(deportesescribe.get(cont))) {

                            encontrado = true;
                            sal = true;
                        } else {
                            cont++;
                        }
                    } else {
                        sal = true;
                    }
                }
                if (!encontrado) {
                    //Si el deporte no estaba en la lista se añade
                    deportesescribe.add(elalumno.getdeporte());
                }

            }
        }
        //Recorremos la lista de deportes que han cambiado
        for (String deportegraba : deportesescribe) {
            //Se regraba el fichero de cada deporte cambiado
            regrabafichero(deportegraba);
            System.out.println("Fichero del deporte " + deportegraba.toLowerCase() + " regrabado.");
        }
        return contalum;
    }

    //Cambia el nombre de un curso preguntando al usuario
    public static void cambiarnombrecurso() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        Scanner entrada = new Scanner(System.in);
        //se leen los cursos
        ArrayList<String> cursos = listacursos();
        int elegido, cont, cuantos;

        boolean existecurso = false;
        boolean sal = false;
        String nuevocurso = "";
        if (cursos.size() > 0) {
            System.out.println("Cursos:");
            //Se muestran los cursos
            for (cont = 0; cont < cursos.size(); cont++) {
                System.out.println(cont + 1 + " -  " + cursos.get(cont));
            }
            System.out.println("Elige un curso introduciendo el número.");
            try {
                //el usuario elige un curso
                elegido = entrada.nextInt();
                if (elegido > 0 && elegido <= cursos.size()) {
                    //se muestran los alumnos del curso elegido
                    try {
                        System.out.println("Intrduce el nuevo nombre del curso");
                        nuevocurso = lector.readLine();
                        if (!nuevocurso.isEmpty() && nuevocurso != null) {
                            cont = 0;
                            while (!sal) {
                                if (cont < cursos.size()) {
                                    if (cursos.get(cont).equals(nuevocurso)) {
                                        sal = true;
                                        existecurso = true;
                                    } else {
                                        cont++;
                                    }
                                } else {
                                    sal = true;
                                }
                            }

                            if (!existecurso) {
                                cuantos = cambiaalumnoscursoyregraba(cursos.get(elegido - 1), nuevocurso);
                                System.out.println(cuantos + " alumnos han sido cambiados de curso.");
                            } else {
                                System.out.println("El curso ya existe.");
                            }
                        } else {
                            System.out.println("Error de entrada.");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error de entrada");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Error de entrada.");
            }
        } else {
            System.out.println("No existen cursos.");
        }
    }

    //Cambia el deporte de un alumno en concreto preguntando al usuario
    public static void cambiaralumnodeporte() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        Scanner entrada = new Scanner(System.in);
        String nombre, apellidos, deporte, deporteant;

        int posalu, posdeporte;
        try {
            //pedimos al usuario el nombre y apellids del usuario a modificar
            System.out.println("Vamos a introducir los datos del alumno a modificar el deporte.");
            System.out.println("Introduce el nombre.");
            nombre = lector.readLine();

            System.out.println("Introduce los apellidos.");
            apellidos = lector.readLine();
            //se busca el alumno
            posalu = consultaralumno(nombre, apellidos);
            if (posalu >= 0) {
                //Se guarda el nombre del deporte original del alumno
                deporteant = alumnos.get(posalu).getdeporte();
                System.out.println("El deporte actual del alumno es " + deporteant);
                System.out.println("Elige un deporte.");
                //se muestra la lista de deportes y se permite al usuario seleccionar uno
                muestralistadeportes();
                posdeporte = entrada.nextInt();
                posdeporte--;
                if (posdeporte >= 0 && posdeporte < deportes.size()) 
                {
                    //si el usuario ha introducido un deporte correcto
                    //Se obtiene el nombre del deporte que ha seleccionado el usuario
                    deporte = deportes.get(posdeporte);

                    if (!deporte.equals(deporteant)) //Si el deporte nuevo es diferente al anterior
                    {
                        //se le cambia el deporte al alumno en memoria
                        alumnos.get(posalu).setdeporte(deporte);
                        //se regraba el fichero del deporte anterior en el que desaparecerá el alumno
                        regrabafichero(deporteant);
                        //se regraba el fichero del deporte actual en el que aparecerá el alumno
                        regrabafichero(deporte);
                        System.out.println("Archivos de los deportes " + deporteant.toLowerCase() + " y " + deporte.toLowerCase() + " regrabados");
                    } 
                    else 
                    {
                        System.out.println("Has puesto el mismo deporte.");
                    }

               } 
               else 
               {
                    System.out.println("Error de selección");
               }
            }
            else
            {
                System.out.println("Alumno no encontrado.");
            }
        } catch (IOException ex) {
            System.out.println("Error de entrada.");
        }
    }
}
