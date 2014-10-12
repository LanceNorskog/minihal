package us.norskog.minihal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Created by lance on 10/9/14.
 */

@Provider
@Links
public class MinihalInterceptor implements WriterInterceptor {

    public MinihalInterceptor() {
        System.err.println("MinihalInterceptor created");
    }

 //   @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        // TODO Auto-generated method stub
        System.err.println("MinihalInterceptor called");
//        if (context.getMediaType().get)
        dumpContext(context);
        Object ob = context.getEntity();
        if (ob == null)
            System.err.println("\tEntity object is null!");
        else
            System.err.println("\tEntity type: " + ob.getClass().getCanonicalName().toString());
        OutputStream os = context.getOutputStream();

        byte[] buf = new byte[100];
        Arrays.fill(buf, (byte) 'b');
        os.write(buf);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        context.setOutputStream(bos);
        context.proceed();
    }

    void dumpContext(WriterInterceptorContext context) {
        System.out.println("getType: " + context.getType().getClass());
        System.out.println("getEntity: " + context.getEntity().getClass());
        System.out.println("getGenericType: " + context.getGenericType().getClass().toGenericString());
        Annotation[] annos = context.getAnnotations();
        for(Annotation anno: annos) {
        	System.out.println("\tanno: " + anno.toString());
        }
    }

}
