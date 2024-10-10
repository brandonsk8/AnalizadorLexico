/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package a.analizadorlexico;

/**
 *
 * @author brandon
 */



import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AnalizadorHTML {

    private ArrayList<String> tokensReconocidos = new ArrayList<>();
    private ArrayList<String> erroresEncontrados = new ArrayList<>();
    
    // Método que detecta errores en el código HTML
    public String detectarErrores(String codigo) {
        StringBuilder errores = new StringBuilder();
        String[] lineas = codigo.split("\n");

        // Detección de errores: etiquetas sin cerrar y etiquetas mal formadas
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            if (linea.contains("<") && !linea.contains(">")) {
                errores.append("Error en línea ").append(i + 1).append(": Etiqueta sin cerrar\n");
            }
            if (linea.matches("<[^/!][^>]*>[^<]*<[^/][^>]*>")) {
                errores.append("Error en línea ").append(i + 1).append(": Anidación incorrecta de etiquetas\n");
            }
        }

        return errores.length() > 0 ? errores.toString() : "No se encontraron errores en HTML.\n";
    }

    // Listado completo de etiquetas HTML personalizadas y sus traducciones
    private static final String[][] etiquetas = {
        {"<principal>", "<main>"},
        {"<principal/>", "</main>"},
        {"<encabezado>", "<header>"},
        {"<encabezado/>", "</header>"},
        {"<navegacion>", "<nav>"},
        {"<navegacion/>", "</nav>"},
        {"<apartado>", "<aside>"},
        {"<apartado/>", "</aside>"},
        {"<listaordenada>", "<ul>"},
        {"<listaordenada/>", "</ul>"},
        {"<listadesordenada>", "<ol>"},
        {"<listadesordenada/>", "</ol>"},
        {"<itemlista>", "<li>"},
        {"<itemlista/>", "</li>"},
        {"<anclaje>", "<a>"},
        {"<anclaje/>", "</a>"},
        {"<contenedor>", "<div>"},
        {"<contenedor/>", "</div>"},
        {"<seccion>", "<section>"},
        {"<seccion/>", "</section>"},
        {"<articulo>", "<article>"},
        {"<articulo/>", "</article>"},
        {"<titulo", "<h"}, // Especial, debe completarse con el número
        {"<titulo/>", "</h"}, // Especial, debe completarse con el número
        {"<parrafo>", "<p>"},
        {"<parrafo/>", "</p>"},
        {"<span>", "<span>"},
        {"<span/>", "</span>"},
        {"<entrada/>", "<input/>"},
        {"<formulario>", "<form>"},
        {"<formulario/>", "</form>"},
        {"<label>", "<label>"},
        {"<label/>", "</label>"},
        {"<area/>", "<textarea/>"},
        {"<boton>", "<button>"},
        {"<boton/>", "</button>"},
        {"<piepagina>", "<footer>"},
        {"<piepagina/>", "</footer>"}
    };

    // Método principal que analiza el código y devuelve el resultado como String
    public String analizar(String codigo) {
        System.out.println("Iniciando análisis de HTML...");
        
        // Reemplazar etiquetas personalizadas por las etiquetas HTML correspondientes
        String codigoTraducido = traducirEtiquetas(codigo);

        // Generar el archivo HTML traducido
        generarArchivoHTML(codigoTraducido);

        // Generar el reporte de tokens en un archivo HTML
        generarReporteTokens();

        // Retornar el resultado completo con el reporte de tokens y errores
        StringBuilder resultado = new StringBuilder();
        resultado.append("Código traducido:\n").append(codigoTraducido).append("\n\n");
        resultado.append("Reporte de Tokens:\n");
        
        for (String token : tokensReconocidos) {
            resultado.append(token).append("\n");
        }

        if (!erroresEncontrados.isEmpty()) {
            resultado.append("\nErrores encontrados:\n");
            for (String error : erroresEncontrados) {
                resultado.append(error).append("\n");
            }
        }

        return resultado.toString();
    }

    // Método que traduce las etiquetas personalizadas al formato HTML estándar
    private String traducirEtiquetas(String codigo) {
    int fila = 1;

    for (String[] etiqueta : etiquetas) {
        if (etiqueta[0].contains("<titulo")) {
            // Corregir la traducción de <titulo1> a <h1>, <titulo2> a <h2>, etc.
            codigo = codigo.replaceAll("(?i)<titulo([1-6])>", "<h$1>");
            codigo = codigo.replaceAll("(?i)</titulo([1-6])>", "</h$1>");
            tokensReconocidos.add("Etiqueta: <titulo> traducida a <h" + "$1" + "> en línea: " + fila);
        } else {
            // Reemplazar etiquetas personalizadas por etiquetas HTML estándar
            if (codigo.contains(etiqueta[0])) {
                tokensReconocidos.add("Etiqueta: " + etiqueta[0] + " traducida a " + etiqueta[1] + " en línea: " + fila);
            }
            codigo = codigo.replaceAll(Pattern.quote(etiqueta[0]), etiqueta[1]);
        }
    }
    return codigo;
}




   
    // Método que genera el archivo HTML con el código traducido
private void generarArchivoHTML(String codigoTraducido) {
    try (FileWriter writer = new FileWriter("codigo_traducido.html")) {
        // Generar el archivo HTML con una estructura correcta
        writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Código Traducido</title>\n</head>\n<body>\n");
        writer.write(codigoTraducido); // Agregar el código traducido
        writer.write("\n</body>\n</html>");
        System.out.println("Archivo HTML generado exitosamente: codigo_traducido.html");
    } catch (IOException e) {
        System.err.println("Error al generar el archivo HTML: " + e.getMessage());
    }
}


    // Método que genera un reporte de tokens en formato HTML
    private void generarReporteTokens() {
        try (FileWriter writer = new FileWriter("reporte_tokens.html")) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Tokens</title>\n</head>\n<body>\n");
            writer.write("<h1>Reporte de Tokens</h1>\n");
            writer.write("<table border='1'>\n");
            writer.write("<tr><th>Token</th><th>Descripción</th></tr>\n");

            for (String token : tokensReconocidos) {
                writer.write("<tr><td>" + token + "</td><td>Token reconocido</td></tr>\n");
            }

            writer.write("</table>\n");

            if (!erroresEncontrados.isEmpty()) {
                writer.write("<h2>Errores Encontrados</h2>\n");
                writer.write("<ul>\n");
                for (String error : erroresEncontrados) {
                    writer.write("<li>" + error + "</li>\n");
                }
                writer.write("</ul>\n");
            }

            writer.write("</body>\n</html>");
            System.out.println("Reporte de tokens generado exitosamente: reporte_tokens.html");
        } catch (IOException e) {
            System.err.println("Error al generar el reporte de tokens: " + e.getMessage());
        }
    }
}
