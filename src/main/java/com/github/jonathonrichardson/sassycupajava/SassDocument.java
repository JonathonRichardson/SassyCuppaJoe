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

    public SassDocument(BufferedReader inputStream) throws IOException {
        parseNode(inputStream);
    }

    private AbstractNode parseNode(BufferedReader inputStream) throws IOException {
        inputStream.mark(1000); // For now, assume that we're not going to need to read more than 1000 characters before determining the node type

        int charCode;
        while (!this.streamCompletelyRead && (charCode = inputStream.read()) != -1) {
            char currentChar = (char) charCode;

            if (currentChar == '{') {
                inputStream.reset();
                return new BlockScopeNode(inputStream);
            }
        }

        return null;
    }

    public String toString() {
        StringBuilder cssString = new StringBuilder();

        for (AbstractNode node : nodes) {
            cssString.append(node.toCss());
        }

        return cssString.toString();
    }
}
