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

public class AnalizadorJS {
    
    // Método que detecta errores en JavaScript
    public String detectarErrores(String codigo) {
        StringBuilder errores = new StringBuilder();
        String[] lineas = codigo.split("\n");

        // Detección de errores comunes
        for (int i = 0; i < lineas.length; i++) {
            String linea = lineas[i];
            
            // Error: falta de cierre de paréntesis
            if (linea.contains("(") && !linea.contains(")")) {
                errores.append("Error en línea ").append(i + 1).append(": Paréntesis sin cerrar\n");
            }

            // Error: falta de cierre de llaves
            int abrirLlaves = linea.length() - linea.replace("{", "").length();
            int cerrarLlaves = linea.length() - linea.replace("}", "").length();
            if (abrirLlaves != cerrarLlaves) {
                errores.append("Error en línea ").append(i + 1).append(": Llave sin cerrar\n");
            }

            // Error: falta de punto y coma
            if (!linea.trim().endsWith(";") && !linea.trim().isEmpty() && !linea.contains("{") && !linea.contains("}")) {
                errores.append("Error en línea ").append(i + 1).append(": Falta punto y coma al final de la instrucción\n");
            }
        }

        return errores.length() > 0 ? errores.toString() : "No se encontraron errores en JavaScript.\n";
    }

    private ArrayList<String> tokensReconocidos = new ArrayList<>();
    private ArrayList<String> erroresEncontrados = new ArrayList<>();

    // Palabras reservadas de JavaScript
    private static final String[] palabrasReservadas = {
        "function", "const", "let", "var", "if", "else", "for", "while", "return", 
        "switch", "case", "break", "continue", "try", "catch", "finally", "null", "undefined", "true", "false"
    };

    // Operadores Aritméticos, Relacionales, y Lógicos combinados
    private static final String[] operadores = {
        "\\+", "-", "\\*", "/", "%", "\\+\\+", "--", "==", "!=", "<", ">", "<=", ">=", "&&", "\\|\\|", "!"
    };

    // Expresiones Regulares para Números y Cadenas
    private static final String regexNumero = "\\b\\d+(\\.\\d+)?\\b"; // Números enteros y decimales
    private static final String regexCadena = "\"(.*?)\"|'(.*?)'"; // Cadenas entre comillas simples o dobles

    public String analizar(String codigo) {
        System.out.println("Iniciando análisis de JavaScript...");

        // Analizar palabras reservadas
        analizarPalabrasReservadas(codigo);
        
        // Analizar operadores
        analizarOperadores(codigo);

        // Analizar números y cadenas
        analizarNumeros(codigo);
        analizarCadenas(codigo);

        // Generar el archivo con los resultados del análisis
        generarReporteTokens();

        // Retornar el resultado como String
        StringBuilder resultado = new StringBuilder();
        resultado.append("Reporte de Tokens JavaScript:\n");
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

    private void analizarPalabrasReservadas(String codigo) {
        for (String palabra : palabrasReservadas) {
            Pattern pattern = Pattern.compile("\\b" + palabra + "\\b");
            Matcher matcher = pattern.matcher(codigo);
            while (matcher.find()) {
                tokensReconocidos.add("Palabra reservada: " + matcher.group() + " encontrada en posición " + matcher.start());
            }
        }
    }

    private void analizarOperadores(String codigo) {
        for (String operador : operadores) {
            Pattern pattern = Pattern.compile(operador);
            Matcher matcher = pattern.matcher(codigo);
            while (matcher.find()) {
                tokensReconocidos.add("Operador: " + matcher.group() + " encontrado en posición " + matcher.start());
            }
        }
    }

    private void analizarNumeros(String codigo) {
        Pattern pattern = Pattern.compile(regexNumero);
        Matcher matcher = pattern.matcher(codigo);
        while (matcher.find()) {
            tokensReconocidos.add("Número: " + matcher.group() + " encontrado en posición " + matcher.start());
        }
    }

    private void analizarCadenas(String codigo) {
        Pattern pattern = Pattern.compile(regexCadena);
        Matcher matcher = pattern.matcher(codigo);
        while (matcher.find()) {
            tokensReconocidos.add("Cadena: " + matcher.group() + " encontrada en posición " + matcher.start());
        }
    }

    // Generar un archivo HTML con el reporte de tokens
    private void generarReporteTokens() {
        try (FileWriter writer = new FileWriter("reporte_tokens_js.html")) {
            writer.write("<!DOCTYPE html>\n<html>\n<head>\n<title>Reporte de Tokens JavaScript</title>\n</head>\n<body>\n");
            writer.write("<h1>Reporte de Tokens JavaScript</h1>\n");
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
            System.out.println("Reporte de tokens JavaScript generado exitosamente: reporte_tokens_js.html");
        } catch (IOException e) {
            System.err.println("Error al generar el reporte de tokens JavaScript: " + e.getMessage());
        }
    }
}
