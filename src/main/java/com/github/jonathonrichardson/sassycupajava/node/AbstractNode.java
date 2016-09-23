package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;

import java.io.IOException;

/**
 * Created by jon on 9/21/16.
 */
public abstract class AbstractNode {
    private String originalText = null;

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

    public abstract String toCss();
}
