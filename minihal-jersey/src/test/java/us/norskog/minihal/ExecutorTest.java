package us.norskog.minihal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * All features of base Executor
 * including variable expansion
 * @author lance
 *
 */

public class ExecutorTest {
	Map<String,Object> map;
	Response response;
	Item item;
	Executor base;

	@Before
	public void setUp() throws Exception {
		map = new HashMap<String,Object>();
		map.put("q", "monkeys");
		map.put("rows", 10);
		Item[] itemArray = new Item[2];
		itemArray[0] = new Item("A1");
		itemArray[1] = new Item("A2");
		map.put("itemArray", itemArray);
		map.put("itemList", new ArrayList<Item>());
		((List<Item>)map.get("itemList")).add(new Item("B1"));
		((List<Item>)map.get("itemList")).add(new Item("B2"));
		
		response = new Response();
		item = new Item("");
		base = new Executor();
		base.setVars(response, item);
	}

	@After
	public void tearDown() throws Exception {
		base = null;
		
	}

	@Test
	public void response() {
		String abc = base.evalExpr("${abc}");
		Assert.assertNull( abc);
		abc = base.evalExpr("${response.q}");
		Assert.assertEquals("monkeys", abc);
		abc = base.evalExpr("${response.rows}");
		Assert.assertEquals("10", abc);
		abc = base.evalExpr("${response.rows * 2}");
		Assert.assertEquals("20", abc);
	}

	@Test
	public void map() {
		Executor mapbase = new Executor();
		mapbase.setVars(map, null);

		String abc = mapbase.evalExpr("${abc}");
		Assert.assertNull( abc);
		abc = mapbase.evalExpr("${response.q}");
		Assert.assertEquals("monkeys", abc);
		abc = mapbase.evalExpr("${response.rows}");
		Assert.assertEquals("10", abc);
		abc = mapbase.evalExpr("${response.rows * 2}");
		Assert.assertEquals("20", abc);
		getItems();
	}

	@Test
	public void getItems() {
		List<Object> items = base.getItems("${response.itemArray}");
		Assert.assertNotNull(items);
		Iterator<Object> iter = items.iterator();
		Assert.assertEquals("A1", ((Item) iter.next()).getValue().toString());
		Assert.assertEquals("A2", ((Item) iter.next()).getValue().toString());
		Assert.assertFalse(iter.hasNext());
		items = base.getItems("${response.itemList}");
		Assert.assertNotNull(items);
		iter = items.iterator();
		Assert.assertEquals("B1", ((Item) iter.next()).getValue().toString());
		Assert.assertEquals("B2", ((Item) iter.next()).getValue().toString());
		Assert.assertFalse(iter.hasNext());
	}

	@Test
	public void fetchItems() {
		List<Object> items = base.getItems("${response.itemArray}");
		Assert.assertNotNull(items);
		Iterator<Object> iter = items.iterator();
		base.setVars(response, iter.next());
		String a = base.evalExpr("${item.value}");
		Assert.assertEquals("A1", a);
		base.setVars(response, iter.next());
		a = base.evalExpr("${item.value}");
		Assert.assertEquals("A2", a);
		Assert.assertFalse(iter.hasNext());
		items = base.getItems("${response.itemList}");
		Assert.assertNotNull(items);
		iter = items.iterator();
		base.setVars(response, iter.next());
		String b = base.evalExpr("${item.value}");
		Assert.assertEquals("B1", b);
		base.setVars(response, iter.next());
		b = base.evalExpr("${item.value}");
		Assert.assertEquals("B2", b);
		Assert.assertFalse(iter.hasNext());

	}

	@Test
	public void fetchEmbedded() {
		List<Object> itemList = base.getItems("${response.itemArray}");
		List<String> items = base.expandItems(itemList, "${item.value}");
		Assert.assertNotNull(items);
		Iterator<String> iter = items.iterator();
		Assert.assertEquals("A1", iter.next());
		Assert.assertEquals("A2", iter.next());
		itemList = base.getItems("${response.itemList}");
		items = base.expandItems(itemList, "${item.value}");
		Assert.assertNotNull(items);
		iter = items.iterator();
		Assert.assertEquals("B1", iter.next());
		Assert.assertEquals("B2", iter.next());
	}
}

class Response {

	public String getQ() {
		return "monkeys";
	}

	public String getRows() {
		return "10";
	}

	public Item[] getItemArray() {
		Item[] items = {new Item("A1"), new Item("A2")};
		return items;
	}

	public List<Item> getItemList() {
		List<Item> list = new ArrayList<Item>();
		list.add(new Item("B1"));
		list.add(new Item("B2"));
		return list;
	}
}

class Item {
	private final String value;

	public Item(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

