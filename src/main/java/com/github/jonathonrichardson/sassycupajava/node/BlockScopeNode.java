package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jon on 9/22/16.
 */
public class BlockScopeNode extends AbstractNode {
    List<AbstractNode> nodes = new ArrayList<>();
    List<Selector> selectors = new ArrayList<>();

    /**
     * This constructor should always take a stream and read the next characters as if they were that type of syntax
     * node.  It should advance the stream just beyond where it needs to be to start the next node.  It should throw
     * an error if the stream doesn't match the node type.
     *
     * @param parser
     * @throws IOException
     */
    public BlockScopeNode(Parser parser) throws IOException, InvalidSyntaxException {
        for (String selectorText : parser.slurpToChar('{').split(",")) {
            this.selectors.add(new Selector(StringUtils.trim(selectorText)));
        }

        parser.consumeWhitespace();
        this.nodes.addAll(parser.getNodes());
    }

    @Override
    public String toCss() {
        StringBuilder stringBuilder = new StringBuilder();

        String commaSeparatedNumbers = selectors.stream()
                .map(i -> i.text)
                .collect(Collectors.joining(", "));

        stringBuilder.append(commaSeparatedNumbers);
        stringBuilder.append(" {");

        for (AbstractNode node: nodes) {
            stringBuilder.append(node.toCss());
        }

        stringBuilder.append(" }\n");

        return stringBuilder.toString();
    }
}
