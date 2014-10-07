package us.norskog.minihal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ELTest {
    Request request;
    Response response;
    Item item;
    ELBase base;

    @Before
    public void setUp() throws Exception {
        request = new Request();
        response = new Response();
        item = new Item();

        base = new ELBase();
    }

    @After
    public void tearDown() throws Exception {
        base = null;
    }

    @Test
    public void checkEL() {
        base.setTypes(request, response, item);
        base.setVars(request, response, item);
        String abc = base.eval("${request.getAbc()}");
        Assert.assertEquals("abc", abc);
    }
}

class Request {
    public String getAbc() { return "abc";}
}

class Response {

}

class Item {

}