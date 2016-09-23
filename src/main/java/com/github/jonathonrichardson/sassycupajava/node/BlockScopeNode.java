package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;
import com.sun.istack.internal.NotNull;
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

    public String toCss(List<Selector> leadingSelectors) {
        if (leadingSelectors == null) {
            leadingSelectors = new ArrayList<>();
        }

        List<Selector> resolvedSelectors = new ArrayList<>();
        if (leadingSelectors.size() > 0) {
            for (Selector selector : leadingSelectors) {
                for (Selector innerSelector : this.selectors) {
                    resolvedSelectors.add(new Selector(selector.text + " " + innerSelector.text));
                }
            }
        }
        else {
            resolvedSelectors = this.selectors;
        }

        StringBuilder stringBuilder = new StringBuilder();

        String commaSeparatedNumbers = resolvedSelectors.stream()
                .map(i -> i.text)
                .collect(Collectors.joining(", "));

        for (AbstractNode node: nodes) {
            String css;

            if (node instanceof BlockScopeNode) {
                css = ((BlockScopeNode) node).toCss(resolvedSelectors);
                stringBuilder.append(css);
            }
            else {
                stringBuilder.append(commaSeparatedNumbers);
                stringBuilder.append(" {");
                stringBuilder.append(node.toCss());
                stringBuilder.append("}\n");
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public String toCss() {
        return this.toCss(null);
    }
}
