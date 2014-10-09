package us.norskog.minihal;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lance on 10/8/14.
 */
public class ParserTest {

    @Test
    public void parseSimple() {
        Parser p = new Parser("/abc");
        p.setTypes(Request.class, Response.class);
        Request req = new Request();
        Response resp = new Response();
        String simple = p.evaluate(req, resp, null);
        Assert.assertEquals("/abc", simple);
    }


    class Request {
        public Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("q", "monkeys");
            params.put("rows", "10");
            return params;
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

}