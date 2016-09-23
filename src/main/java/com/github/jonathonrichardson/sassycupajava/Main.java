package com.github.jonathonrichardson.sassycupajava;

import java.io.*;

/**
 * Created by jon on 9/21/16.
 */
public class Main {
    public static void main(String[] args) {
        SassCompiler compiler = new SassCompiler();
        
        if (args.length > 0) {
            String pathName = args[0];
            File input = new File(pathName);

            if (!input.isFile() || !input.canRead()) {
                throw new RuntimeException("input must be a readable filename");
            }

            try {
                FileInputStream stream = new FileInputStream(input);

                BufferedReader inputStream = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(input), "UTF-8"
                        )
                );

                String compiledText = compiler.compile(inputStream);

                System.console().printf("%s", compiledText);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            System.console().printf("You need to supply a filename to this command.");
        }
    }
}
