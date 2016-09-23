package com.github.jonathonrichardson.sassycupajava;

import com.github.jonathonrichardson.sassycupajava.node.AbstractNode;
import com.github.jonathonrichardson.sassycupajava.node.BlockScopeNode;
import com.github.jonathonrichardson.sassycupajava.node.CommentNode;
import com.github.jonathonrichardson.sassycupajava.node.KeyValueNode;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 9/22/16.
 */
public class Parser {
    BufferedReader stream;

    public Parser(BufferedReader stream) {
        this.stream = stream;
    }

    public Character readChar() throws IOException {
        int charCode = stream.read();

        if (charCode != -1) {
            return new Character((char) charCode);
        }
        else {
            return null;
        }
    }

    public AbstractNode readNode() throws IOException, InvalidSyntaxException {
        stream.mark(1000);
        AbstractNode node = null;
        StringBuilder buffer = new StringBuilder();

        this.consumeWhitespace();
        if (this.atEnd()) {
            return null;
        }

        String peak2 = this.peek(2);
        if (peak2.equals("/*") || peak2.equals("//")) {
            return new CommentNode(this);
        }

        while(true) {
            Character nextChar = this.readChar();

            if (nextChar == null && buffer.length() != 0) {
                throw new InvalidSyntaxException(String.format(
                        "Unexpected text at end of input: \"%s\"",
                        this.readToEnd()
                ));
            }
            else {
                buffer.append(nextChar);
            }

            if (nextChar == '{') {
                stream.reset();
                node = new BlockScopeNode(this);
                buffer = new StringBuilder();
                break;
            }
            else if (nextChar == ';') {
                stream.reset();
                node = new KeyValueNode(this);
                buffer = new StringBuilder();
                break;
            }
        }

        this.consumeWhitespace();
        return node;
    }


    /**
     * Reads to the end of the stream and returns the result as a String
     *
     * @return
     * @throws Exception
     */
    public String readToEnd() throws IOException {
        StringBuilder builder = new StringBuilder();

        while (true) {
            Character nextChar = this.readChar();

            if (nextChar == null) {
               break;
            }
            else {
                builder.append(nextChar);
            }
        }

        return builder.toString();
    }

    public boolean atEnd() throws IOException {
        this.stream.mark(1);

        if (this.stream.read() == -1) {
            return true;
        }
        else {
            this.stream.reset();
            return false;
        }

    }

    public String peek(int n) throws IOException {
        this.stream.mark(n);
        StringBuilder builder = new StringBuilder();
        Integer count = new Integer(n);

        while (count > 0) {
            count--;

            Character newChar = this.readChar();

            if (newChar == null) {
                break;
            }
            else {
                builder.append(newChar);
            }
        }

        this.stream.reset();
        return builder.toString();
    }

    public List<AbstractNode> getNodes() throws IOException, InvalidSyntaxException{
        List<AbstractNode> nodes = new ArrayList<>();
        while(true) {
            AbstractNode node = this.readNode();
            if (node != null) {
                nodes.add(node);
            }

            this.consumeWhitespace();

            if (this.atEnd()) {
                break;
            }

            if (this.peek(1).equals("}")) {
                this.readChar();
                break;
            }
        }

        return nodes;
    }

    public String slurpToChar(String stopString) throws IOException, InvalidSyntaxException {
        StringBuilder slurpedText = new StringBuilder();

        while(!this.peek(stopString.length()).equals(stopString)) {
            Character character = this.readChar();

            if (character == null) {
                throw new InvalidSyntaxException(String.format(
                        "Unexpected end of input.  Expected %s, but got %s",
                        stopString,
                        slurpedText.toString()
                ));
            }
            else {
                slurpedText.append(character);
            }
        }

        this.stream.skip(stopString.length());

        return slurpedText.toString();
    }

    /**
     * Consumes up to and including the stop character, but only returns the characters
     * before the stop character.
     *
     * @param stopChar
     * @return
     * @throws IOException
     * @throws InvalidSyntaxException
     */
    public String slurpToChar(char stopChar) throws IOException, InvalidSyntaxException {
        return this.slurpToChar((new Character(stopChar)).toString());
    }

    /**
     * Advances the cursor to the next non-whitespace character;
     *
     * @throws IOException
     */
    public void consumeWhitespace() throws IOException {
        while (true) {
            stream.mark(1);

            Character character = readChar();
            if (character == null) {
                return;
            }

            if (!StringUtils.isWhitespace(character.toString())) {
                stream.reset();
                break;
            }
        }
    }
}
