package us.norskog.minihal;

/**
 * Expression evaluator: return current value of constant or expandable expression
 * based on given data.
 */



class Expression {
    final private String expr;
    final private boolean doEL;
    final private ELBase base;

    public Expression(String expr, boolean doEL) {
        this.expr = expr;
        this.doEL = doEL;
        if (doEL)
            base = new ELBase();
        else
            base = null;
    }

    public void setTypes(Class responseClass, Class itemClass) {
        base.setTypes(responseClass);
        base.setItemType(itemClass);
    }

    public Object eval(Object response, Object item) {
        if (! doEL)
            return expr;
        base.setVars(response, item);
        Object value = base.evalExpr(expr);
        return value;
    }

    public String toString() {
        return expr;
    }
}
