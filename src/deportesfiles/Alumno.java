/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deportesfiles;

/**
 *
 * @author arturv
 */
public class Alumno implements Comparable<Alumno>{
    private String nombre;
    private String apellidos;
    private String curso;
    private boolean sexo;
    private int edad;
    private String deporte;
    
    public Alumno(String nombre, String apellidos, String curso, boolean sexo, int edad, String deporte)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.curso = curso;
        this.sexo = sexo;
        this.edad = edad;
        this.deporte = deporte;
    }
    
    public String getnombre()
    {
        return nombre;
    }
    
    public String getapellidos()
    {
        return apellidos;
    }
    
    public String getcurso()
    {
        return curso;
    }
    
    public boolean getsexo()
    {
        return sexo;
    }
    
    public int getedad()
    {
        return edad;
    }
    
    public String getdeporte()
    {
        return deporte;
    }
    
    public void setnombre(String nombre)
    {
        this.nombre = nombre;
    }
    
    public void setapellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }
    
    public void setcurso(String curso)
    {
        this.curso = curso;   
    }
    
    public void setsexo(Boolean sexo)
    {
        this.sexo = sexo;
    }
    
    public void setedad(int edad)
    {
        this.edad = edad;
    }
    
    public void setdeporte(String deporte)
    {
        this.deporte = deporte;
    }

    @Override
    public int compareTo(Alumno o) {
        if (apellidos.toLowerCase().equalsIgnoreCase(o.getapellidos().toLowerCase())) {
            return nombre.compareTo(o.getnombre());
        }
        else
        {
            return apellidos.compareTo(o.getapellidos());
        }        
    }
}
