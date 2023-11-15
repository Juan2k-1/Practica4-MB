package Controlador;

import Modelo.Consulta;
import Modelo.ConsultaTabla;
import Modelo.Documento;
import Modelo.DocumentoTabla;
import Vista.VistaConsultas;
import Vista.VistaIndexarDocumentos;
import Vista.VistaMensaje;
import Vista.VistaMostrarDocumentosIndexados;
import Vista.VistaPorDefecto;
import Vista.VistaPrincipal;
import Vista.VistaSeleccionarFichero;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author Juan Alberto Dominguez Vazquez
 */
public class ControladorPrincipal implements ActionListener
{

    private VistaMensaje vMensaje;
    private VistaPorDefecto vPorDefecto;
    private VistaPrincipal vPrincipal;
    private VistaIndexarDocumentos vIndexar;
    private VistaSeleccionarFichero vSeleccionarFichero;
    private VistaMostrarDocumentosIndexados vDocumentosIndexados;
    private VistaConsultas vConsultas;
    private DocumentoTabla dTabla;
    private ConsultaTabla cTabla;
    private SolrClient solrClient;
    private ArrayList<Documento> documentos;
    private Process process;

    public ControladorPrincipal(VistaMensaje vMensaje, SolrClient solrClient, Process process)
    {
        this.vMensaje = vMensaje;
        this.solrClient = solrClient;
        this.vPorDefecto = new VistaPorDefecto();
        this.vPrincipal = new VistaPrincipal();
        this.vIndexar = new VistaIndexarDocumentos();
        this.vDocumentosIndexados = new VistaMostrarDocumentosIndexados();
        
        this.vSeleccionarFichero = new VistaSeleccionarFichero();
        this.vConsultas = new VistaConsultas();
        this.dTabla = new DocumentoTabla(vDocumentosIndexados);
        this.cTabla = new ConsultaTabla(vConsultas);
        this.process = process;

        addListeners();

        this.vPrincipal.setLayout(new CardLayout());
        //this.vDocumentosIndexados.setLayout(new BorderLayout());
        //this.vDocumentosIndexados.add(contentPane, BorderLayout.CENTER);
        
        this.vPrincipal.add(vPorDefecto);
        this.vPrincipal.add(vIndexar);
        this.vPrincipal.add(vSeleccionarFichero);
        this.vPrincipal.add(vDocumentosIndexados);
        this.vPrincipal.add(vConsultas);

        this.vPorDefecto.setVisible(true);
        this.vIndexar.setVisible(false);
        this.vSeleccionarFichero.setVisible(false);
        this.vDocumentosIndexados.setVisible(false);
        this.vConsultas.setVisible(false);

        this.vPrincipal.setLocationRelativeTo(null); //Para que la ventana se muestre en el centro de la pantalla
        this.vPrincipal.setVisible(true); // Para hacer la ventana visible
        this.vPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Para que la ventana se cierra cuando le doy a cerrar
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "IndexarDocumentos":
            {
                this.vPorDefecto.setVisible(false);
                this.vIndexar.setVisible(false);
                this.vDocumentosIndexados.setVisible(false);
                this.vSeleccionarFichero.setVisible(false);
                this.vIndexar.setVisible(true);
                break;
            }
            case "SeleccionarFichero":
            {
                this.vPorDefecto.setVisible(false);
                this.vIndexar.setVisible(false);
                this.vDocumentosIndexados.setVisible(false);
                this.vSeleccionarFichero.setVisible(true);
                int seleccion = this.vSeleccionarFichero.jFileChooserFicheros.showOpenDialog(null);
                if (seleccion == JFileChooser.APPROVE_OPTION)
                {
                    File ficheroSeleccionado = this.vSeleccionarFichero.jFileChooserFicheros.getSelectedFile();
                    String cisiAllFilePath = ficheroSeleccionado.getAbsolutePath();
                    this.vIndexar.jTextFieldFichero.setText(cisiAllFilePath);
                    if (cisiAllFilePath == null)
                    {
                        this.vMensaje.MensajeDeError("Error, ruta vacía");
                    } else
                    {
                        try
                        {
                            this.documentos = indexarDocumentos(solrClient, cisiAllFilePath);
                            this.vMensaje.MensajeInformacion("¡Documentos indexados con éxito!");
                        } catch (IOException | SolrServerException ex)
                        {
                            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;
            }
            case "MostrarDocumentosIndexados":
            {
                if (this.documentos == null)
                {
                    this.vMensaje.MensajeDeError("Error, no hay documentos indexados");
                } else
                {
                    this.vDocumentosIndexados.setVisible(true);
                    this.vPorDefecto.setVisible(false);
                    this.vIndexar.setVisible(false);
                    this.vSeleccionarFichero.setVisible(false);
                    this.vDocumentosIndexados.jTableMostrarDocumentos.setModel(dTabla);
                    DiseñoTablaDocumentos();
                    pideDocumentos();
                }
                break;
            }
            case "RealizarConsulta":
            {
                this.vConsultas.setVisible(true);
                this.vDocumentosIndexados.setVisible(false);
                this.vPorDefecto.setVisible(false);
                this.vIndexar.setVisible(false);
                this.vSeleccionarFichero.setVisible(false);
                this.vConsultas.jTableConsultas.setModel(cTabla);
                DiseñoTablaConsultas();
                break;
            }
            case "Buscar":
            {
                int i = 1;
                String contenido = this.vConsultas.jTextFieldConsulta.getText();
                Consulta consulta = new Consulta(i, contenido);
                ArrayList<Documento> documentos = null;
                try
                {
                    documentos = buscar(consulta);
                } catch (SolrServerException | IOException ex)
                {
                    Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                documentosEncontrados(documentos);
                i++;
                break;
            }

            case "Desconectar":
            {
                try
                {
                    this.solrClient.close();
                    this.vMensaje.MensajeInformacion("Desconectado del servidor con éxito");
                } catch (IOException ex)
                {
                    Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "Cerrar Servidor":
            {
                stopSolrServer();
                this.vMensaje.MensajeInformacion("Servidor Solr, cerrado con éxito");
                this.vPrincipal.dispose();
                break;
            }
            case "Salir":
            {
                this.vPrincipal.dispose();
                System.exit(0);
                break;
            }
        }
    }

    private ArrayList<Documento> buscar(Consulta consulta) throws SolrServerException, IOException
    {
        ArrayList<Documento> documentos = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery();
        String solrUrl = "http://localhost:8983/solr/CORPUS";
        SolrClient solrClientmicoleccion = new HttpSolrClient.Builder(solrUrl).build();
        solrQuery.setQuery("content:" + consulta.getContenido());
        solrQuery.set("fl", "id,author,title,content,score");

        QueryResponse response = solrClientmicoleccion.query(solrQuery);
        SolrDocumentList results = response.getResults();
        for (int i = 0; i < results.size(); i++)
        {
            SolrDocument document = results.get(i);
            Long id = Long.valueOf((String) document.getFieldValue("id"));
            String author = document.getFieldValue("author").toString();
            String title = document.getFieldValue("title").toString();
            String content = document.getFieldValue("content").toString();
            Float score = (Float) document.getFieldValue("score");
            Documento documento = new Documento(id, author, title, content, score);
            documentos.add(documento);
        }
        return documentos;
    }

    private void addListeners()
    {
        this.vPrincipal.jMenuItemCerrarServidor.addActionListener(this);
        this.vPrincipal.jMenuItemDesconectar.addActionListener(this);
        this.vPrincipal.jMenuItemIndexar.addActionListener(this);
        this.vPrincipal.jMenuItemMostrarIndexados.addActionListener(this);
        this.vPrincipal.jMenuItemRealizarConsulta.addActionListener(this);
        this.vPrincipal.jMenuItemSalir.addActionListener(this);
        this.vIndexar.jButtonSeleccionarFichero.addActionListener(this);
        this.vConsultas.jButtonBuscar.addActionListener(this);
    }

    private ArrayList<Documento> indexarDocumentos(SolrClient solr, String cisiAllFilePath) throws IOException, SolrServerException
    {
        //String filePath = "src\\main\\java\\resources\\CISI.ALL";
        Path pathToDocument = null;
        BufferedReader br = null;

        pathToDocument = Paths.get(cisiAllFilePath);
        br = Files.newBufferedReader(pathToDocument.toAbsolutePath());

        String line;
        String marcaFinTexto = ".X";
        boolean inDocument = false;
        String id = null;
        String title = null;
        String author = null;
        StringBuilder content = new StringBuilder();
        ArrayList<Documento> documents = new ArrayList();

        while ((line = br.readLine()) != null)
        {
            if (line.startsWith(".I"))
            {
                // Nuevo documento comienza
                if (inDocument)
                {
                    // Si ya estábamos en un documento, enviamos el documento a Solr
                    SolrInputDocument document = new SolrInputDocument();
                    document.addField("id", id);
                    document.addField("title", title);
                    document.addField("author", author);
                    document.addField("content", content.toString());
                    solr.add("CORPUS", document);
                    Documento documento = new Documento(Long.parseLong(id), author, title, content.toString());
                    documents.add(documento);
                }
                inDocument = true;
                id = line.substring(3).trim(); // Obtener el ID del documento
                content.setLength(0); // Limpiar el contenido
            } else if (line.startsWith(".T"))
            {
                // Título del documento
                title = br.readLine().trim();
            } else if (line.startsWith(".A"))
            {
                // Autor del documento
                author = br.readLine().trim();
            } else if (line.startsWith(".W"))
            {
                while((line = br.readLine()) != null && !line.equals(marcaFinTexto)) {
                    content.append(line.trim()).append(" ");
                }  
            }
        }

        // Procesamos el último documento 
        if (inDocument)
        {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", id);
            document.addField("title", title);
            document.addField("author", author);
            document.addField("content", content.toString());
            solr.add("CORPUS", document);
            Documento documento = new Documento(Long.parseLong(id), author, title, content.toString());
            documents.add(documento);
        }

        // Enviar los cambios al servidor Solr
        solr.commit("CORPUS");
        br.close();
        return documents;
    }

    /**
     * Diseño de la tabla documentos con el espacio entre columnas y la
     * prohibición de editar celdas
     */
    private void DiseñoTablaDocumentos()
    {
        //Para no permitir el redimensionamiento de las columnas con el ratón
        vDocumentosIndexados.jTableMostrarDocumentos.getTableHeader().setResizingAllowed(false);
        vDocumentosIndexados.jTableMostrarDocumentos.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(0).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(1).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(2).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(3).setPreferredWidth(1000);
    }

    /**
     * Diseño de la tabla consultas con el espacio entre columnas y la
     * prohibición de editar celdas
     */
    private void DiseñoTablaConsultas()
    {
        //Para no permitir el redimensionamiento de las columnas con el ratón
        vDocumentosIndexados.jTableMostrarDocumentos.getTableHeader().setResizingAllowed(false);
        vDocumentosIndexados.jTableMostrarDocumentos.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(0).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(1).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(2).setPreferredWidth(90);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(3).setPreferredWidth(1000);
        vDocumentosIndexados.jTableMostrarDocumentos.getColumnModel().getColumn(4).setPreferredWidth(90);
    }

    /**
     * Este metodo rellena la tabla de documentos con los documentos de solr
     */
    private void pideDocumentos()
    {
        this.dTabla.vaciarTablaDocumentos();
        this.dTabla.rellenarTablaDocumentos(this.documentos);
    }

    /**
     * Este metodo rellena la tabla de documentos con los documentos de solr
     */
    private void documentosEncontrados(ArrayList<Documento> documents)
    {
        this.cTabla.vaciarTablaConsultas();
        this.cTabla.rellenarTablaConsultas(documents);
    }

    private void stopSolrServer()
    {
        // Ruta al directorio donde se encuentra el script solr.cmd
        String solrBinPath = "C:\\Users\\juald\\Downloads\\solr-9.3.0-slim\\solr-9.3.0-slim\\bin\\";

        // Comando para detener el servidor Solr
        String command = "solr.cmd";
        String argument = "stop -all";

        // Construir la lista de comandos
        List<String> commands = new ArrayList<>();
        commands.add(solrBinPath + command);
        commands.add(argument);

        // Configurar ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);

        try
        {
            // Iniciar el proceso
            process = processBuilder.start();
            this.process.destroy();

            // Esperar a que el proceso termine
            int exitCode = process.waitFor();
            System.out.println("Proceso Solr terminado con código de salida: " + exitCode);
        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
