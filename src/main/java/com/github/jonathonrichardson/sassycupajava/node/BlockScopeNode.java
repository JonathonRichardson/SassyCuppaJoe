package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 9/22/16.
 */
public class BlockScopeNode extends AbstractNode {
    List<AbstractNode> nodes = new ArrayList<>();

    /**
     * This constructor should always take a stream and read the next characters as if they were that type of syntax
     * node.  It should advance the stream just beyond where it needs to be to start the next node.  It should throw
     * an error if the stream doesn't match the node type.
     *
     * @param parser
     * @throws IOException
     */
    public BlockScopeNode(Parser parser) throws IOException {

    }

    @Override
    public String toCss() {
        return null;
    }
}
