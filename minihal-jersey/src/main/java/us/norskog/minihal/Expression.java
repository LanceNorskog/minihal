package us.norskog.minihal;

/**
 * Expression evaluator: return current value
 */



class Expression {
    final private String expr;
    final private ELBase base;

    public Expression(String expr) {
        this.expr = expr;
        base = new ELBase();
    }

    public void setTypes(Class requestClass, Class responseClass, Class itemClass) {
        base.setTypes(requestClass, responseClass, itemClass);
    }

    public String eval(Object request, Object response, Object item) {
        base.setVars(request, response, item);
        String value = base.evalExpr(expr);
        return value;
    }
}
