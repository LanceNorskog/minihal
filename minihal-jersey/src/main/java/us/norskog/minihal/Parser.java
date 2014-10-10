package us.norskog.minihal;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse and save parts of a hyperlink spec
 */
public class Parser {
    List<Expression> parts = new ArrayList<Expression>();
    Class requestClass;
    Class responseClass;
    private Class itemClass;

    public Parser(String spec) {
        StringBuilder sb = new StringBuilder();
      // build list of parts that are either Strings or Expressions
        // need inside/outside boolean to decide which.
        boolean el = false;
        for(int i = 0; i < spec.length(); i++) {
            char ch = spec.charAt(i);
            if (el) {
                if (ch == '}') {
                    if (sb.length() > 0) {
                        parts.add(new Expression(sb.toString(), true));
                        sb.setLength(0);
                    } else {
                        throw new IllegalArgumentException();
                    }
                    el = false;
                } else {
                    sb.append(ch);
                }
            } else {
                if (ch == '$') {
                    if (i + 1 == spec.length() || ! (spec.charAt(i + 1) == '{'))
                        throw new IllegalArgumentException();
                    i++;
                    if (sb.length() > 0) {
                        parts.add(new Expression(sb.toString(), false));
                        sb.setLength(0);
                    }
                    el = true;
                } else if (ch == '{' || ch == '}') {
                    throw new IllegalArgumentException();
                } else {
                        sb.append(ch);
                }
            }
        }
        if (el)
            throw new IllegalArgumentException();
        if (sb.length() > 0 || parts.size() == 0)
            parts.add(new Expression(sb.toString(), false));
    }

    public void setTypes(Class requestClass, Class responseClass, Class itemClass) {
        this.requestClass = requestClass;
        this.responseClass = responseClass;
        this.itemClass = itemClass;
        for(Expression part: parts) {
            part.setTypes(requestClass, responseClass, itemClass);
        }
    }

    public String evaluate(Object request, Object response, Object item) {
        StringBuilder sb = new StringBuilder();
        for(Expression part: parts) {
            sb.append(part.eval(request, response, item));
        }
        return sb.toString();
    }

    public List<Expression> getParts() {
        return parts;
    }
}

