package us.norskog.minihal;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

/**
 * Implement EL actions for request, response and item.
 * First get EL interface right, then refine to getX bean stuff only.
 * Object is hard-created to eval one string.
 * User must create its own map of objects.
 *
 */
public class ELBase {

    private ExpressionFactory factory;
    private SimpleContext context;
    private Object request;
    private Object response;
    private Object item;
    private ValueExpression requestValue;
    private ValueExpression responseValue;
    private ValueExpression itemValue;

    public ELBase() {
         factory = new de.odysseus.el.ExpressionFactoryImpl();
    }

    public void setTypes(Object request, Object response, Object item) {
        // cache request, response, item
        context = new SimpleContext(new SimpleResolver());
        requestValue = factory.createValueExpression(context, "#{request}", request.getClass());
        responseValue = factory.createValueExpression(context, "#{response}", response.getClass());
        itemValue = factory.createValueExpression(context, "#{item}", item.getClass());
    }

    public Object setVars(Object request, Object response, Object item) {
        requestValue.setValue(context, request);
        responseValue.setValue(context, response);
        itemValue.setValue(context, item);
        return null;
    }

    public String eval(String given) {
        ValueExpression expr = factory.createValueExpression(context, given, Object.class);
        Object raw = expr.getValue(context);
        System.out.println("Class of " + given + ": " + raw.getClass());
        String cooked = raw.toString();
        System.out.println("Value of " + given + ": " + cooked);
        return cooked;
    }


    // get iterator of items
}
