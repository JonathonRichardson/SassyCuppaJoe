package com.github.jonathonrichardson.sassycupajava.node;

import com.github.jonathonrichardson.sassycupajava.InvalidSyntaxException;
import com.github.jonathonrichardson.sassycupajava.Parser;

import java.io.IOException;

/**
 * Created by jon on 9/23/16.
 */
public class CommentNode extends AbstractNode {
    boolean isCStyle;
    String commentText;

    public CommentNode(Parser parser) throws IOException, InvalidSyntaxException {
        parser.consumeWhitespace();
        String start = parser.peek(2);

        if (start.equals("//")) {
            isCStyle = false;
            commentText = parser.slurpToChar('\n');
        }
        else if (start.equals("/*")) {
            isCStyle = true;
            commentText = parser.slurpToChar("*/") + "*/";
        }
        else {
            throw new InvalidSyntaxException(String.format("Expected \"//\" or \"/*\", but got \"%s\"", start));
        }
    }

    public boolean isCStyle() {
        return isCStyle;
    }

    public String getCommentText() {
        return commentText;
    }
}
