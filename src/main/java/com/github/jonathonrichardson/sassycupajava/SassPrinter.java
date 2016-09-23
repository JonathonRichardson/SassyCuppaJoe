package com.github.jonathonrichardson.sassycupajava;

import com.github.jonathonrichardson.sassycupajava.node.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jon on 9/23/16.
 */
public class SassPrinter {
    private SassDocument sassDocument;
    private int indentLevel = 0;

    public SassPrinter(SassDocument document) {
        this.sassDocument = document;
    }

    String print() {
        Map<String, String> variables = new HashMap<>();
        sassDocument.evalVariables(variables);

        StringBuilder cssString = new StringBuilder();

        for (AbstractNode node : sassDocument.getNodes()) {
            if (node instanceof KeyValueNode) {
                cssString.append(printStatement((KeyValueNode) node));
            }
            else if (node instanceof CommentNode) {
                cssString.append(printCommentNode((CommentNode) node));
            }
            else if (node instanceof CommentNode) {
                cssString.append(printBlockNode((BlockScopeNode) node, null));
            }
        }

        return cssString.toString();
    }

    private String printBlockNode(BlockScopeNode node, List<Selector> leadingSelectors) {
        StringBuilder

    }

    private String printCommentNode(CommentNode node) {
        if (node.isCStyle()) {
            return StringUtils.replace(node.getCommentText(), "\n", "\n" + getIndent());
        }
        return "";
    }

    private String printStatement(KeyValueNode node) {
        return String.format(
                "%s%s: %s;\n",
                getIndent(),
                node.getKey(),
                node.renderValue()
        );
    }

    private String indent(String text) {
        return StringUtils.replace(text, "\n", "\n" + getIndent());
    }

    private String getIndent() {
        StringBuilder indent = new StringBuilder();

        for (int i = 0; i < indentLevel; i++) {
            indent.append("  ");
        }

        return indent.toString();
    }
}
