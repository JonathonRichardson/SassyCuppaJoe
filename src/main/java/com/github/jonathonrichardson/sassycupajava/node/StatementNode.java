package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jon on 9/22/16.
 */
public class StatementNode extends AbstractNode {
    String key = null;
    String value = null;

    public StatementNode(Parser parser) throws IOException, InvalidSyntaxException {

    }

    @Override
    public String toCss(Map<String, String> variables) {
        return null;
    }
}
