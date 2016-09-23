package com.github.jonathonrichardson.sassycupajava;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;


import java.io.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by jon on 9/21/16.
 */
public class SassCompilerTest {
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    private boolean testAll = false;

    @Test
    public void testCompiler() throws IOException {
        SassCompiler compiler = new SassCompiler();
        print("hello");
        printf("CWD: %s\n", (new File(".")).getCanonicalPath());
        File specDir = new File("spec/basic");

        collector.checkThat(true, CoreMatchers.equalTo(false));

        if (specDir.isDirectory()) {
            for (File testdir : specDir.listFiles()) {
                if (testdir.isDirectory()) {
                    printf("%s\n", testdir.getName());
                    File inputFile = new File(testdir.toString() + "/input.scss");
                    File outputFile = new File(testdir.toString() + "/expected_output.css");

                    String compiledOutput = compiler.compile(inputFile);
                    String expectedOutput = FileUtils.readFileToString(outputFile, "UTF-8");

                    StringBuilder message = new StringBuilder();
                    message.append(String.format(
                            "Unexpected Output for %s.  Expected: \n",
                            testdir.getName()
                    ));
                    message.append("<<---------------------------\n");
                    message.append(expectedOutput);
                    message.append("<<---------------------------\n");
                    message.append("But got:\n");
                    message.append(">>---------------------------\n");
                    message.append(compiledOutput);
                    message.append(">>---------------------------\n");

                    if (!compiledOutput.equals(expectedOutput)) {
                        //print(message.toString());
                    }

                    if (testAll) {
                        collector.checkThat(compiledOutput, CoreMatchers.equalTo(expectedOutput));
                    }
                    else {
                        assertEquals(testdir.getName(), stripWhitespace(expectedOutput), stripWhitespace(compiledOutput));
                    }

                }
            }
        }
        else {
            fail("SpecDir should be a directory");
        }
    }

    public static String stripWhitespace(String text) {
        text = StringUtils.replace(text, " ",  "");
        text = StringUtils.replace(text, "\n", "");
        return text;
    }

    public static void print(String text) {
        System.out.print(text);
    }

    public static void printf(String text, Object... args) {
        System.out.printf(text, args);
    }
}