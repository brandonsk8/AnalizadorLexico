/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.analizadorlexico;

/**
 *
 * @author brandon
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class InterfazGrafica extends JFrame {

    private JTextArea textAreaCodigo;
    private JButton btnAnalizarCodigo, btnCargarArchivo, btnGenerarReportes;
    private JLabel etiquetaResultado;

    public InterfazGrafica() {
        setTitle("Analizador Léxico - HTML, CSS, JS Mixto");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Cambiar colores de fondo y fuente
        getContentPane().setBackground(new Color(230, 230, 250)); // Color lavanda claro

        // Panel superior con el área de texto
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(new Color(245, 245, 245)); // Fondo gris claro
        textAreaCodigo = new JTextArea(12, 50);
        textAreaCodigo.setFont(new Font("Consolas", Font.PLAIN, 14));
        textAreaCodigo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textAreaCodigo);
        panelCentro.add(new JLabel("Ingrese el códigopara analizar:"), BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Panel inferior con los botones
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(230, 230, 250)); // Igual al fondo principal

        btnAnalizarCodigo = crearBotonEstilizado("Analizar Código");
        btnCargarArchivo = crearBotonEstilizado("Cargar Archivo");
        btnGenerarReportes = crearBotonEstilizado("Generar Reportes");

        panelInferior.add(btnAnalizarCodigo);
        panelInferior.add(btnCargarArchivo);
        panelInferior.add(btnGenerarReportes);
        
        add(panelInferior, BorderLayout.SOUTH);

        // Panel para mostrar resultados
        JPanel panelResultado = new JPanel(new BorderLayout());
        panelResultado.setBackground(new Color(245, 245, 245));
        etiquetaResultado = new JLabel("Resultado del análisis aparecerá aquí...");
        etiquetaResultado.setFont(new Font("Arial", Font.BOLD, 12));
        panelResultado.add(etiquetaResultado, BorderLayout.CENTER);
        add(panelResultado, BorderLayout.NORTH);

        // Configuración de los eventos de los botones
        configurarEventos();

        setVisible(true);
    }

    // Crear un botón con estilo personalizado
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(100, 149, 237)); // Color azul claro
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void configurarEventos() {
        // Evento para el botón "Analizar Código Mixto"
        btnAnalizarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigo = textAreaCodigo.getText();
                codigo = optimizarCodigo(codigo); // Optimización de código
                String resultado = analizarCodigoMixto(codigo); // Unificar análisis de HTML, CSS, JS
                textAreaCodigo.setText(resultado);
                etiquetaResultado.setText("Análisis del código  completado.");
            }
        });

        // Evento para el botón "Cargar Archivo"
        btnCargarArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int resultado = fileChooser.showOpenDialog(null);
                if (resultado == JFileChooser.APPROVE_OPTION) {
                    File archivo = fileChooser.getSelectedFile();
                    cargarArchivo(archivo);
                }
            }
        });

        // Evento para el botón "Generar Reportes"
        btnGenerarReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteOptimizacion(textAreaCodigo.getText(), optimizarCodigo(textAreaCodigo.getText())); // Generar reporte de optimización
                generarReporteTokens(textAreaCodigo.getText()); // Generar el reporte de tokens
                generarReporteErrores(textAreaCodigo.getText()); // Generar el reporte de errores
                etiquetaResultado.setText("Reportes generados exitosamente.");
            }
        });
    }

    // Método para analizar el código mixto
    private String analizarCodigoMixto(String codigo) {
        AnalizadorHTML analizadorHTML = new AnalizadorHTML();
        AnalizadorCSS analizadorCSS = new AnalizadorCSS();
        AnalizadorJS analizadorJS = new AnalizadorJS();

        StringBuilder resultado = new StringBuilder();

        // Detectar las partes de HTML, CSS y JS basadas en tokens de estado >>[html], >>[css], >>[js]
        String[] lineas = codigo.split("\n");
        String estado = "";

        for (String linea : lineas) {
            if (linea.contains(">>[html]")) {
                estado = "html";
                continue;
            } else if (linea.contains(">>[css]")) {
                estado = "css";
                continue;
            } else if (linea.contains(">>[js]")) {
                estado = "js";
                continue;
            }

            switch (estado) {
                case "html":
                    resultado.append(analizadorHTML.analizar(linea)).append("\n");
                    break;
                case "css":
                    resultado.append(analizadorCSS.analizar(linea)).append("\n");
                    break;
                case "js":
                    resultado.append(analizadorJS.analizar(linea)).append("\n");
                    break;
                default:
                    resultado.append("Sin estado definido: ").append(linea).append("\n");
            }
        }

        return resultado.toString();
    }

    
    // Método para optimizar el código eliminando comentarios y líneas vacías
private String optimizarCodigo(String codigo) {
    // Eliminar comentarios en HTML, CSS y JavaScript
    codigo = codigo.replaceAll("<!--.*?-->", ""); // Comentarios HTML
    codigo = codigo.replaceAll("//.*", ""); // Comentarios de una línea en JS y CSS
    codigo = codigo.replaceAll("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/", ""); // Comentarios multilínea

    // Eliminar líneas vacías
    codigo = codigo.replaceAll("(?m)^\\s*$[\n\r]{1,}", "");
    
    return codigo.trim();
}


    // Método para cargar un archivo
    private void cargarArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            textAreaCodigo.setText(contenido.toString());
            etiquetaResultado.setText("Archivo cargado correctamente.");
        } catch (IOException e) {
            etiquetaResultado.setText("Error al cargar el archivo.");
        }
    }

   // Método para generar reporte de optimización
private void generarReporteOptimizacion(String codigoOriginal, String codigoOptimizado) {
    StringBuilder contenido = new StringBuilder();
    contenido.append("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Optimización</title>\n</head>\n<body>\n");
    contenido.append("<h1>Reporte de Optimización</h1>\n");
    contenido.append("<h2>Comentarios Eliminados y Líneas Vacías:</h2>\n");

    // Comparar el código original con el optimizado
    String[] lineasOriginales = codigoOriginal.split("\n");
    String[] lineasOptimizadas = codigoOptimizado.split("\n");

    contenido.append("<pre>");
    int minLineas = Math.min(lineasOriginales.length, lineasOptimizadas.length);
    for (int i = 0; i < minLineas; i++) {
        if (!lineasOptimizadas[i].equals(lineasOriginales[i])) {
            contenido.append("Eliminado: ").append(lineasOriginales[i]).append("\n");
        }
    }
    if (lineasOriginales.length > lineasOptimizadas.length) {
        for (int i = lineasOptimizadas.length; i < lineasOriginales.length; i++) {
            contenido.append("Eliminado: ").append(lineasOriginales[i]).append("\n");
        }
    }
    contenido.append("</pre>\n</body>\n</html>");

    // Guardar el reporte en la carpeta "reportes"
    guardarReporteSecuencial("reporte_optimizacion", contenido.toString(), ".html");
}




    // Método para generar reporte de tokens
private void generarReporteTokens(String codigo) {
    StringBuilder contenido = new StringBuilder();
    contenido.append("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Tokens</title>\n</head>\n<body>\n");
    contenido.append("<h1>Reporte de Tokens</h1>\n");

    // HTML Tokens
    contenido.append("<h2>Tokens de HTML</h2>\n<pre>\n");
    AnalizadorHTML analizadorHTML = new AnalizadorHTML();
    contenido.append(analizadorHTML.analizar(codigo)).append("</pre>\n");

    // CSS Tokens
    contenido.append("<h2>Tokens de CSS</h2>\n<pre>\n");
    AnalizadorCSS analizadorCSS = new AnalizadorCSS();
    contenido.append(analizadorCSS.analizar(codigo)).append("</pre>\n");

    // JS Tokens
    contenido.append("<h2>Tokens de JavaScript</h2>\n<pre>\n");
    AnalizadorJS analizadorJS = new AnalizadorJS();
    contenido.append(analizadorJS.analizar(codigo)).append("</pre>\n");

    contenido.append("</body>\n</html>");

    // Guardar el reporte en la carpeta "reportes"
    guardarReporteSecuencial("reporte_tokens", contenido.toString(), ".html");
}

            //
    // Método para generar el archivo secuencial en la carpeta reportes
private void guardarReporteSecuencial(String nombreBase, String contenido, String extension) {
    try {
        // Crear la carpeta "reportes" si no existe
        File directorioReportes = new File("reportes");
        if (!directorioReportes.exists()) {
            directorioReportes.mkdir(); // Crear la carpeta
        }

        // Determinar el número secuencial para el nuevo reporte
        int contador = 1;
        File archivoReporte;
        do {
            archivoReporte = new File(directorioReportes, nombreBase + "_" + contador + extension);
            contador++;
        } while (archivoReporte.exists()); // Buscar el siguiente número disponible

        // Guardar el reporte en el archivo
        try (FileWriter writer = new FileWriter(archivoReporte)) {
            writer.write(contenido);
        }

        System.out.println("Reporte generado exitosamente: " + archivoReporte.getAbsolutePath());
    } catch (IOException e) {
        System.err.println("Error al guardar el reporte: " + e.getMessage());
    }
}

    // Método para generar reporte de errores
private void generarReporteErrores(String codigo) {
    StringBuilder contenido = new StringBuilder();
    contenido.append("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Errores</title>\n</head>\n<body>\n");
    contenido.append("<h1>Reporte de Errores</h1>\n");

    // Lógica para encontrar errores en HTML, CSS, JS
    contenido.append("<h2>Errores de HTML</h2>\n<pre>\n");
    AnalizadorHTML analizadorHTML = new AnalizadorHTML();
    contenido.append(analizadorHTML.detectarErrores(codigo)).append("</pre>\n");

    contenido.append("<h2>Errores de CSS</h2>\n<pre>\n");
    AnalizadorCSS analizadorCSS = new AnalizadorCSS();
    contenido.append(analizadorCSS.detectarErrores(codigo)).append("</pre>\n");

    contenido.append("<h2>Errores de JavaScript</h2>\n<pre>\n");
    AnalizadorJS analizadorJS = new AnalizadorJS();
    contenido.append(analizadorJS.detectarErrores(codigo)).append("</pre>\n");

    contenido.append("</body>\n</html>");

    // Guardar el reporte en la carpeta "reportes"
    guardarReporteSecuencial("reporte_errores", contenido.toString(), ".html");
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazGrafica();
            }
        });
    }
}

