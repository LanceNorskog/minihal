package us.norskog.minihal;

import java.util.List;

/**
 * Parse and save parts of a hyperlink spec
 */
public class Parser {
    List<Object> parts;
    Class requestClass;
    Class responseClass;

    public Parser(String spec) {
        StringBuilder sb = new StringBuilder();
        int offset = outside(spec, sb, 0);
        parts.add(sb.toString());
        while (offset < spec.length()) {
            offset = inside(spec, sb, offset);
            parts.add(new Expression(sb.toString()));
        }

    }

    private int inside(String spec, StringBuilder sb, int offset) {

    }

    private int outside(String spec, StringBuilder sb, int offset) {
        return 0;
    }

    public void parse(String s) {
    }

    public void setTypes(Class requestClass, Class responseClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
    }

    public String evaluate(Object request, Object response, Object item) {

    }
}

