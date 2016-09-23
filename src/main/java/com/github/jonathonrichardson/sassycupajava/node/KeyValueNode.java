package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jon on 9/22/16.
 */
public class KeyValueNode extends AbstractNode {
    private String key;
    private String value;

    public KeyValueNode(Parser parser) throws IOException, InvalidSyntaxException {
        this.key = StringUtils.trim(parser.slurpToChar(':'));
        System.out.printf("Key: \"%s\"\n", key);
        this.value = StringUtils.trim(parser.slurpToChar(';'));
        System.out.printf("Value: \"%s\"\n", value);

        parser.consumeWhitespace();
    }

    @Override
    public String toCss(Map<String, String> variables) {
        String valueText = value;

        for (String variable : variables.keySet()) {
            valueText = StringUtils.replace(valueText, variable, variables.get(variable));
        }

        if (key.startsWith("$")) {
            variables.put(key, valueText);
            return "";
        }
        else {
            return "\n  " + key + ": " + valueText + ";";
        }
    }
}
