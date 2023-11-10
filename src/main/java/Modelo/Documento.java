package Modelo;

import org.apache.solr.common.SolrDocument;

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
    private SolrDocument documento;

    public Documento()
    {
    }

    public Documento(Long id, String autor, String titulo, String contenido, SolrDocument documento)
    {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.contenido = contenido;
        this.documento = documento;
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

    public SolrDocument getDocumento()
    {
        return this.documento;
    }

    public void setDocumento(SolrDocument documento)
    {
        this.documento = documento;
    }
    
    @Override
    public String toString()
    {
        return "Documento{" + "id=" + id + ", autor=" + autor + ", titulo=" + titulo + ", contenido=" + contenido + '}';
    }    
}