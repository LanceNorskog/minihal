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
 * Implement EL actions for response and item.
 * First get EL interface right, then refine to getX bean stuff only.
 * Object is hard-created to eval one string.
 * User must create its own map of objects.
 *
 * TODO: does any of this really need optimizing?
 *
 */
public class Executor {

	private ExpressionFactory factory;
	private SimpleContext context;
	private ValueExpression responseValue;
	private ValueExpression itemValue;

	public Executor() {
		//        System.setProperty("javax.el.methodInvocations", "false");
		factory = new de.odysseus.el.ExpressionFactoryImpl();
	}

	void setTypes(Object response, Object item) {
		if (responseValue == null) {
			// cache response evaluator
			context = new SimpleContext(new SimpleResolver());
			responseValue = factory.createValueExpression(context, "#{response}", response.getClass());
		}
		if (item != null && itemValue == null) {
			itemValue = factory.createValueExpression(context, "#{item}", item.getClass());
		}
	}

	public Object setVars(Object response, Object item) {
		setTypes(response, item);
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

	public List<Object> getItems(String items) {
		Object raw = eval(items);
		System.out.println("Class of " + items + ": " + raw.getClass());
		if (raw instanceof List)
			return (List<Object>) raw;
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
			List<Object> list = new ArrayList<Object>((Collection) raw);
			return list;
		}

		return Collections.emptyList();
	}

	public List<String> expandItems(List<Object> itemList, String itemExpr) {
		List<String> embedded = new ArrayList<String>();
		if (itemList.size() == 0)
			return embedded;
		for(Object item: itemList) {
			itemValue.setValue(context, item);
			String embed = evalExpr(itemExpr);
			embedded.add(embed);
		}
		return embedded;
	}
}
