package hr.algebra.java2.liars.dice.marina_spudic_java2.utils;

import javafx.scene.control.Alert;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class DocumentationUtils {
    private static final String SERIALIZATION_FILE_NAME = "documentation.html";

    public static void GenerateDocumentation(){
    StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>");
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<title>Documentation</title>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<h1>HTML dokumentacija projektnog zadatka iz Jave 2</h1>");
        builder.append("<p>Class list:</p>");

        try {
        List<Path> pathsList = Files.walk(Paths.get("."))
                .filter(path -> path.getFileName().toString().endsWith(".class"))
                //.forEach(System.out::println);
                .collect(Collectors.toList());


        for (Path path : pathsList) {

            String fileName = path.getFileName().toString();

            StringTokenizer tokenizer = new StringTokenizer(path.toString(), "\\");

            String fullQualifiedName = "";

            Boolean packageStart = false;

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                if ("classes".equals(token)) {
                    packageStart = true;
                    continue;
                }

                if (packageStart == false) {
                    continue;
                }

                if (token.endsWith(".class")) {
                    token = token.substring(0, token.indexOf("."));
                    fullQualifiedName += token;
                    break;
                }

                fullQualifiedName += token;
                fullQualifiedName += ".";
            }

            if ("module-info".equals(fullQualifiedName)) {
                continue;
            }

            builder.append("Class:");

            Class<?> klasa = Class.forName(fullQualifiedName);

            builder.append("<h2>" + Modifier.toString(klasa.getModifiers()) + " " + fullQualifiedName);
            builder.append("</h2>");
            builder.append("<br \\>");
            builder.append("<br \\>");

            builder.append("   Variables:");

            Field[] classFields = klasa.getDeclaredFields();

            StringBuilder fieldsStringBuilder = new StringBuilder();

            for (Field f : classFields) {
                fieldsStringBuilder.append(Modifier.toString(f.getModifiers()));
                fieldsStringBuilder.append(" ");
                fieldsStringBuilder.append(f.getType().getSimpleName());
                fieldsStringBuilder.append(" ");
                fieldsStringBuilder.append(f.getName());
                fieldsStringBuilder.append("<br />");
            }

            fieldsStringBuilder.append("<br />");

            builder.append("<h3>   " + fieldsStringBuilder + "</h3>");

            builder.append("   Constructors:");

            Constructor[] constructors = klasa.getConstructors();

            for (Constructor c : constructors) {

                String constructorParamString = generateExecutableDocumentation(c);

                builder.append("<h3>   " + c.getName() + "(" + constructorParamString + ")");
                builder.append("</h3>");
                builder.append("<br \\>");
            }

            builder.append("   Methods:");

            Method[] methods = klasa.getMethods();

            for (Method method : methods) {

                String methodMetaDataString = generateExecutableDocumentation(method);

                String methodMetaData = "";

                methodMetaData += Modifier.toString(method.getModifiers()) + " ";
                methodMetaData += method.getReturnType().getSimpleName() + " ";

                builder.append("<h3>   " + methodMetaData + " " + method.getName() + "(" + methodMetaDataString + ")");
                builder.append("</h3>");
                builder.append("<br \\>");
            }

            System.out.println(path.getFileName());
        }
    } catch (
    IOException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }

        builder.append("</body>");
        builder.append("</html>");

        try (
    FileWriter fw = new FileWriter(SERIALIZATION_FILE_NAME)) {
        fw.write(builder.toString());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation generated successfuly!");
        alert.setContentText("The file \"" + SERIALIZATION_FILE_NAME + "\" has been generated!");

        alert.showAndWait();

    } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error while creating documentation file");
        alert.setHeaderText("Cannot generate documentation");
        alert.setContentText("Details: " + e.getMessage());

        alert.showAndWait();
    }
}

    private static <T extends Executable> String generateExecutableDocumentation(T executable) {
        Parameter[] constructorParams = executable.getParameters();

        String paramString = "";

        for (int i = 0; i < constructorParams.length; i++) {
            Parameter p = constructorParams[i];
            paramString += Modifier.toString(p.getModifiers());
            paramString += p.getType().getSimpleName() + " ";
            paramString += p.getName();

            if (i < (constructorParams.length - 1)) {
                paramString += ", ";
            }
        }

        return paramString;
    }

}
