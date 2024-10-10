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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorCSS {

    private ArrayList<String> tokensReconocidos = new ArrayList<>();
    private ArrayList<String> erroresEncontrados = new ArrayList<>();

    // Método que detecta errores en el código CSS
   public String detectarErrores(String codigo) {
    StringBuilder errores = new StringBuilder();
    String[] lineas = codigo.split("\n");

    for (int i = 0; i < lineas.length; i++) {
        String linea = lineas[i];
        
        // Detectar propiedad sin punto y coma
        if (linea.contains(":") && !linea.contains(";")) {
            errores.append("Error en línea ").append(i + 1).append(": Propiedad CSS sin terminar con punto y coma\n");
        }

        // Detectar propiedad sin valor
        if (linea.matches("[a-zA-Z-]+:\\s*;")) {
            errores.append("Error en línea ").append(i + 1).append(": Propiedad CSS sin valor asignado\n");
        }
    }

    return errores.length() > 0 ? errores.toString() : "No se encontraron errores en CSS.\n";
}


    // Definir las reglas CSS y otros tokens importantes
    private static final String[] propiedadesCSS = {
        "color", "background-color", "font-size", "margin", "padding", "border", "width", "height",
        "display", "position", "top", "bottom", "left", "right", "z-index", "justify-content", "align-items",
        "border-radius", "float", "text-align", "box-shadow"
    };

    private static final String[] selectores = {
        "*", "body", "div", "span", "header", "main", "nav", "ul", "li", "h1", "h2", "h3", "h4", "h5", "h6", "p", 
        "section", "article", "footer", "aside"
    };

    private static final String[] colores = {
        "#[0-9a-fA-F]{3}", "#[0-9a-fA-F]{6}", "rgb\\([0-9]+, *[0-9]+, *[0-9]+\\)",
        "rgba\\([0-9]+, *[0-9]+, *[0-9]+, *[0-9]+(\\.\\d+)?\\)"
    };

    public String analizar(String codigo) {
        System.out.println("Iniciando análisis de CSS...");
        
        // Análisis léxico básico de CSS
        analizarSelectores(codigo);
        analizarPropiedadesCSS(codigo);
        analizarColores(codigo);

        // Generar el archivo con los resultados del análisis
        generarReporteTokens();

        // Retornar el resultado como String
        StringBuilder resultado = new StringBuilder();
        resultado.append("Reporte de Tokens CSS:\n");
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

    private void analizarSelectores(String codigo) {
        for (String selector : selectores) {
            Pattern pattern = Pattern.compile("\\b" + selector + "\\b");
            Matcher matcher = pattern.matcher(codigo);
            while (matcher.find()) {
                tokensReconocidos.add("Selector: " + matcher.group() + " encontrado en posición " + matcher.start());
            }
        }
    }

    private void analizarPropiedadesCSS(String codigo) {
        for (String propiedad : propiedadesCSS) {
            Pattern pattern = Pattern.compile("\\b" + propiedad + "\\b");
            Matcher matcher = pattern.matcher(codigo);
            while (matcher.find()) {
                tokensReconocidos.add("Propiedad CSS: " + matcher.group() + " encontrada en posición " + matcher.start());
            }
        }
    }

    private void analizarColores(String codigo) {
        for (String color : colores) {
            Pattern pattern = Pattern.compile(color);
            Matcher matcher = pattern.matcher(codigo);
            while (matcher.find()) {
                tokensReconocidos.add("Color: " + matcher.group() + " encontrado en posición " + matcher.start());
            }
        }
    }

    // Generar un archivo HTML con el reporte de tokens
    private void generarReporteTokens() {
        try (FileWriter writer = new FileWriter("reporte_tokens_css.html")) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Tokens CSS</title>\n</head>\n<body>\n");
            writer.write("<h1>Reporte de Tokens CSS</h1>\n");
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
            System.out.println("Reporte de tokens CSS generado exitosamente: reporte_tokens_css.html");
        } catch (IOException e) {
            System.err.println("Error al generar el reporte de tokens CSS: " + e.getMessage());
        }
    }
}
