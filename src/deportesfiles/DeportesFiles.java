/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deportesfiles;

import deportesfiles.Metodos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author arturv
 */
public class DeportesFiles {

   

    /**
     * @param args the command line arguments
     */
    //Crea los deportes que hay por defecto.
 

    //Principal
    public static void main(String[] args) {
        // TODO code application logic here
        int opcion = 99;
        Scanner entrada = new Scanner(System.in);
        //se crea la lista de deportes por defecto en memoria
        Metodos.creadeportesdefecto();
        //Si la carpeta datos existe
        if (Metodos.existecarpeta()) {
            System.out.println("La carpeta existe. OK.");
            //se comprueba que existen todos los archivos de deportes
            //si falta alguno es que se ha eliminado el deporte
            Metodos.compruebadeportes();
            //se leen los ficheros de los deportes poniendo los alumnos en memoria
            Metodos.leeficheros();
            //se muestra la cantidad de alumnos en memoria
            System.out.println("Existen " + Metodos.cuantosalumnos() + " alumnos en memoria.");
        } else {
            System.out.println("La carpeta no existe.");
            //si la carpeta no existe se crean la carpeta y todos los ficheros de deportes
            Metodos.creacarpetayficheros();
        }
        //Se muestra el menú
        do {
            System.out.println("Menu principal");
            System.out.println("1-Registrar inscripción de un alumno.");
            System.out.println("2-Consultar alumnos que hay inscritos en un determinado curso.");
            System.out.println("3-Consultar incidencia.");
            System.out.println("4-Eliminar una inscripción de un alumno.");
            System.out.println("5-Ver listado agrupado por deporte y alumnos alfabeticamente.");
            System.out.println("6-Anular deporte.");
            System.out.println("7-Ver toda la lista de alumnos.");
            System.out.println("8-Cambiar un alumno de deporte");
            System.out.println("9-Cambiar nombre a un curso");
            System.out.println("0-Salir");
            System.out.println("Introduce una opción:");

            try {

                opcion = entrada.nextInt();

                switch (opcion) {
                    case 1:
                        Metodos.registraralumno();
                        break;
                    case 2:
                        Metodos.consultaralumnoscurso();
                        break;
                    case 3:
                        Metodos.consultaincidencia();
                        break;
                    case 4:
                        Metodos.eliminarinscripcion();
                        break;
                    case 5:
                        Metodos.listaordenada();
                        break;
                    case 6:
                        Metodos.anulardeporte();
                        break;
                    case 7:
                        Metodos.vertodalista();
                        break;
                    case 8:
                        Metodos.cambiaralumnodeporte();
                        break;
                    case 9:
                        Metodos.cambiarnombrecurso();
                        break;
                    default:
                        System.out.println("Opción incorrecta.");
                }
            } catch (Exception ex) {
                System.out.println("Error entrando opción del menú.");
                entrada.nextLine();
            }
        } while (opcion != 0);

    } 
}
