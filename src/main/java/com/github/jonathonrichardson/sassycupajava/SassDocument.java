package com.github.jonathonrichardson.sassycupajava;

import com.github.jonathonrichardson.sassycupajava.node.AbstractNode;
import com.github.jonathonrichardson.sassycupajava.node.BlockScopeNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 9/21/16.
 */
public class SassDocument {
    private List<AbstractNode> nodes = new ArrayList<>();
    private boolean streamCompletelyRead = false;

    public SassDocument(BufferedReader inputStream) throws IOException, InvalidSyntaxException {
        Parser parser = new Parser(inputStream);

        nodes.addAll(parser.getNodes());
    }

    public String toString() {
        StringBuilder cssString = new StringBuilder();

        for (AbstractNode node : nodes) {
            cssString.append(node.toCss());
        }

        return cssString.toString();
    }
}
