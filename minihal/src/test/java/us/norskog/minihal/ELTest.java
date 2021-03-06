package us.norskog.minihal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ELTest {
    Request request;
    Response response;
    Item item;
    ELBase base;

    @Before
    public void setUp() throws Exception {
        request = new Request();
        response = new Response();
        item = new Item("");
        base = new ELBase();
    }

    @After
    public void tearDown() throws Exception {
        base = null;
    }

    @Test
    public void requests() {
        base.setTypes(request, response, item);
        base.setVars(request, response, item);
        String abc = base.evalExpr("${abc}");
        Assert.assertEquals("null", abc);
        abc = base.evalExpr("${request.params.q}");
        Assert.assertEquals("monkeys", abc);
        abc = base.evalExpr("${request.params.rows}");
        Assert.assertEquals("10", abc);
    }

    @Test
    public void getItems() {
        base.setTypes(request, response, item);
        base.setVars(request, response, item);
        Collection<Object> items = base.getItems("${response.itemArray}");
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
        base.setTypes(request, response, item);
        base.setVars(request, response, item);
        Collection<Object> items = base.getItems("${response.itemArray}");
        Assert.assertNotNull(items);
        Iterator<Object> iter = items.iterator();
        base.setVars(request, response, iter.next());
        String a = base.evalExpr("${item.value}");
        Assert.assertEquals("A1", a);
        base.setVars(request, response, iter.next());
        a = base.evalExpr("${item.value}");
        Assert.assertEquals("A2", a);
        Assert.assertFalse(iter.hasNext());
        items = base.getItems("${response.itemList}");
        Assert.assertNotNull(items);
        iter = items.iterator();
        base.setVars(request, response, iter.next());
        String b = base.evalExpr("${item.value}");
        Assert.assertEquals("B1", b);
        base.setVars(request, response, iter.next());
        b = base.evalExpr("${item.value}");
        Assert.assertEquals("B2", b);
        Assert.assertFalse(iter.hasNext());

    }
}

class Request {
    public Map<String,String> getParams() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("q", "monkeys");
        params.put("rows", "10");
        return params;
    }
}

class Response {

    public String getQ() {return "monkeys";}
    public String getRows() {return "10";}
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