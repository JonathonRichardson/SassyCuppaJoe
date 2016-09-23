package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;
import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public String toCss(List<Selector> leadingSelectors, Map<String, String> variables) {
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

        List<AbstractNode> queue = new ArrayList<>();
        queue.addAll(nodes);

        AbstractNode curQueueEntry;
        while(queue.size() > 0) {
            curQueueEntry = queue.remove(0);

            if (curQueueEntry instanceof BlockScopeNode) {
                String css;
                css = ((BlockScopeNode) curQueueEntry).toCss(resolvedSelectors, variables);
                stringBuilder.append(css);
            }
            else {
                stringBuilder.append(commaSeparatedNumbers);
                stringBuilder.append(" {");

                stringBuilder.append(curQueueEntry.toCss(variables));

                while(!queue.isEmpty() && (queue.get(0) instanceof KeyValueNode)) {
                    stringBuilder.append(queue.remove(0).toCss(variables));
                }

                stringBuilder.append(" }\n");
            }

        }

        return stringBuilder.toString();
    }

    @Override
    public String toCss(Map<String, String> variables) {
        return this.toCss(new ArrayList<Selector>(), variables);
    }

    @Override
    public void evalVariables(Map<String, String> variables) {
        super.evalVariables(variables);

        for (AbstractNode node : nodes) {
            node.evalVariables(variables);
        }
    }
}
