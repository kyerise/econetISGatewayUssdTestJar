package zw.co.econet.ussd.ussdtestjar;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by brighton on 4/17/15.
 */
public class RequestProcessor {

    public String process(String req) throws IOException {
        //create http client using apache http client library
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(NewJDialog.url);
        ObjectMapper mapper = new ObjectMapper();
        //declare entity for posting the json request, use object mapper to convert request wrapper to json string
        StringEntity input = new StringEntity(req);
        //set content type for the input string
      //  input.setContentType("application/json");

        //set entity to http post request
        post.setEntity(input);
        //execute call to http rest service and get response
        HttpResponse response = client.execute(post);
       InputStream r = response.getEntity().getContent();
        String res=IOUtils.toString(r);
        System.out.println(NewJDialog.url+"<>>><><><><><><><><><><><><><> "+res);
        client.close();
        return res;
    }

}
