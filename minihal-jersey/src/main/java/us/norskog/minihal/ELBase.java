package us.norskog.minihal;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implement EL actions for request, response and item.
 * First get EL interface right, then refine to getX bean stuff only.
 * Object is hard-created to eval one string.
 * User must create its own map of objects.
 *
 * TODO: does any of this really need optimizing?
 *
 */
public class ELBase {

    private ExpressionFactory factory;
    private SimpleContext context;
    private ValueExpression requestValue;
    private ValueExpression responseValue;
    private ValueExpression itemValue;

    public ELBase() {
 //        System.setProperty("javax.el.methodInvocations", "false");
         factory = new de.odysseus.el.ExpressionFactoryImpl();
    }

    public void setTypes(Class requestClass, Class responseClass, Class itemClass) {
        // cache request, response, item
        context = new SimpleContext(new SimpleResolver());
        requestValue = factory.createValueExpression(context, "#{request}", requestClass);
        responseValue = factory.createValueExpression(context, "#{response}", responseClass);
        if (itemClass != null)
             itemValue = factory.createValueExpression(context, "#{item}", itemClass);
    }

    // TODO: maybe split setting request/response and item to minimize.
    public Object setVars(Object request, Object response, Object item) {
        requestValue.setValue(context, request);
        responseValue.setValue(context, response);
        if (item != null)
            itemValue.setValue(context, item);
        return null;
    }

    private Object eval(String given) {
        ValueExpression expr = factory.createValueExpression(context, given, Object.class);
        Object raw = null;
        try {
            raw = expr.getValue(context);
            return raw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String evalExpr(String single) {
        Object raw = eval(single);
        if (raw == null)
            raw = "null";
        System.out.println("Class of " + single + ": " + raw.getClass());
        String cooked = raw.toString();
        System.out.println("Value of " + single + ": " + cooked);
        return cooked;
    }
        // get iterator of items
    public Collection<Object> getItems(String items) {
        Object raw = eval(items);
        System.out.println("Class of " + items + ": " + raw.getClass());
        if (raw.getClass().isArray()) {
            Object[] obs = (Object[]) raw;
            System.out.println("Items in array: " + obs.length);
            List<Object> list = new ArrayList<Object>();
            for(int i = 0; i < obs.length; i++) {
                list.add(i, obs[i]);
            }
            return list;
        }
        if (raw instanceof Collection) {
            return (Collection) raw;
        }

        return Collections.emptyList();
    }
}
