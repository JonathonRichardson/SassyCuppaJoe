package com.github.jonathonrichardson.sassycupajava;

import com.sun.tools.javac.resources.compiler;

import java.io.*;

/**
 * Created by jon on 9/21/16.
 */
public class SassCompiler {
    public String compile(BufferedReader sassText) {
        return "";
    }

    public String compile(File file) {
        BufferedReader inputStream = null;
        String compiledText = null;
        try {
            inputStream = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF-8"
                    )
            );

            SassDocument document = new SassDocument(inputStream);
            compiledText = document.toString();
        } catch (UnsupportedEncodingException|InvalidSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compiledText;
    }
}
