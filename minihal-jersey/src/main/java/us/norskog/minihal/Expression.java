package us.norskog.minihal;

/**
 * Expression evaluator: return current value
 */



class Expression {
    final private String expr;
    final private ELBase base;

    public Expression(String expr, Class requestClass, Class responseClass) {
        this.expr = expr;
        base = new ELBase();
        base.setTypes(requestClass, responseClass, null);
    }

    public String eval(Object request, Object response, Object item) {

    }
}
