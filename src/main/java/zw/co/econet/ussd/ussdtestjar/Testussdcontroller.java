package zw.co.econet.ussd.ussdtestjar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zw.co.econet.intergration.messagingSchema.MessageRequestDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import static zw.co.econet.ussd.ussdtestjar.ApplicationConstants.*;

/**
 * Created by brighton on 4/16/15.
 */
public class Testussdcontroller {

    Logger logger = LoggerFactory.getLogger(getClass());

private TestRequestwrapper request= new TestRequestwrapper();


    public TestRequestwrapper getRequest() {
        return request;
    }

    public void setRequest(TestRequestwrapper request) {
        this.request = request;
    }

    public String startSession() throws Exception {
        int random = (int )(Math.random() * 10000 + 1);
        request.setSessionId(""+random);

        Map requestMap = new HashMap();

        requestMap.put("message_body", "#");
        requestMap.put("session_state", "FIRST");
        requestMap.put("SessionId", request.getSessionId());
        requestMap.put("source_number", request.getMobileNumber());
        requestMap.put("destination_number", request.getShortcode());
        requestMap.put("transactionID",request.getSessionId());
        XMLProcessor xmlProcessor = new XMLProcessor();
        MessageRequestDocument doc = xmlProcessor.composeReq(requestMap);
        RequestProcessor requestProcessor = new RequestProcessor();
        String responseString = "";
        Map responseMap = new HashMap();
            responseString=requestProcessor.process(doc.toString());
        responseMap = xmlProcessor.parseResponseXML(responseString);
        request.setMobileNumber((String)responseMap.get(SOURCE_NUMBER));
        request.setResponsemessage((String) responseMap.get(MESSAGE_BODY));
        request.setSessionId((String) responseMap.get(TRANSACTION_ID));
        request.setShortcode((String) responseMap.get(DESTINATION_NUMBER));
        request.setMessageState(TRANSACTION_TYPE);
        request.setRequestMessage("");




        return "home";
    }

    public String submitResponse() throws Exception {
        Map requestMap = new HashMap();

        requestMap.put("message_body", request.getRequestMessage());
        requestMap.put("session_state", request.getMessageState());
        requestMap.put("SessionId", request.getSessionId());
        requestMap.put("source_number", request.getMobileNumber());
        requestMap.put("destination_number", request.getShortcode());
        requestMap.put("transactionID",request.getSessionId());

        XMLProcessor xmlProcessor = new XMLProcessor();
        MessageRequestDocument doc = xmlProcessor.composeReq(requestMap);
        logger.info("***************\n"+doc.toString());
        String responseString = "";
        RequestProcessor requestProcessor = new RequestProcessor();
        Map responseMap = new HashMap();
            responseString=requestProcessor.process(doc.toString());
        responseMap = xmlProcessor.parseResponseXML(responseString);
        request.setMobileNumber((String)responseMap.get(SOURCE_NUMBER));
        request.setResponsemessage((String) responseMap.get(MESSAGE_BODY));
        request.setSessionId((String) responseMap.get(TRANSACTION_ID));
        request.setShortcode((String) responseMap.get(DESTINATION_NUMBER));
        request.setMessageState(TRANSACTION_TYPE);


        return "home";
    }

}
