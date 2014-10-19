package us.norskog.minihal;

import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implement EL actions for response and item.
 * First get EL interface right, then refine to getX bean stuff only.
 * Object is hard-created to eval one string.
 * User must create its own map of objects.
 *
 * TODO: does any of this really need optimizing?
 *
 */
public class Executor {

	private final ExpressionFactory factory;
	private final SimpleContext context;
	Map<String,ValueExpression> valueExprs = new HashMap<String, ValueExpression>();
//	private ValueExpression responseValue;
//	private ValueExpression itemValue;

	public Executor() {
		//        System.setProperty("javax.el.methodInvocations", "false");
		factory = new de.odysseus.el.ExpressionFactoryImpl();
		context = new SimpleContext(new SimpleResolver());
	}

	ValueExpression setType(String name, Class valueClass) {
		if (! valueExprs.containsKey(name)) {
			// cache response evaluator
			valueExprs.put(name, factory.createValueExpression(context, "#{" + name + "}", valueClass));
		}
		return valueExprs.get(name);
	}

	public void setVar(String name, Object value) {
		ValueExpression valueExpr = setType(name, value.getClass());
		valueExpr.setValue(context, value);
	}

	private Object eval(String given) {
		ValueExpression expr = factory.createValueExpression(context, given, Object.class);
		Object raw = null;
		try {
			raw = expr.getValue(context);
			return raw;
		} catch (javax.el.PropertyNotFoundException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String evalExpr(String single) {
		Object raw = eval(single);
		if (raw == null)
			return null;
		System.out.println("Class of " + single + ": " + raw.getClass());
		String cooked = raw.toString();
		System.out.println("Value of " + single + ": " + cooked);
		return cooked;
	}

	public List<Object> getList(String selector) {
		Object raw = eval(selector);
		System.out.println("Class of " + selector + ": " + raw.getClass());
		if (raw instanceof List)
			return (List<Object>) raw;
		if (raw.getClass().isArray()) {
			Object[] obs = (Object[]) raw;
			List<Object> list = new ArrayList<Object>();
			for(int i = 0; i < obs.length; i++) {
				list.add(i, obs[i]);
			}
			return list;
		}
		if (raw instanceof Collection) {
			List<Object> list = new ArrayList<Object>((Collection) raw);
			return list;
		}

		return Collections.emptyList();
	}

	public List<String> expandList(String selector, List<Object> itemList, String expr) {
		List<String> embedded = new ArrayList<String>();
		if (itemList.size() == 0)
			return embedded;
		ValueExpression itemValueExpr = valueExprs.get(selector);
		for(Object item: itemList) {
			itemValueExpr.setValue(context, item);
			String embed = evalExpr(expr);
			embedded.add(embed);
		}
		return embedded;
	}
}
