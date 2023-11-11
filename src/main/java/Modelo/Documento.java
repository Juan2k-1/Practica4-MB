package Modelo;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
public class Documento
{
    private Long id;
    private String autor;
    private String titulo;
    private String contenido;
    private Float score;

    public Documento()
    {
    }

    public Documento(Long id, String autor, String titulo, String contenido)
    {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public Documento(Long id, String autor, String titulo, String contenido, Float score)
    {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.contenido = contenido;
        this.score = score;
    }

    public Long getId()
    {
        return id;
    }

    public String getAutor()
    {
        return autor;
    }

    public void setAutor(String autor)
    {
        this.autor = autor;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getContenido()
    {
        return contenido;
    }

    public void setContenido(String contenido)
    {
        this.contenido = contenido;
    }  

    public Float getScore()
    {
        return score;
    }

    public void setScore(Float score)
    {
        this.score = score;
    }   
    
    @Override
    public String toString()
    {
        return "Documento{" + "id=" + id + ", autor=" + autor + ", titulo=" + titulo + ", contenido=" + contenido + '}';
    }    
}