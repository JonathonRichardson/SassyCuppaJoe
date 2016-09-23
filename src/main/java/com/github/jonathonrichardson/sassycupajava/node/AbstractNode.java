package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jon on 9/21/16.
 */
public abstract class AbstractNode {
    private Map<String, String> context = new HashMap<>();
    protected String originalText = null;

    protected AbstractNode() {}

    /**
     * This constructor should always take a stream and read the next characters as if they were that type of syntax
     * node.  It should advance the stream just beyond where it needs to be to start the next node.  It should throw
     * an error if the stream doesn't match the node type.
     *
     *
     * @param parser
     * @throws IOException
     */
    public AbstractNode(Parser parser) throws IOException, InvalidSyntaxException {
        this.originalText = parser.readToEnd();
    }

    protected Map<String, String> getVariableContext() {
        return context;
    }

    public void evalVariables(Map<String, String> variables) {
        this.context.clear();

        // Cache the variables for interpolation later
        for(String key : context.keySet()) {
            this.context.put(key, context.get(key));
        }
    }

    public String renderValueInContext(String value) {
        String valueText = value;

        for (String variable : getVariableContext().keySet()) {
            valueText = StringUtils.replace(valueText, variable, getVariableContext().get(variable));
        }

        return valueText;
    }
}
