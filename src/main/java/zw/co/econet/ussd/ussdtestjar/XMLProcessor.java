package zw.co.econet.ussd.ussdtestjar;

import org.apache.xmlbeans.XmlException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zw.co.econet.intergration.messagingSchema.MessageRequest;
import zw.co.econet.intergration.messagingSchema.MessageRequestDocument;
import zw.co.econet.intergration.messagingSchema.MessageRequestDocument.Factory;
import zw.co.econet.intergration.messagingSchema.MessageResponse;
import zw.co.econet.intergration.messagingSchema.MessageResponseDocument;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static zw.co.econet.ussd.ussdtestjar.ApplicationConstants.*;

public class XMLProcessor {
    private Logger logger = LoggerFactory.getLogger(getClass());


    public Map<String, String> parseRequestXML(String requestXML) {
        try {
            Map requestMap = new HashMap();
            MessageRequest messageRequest = Factory.parse(requestXML).getMessageRequest();
            requestMap.put("USSD", messageRequest.getChannel());
            requestMap.put("message_body", messageRequest.getMessage());
            requestMap.put("session_state", messageRequest.getStage());
            requestMap.put("SessionId", messageRequest.getTransactionID());
            requestMap.put("source_number", messageRequest.getSourceNumber());
            requestMap.put("TransactionTime", messageRequest.getTransactionTime().toString());
            requestMap.put("destination_number", messageRequest.getDestinationNumber());
            return requestMap;
        } catch (XmlException e) {
            this.logger.error("Failed to parse the incoming xml message : {} . Error : {} ", requestXML, e.getMessage());
        }
        return null;
    }

    public MessageResponseDocument composeResponseXML(Map<String, String> responseMap) {
        MessageResponseDocument requestDocument = MessageResponseDocument.Factory.newInstance();
        MessageResponse messageResponse = requestDocument.addNewMessageResponse();
        messageResponse.setTransactionID((String) responseMap.get("SessionId"));
        messageResponse.setChannel("USSD");
        messageResponse.setMessage((String) responseMap.get("message_body"));
        messageResponse.setStage((String) responseMap.get("session_state"));
        messageResponse.setTransactionTime(new DateTime().toCalendar(Locale.ENGLISH));
        messageResponse.setDestinationNumber((String) responseMap.get("destination_number"));
        messageResponse.setSourceNumber((String) responseMap.get("source_number"));
        messageResponse.setTransactionType((String) responseMap.get("transaction_type"));
        messageResponse.setApplicationTransactionID((String) responseMap.get("SessionId"));
        return requestDocument;
    }

    public MessageRequestDocument composeReq(Map<String,String> responseMap)
    {
        MessageRequestDocument requestDocument = MessageRequestDocument.Factory.newInstance();
        MessageRequest messageResponse = requestDocument.addNewMessageRequest();
        messageResponse.setTransactionID((String) responseMap.get("SessionId"));
        messageResponse.setChannel("USSD");
        messageResponse.setMessage((String) responseMap.get("message_body"));
        messageResponse.setStage((String) responseMap.get("session_state"));
        messageResponse.setTransactionTime(new DateTime().toCalendar(Locale.ENGLISH));
        messageResponse.setDestinationNumber((String) responseMap.get("destination_number"));
        messageResponse.setSourceNumber((String) responseMap.get("source_number"));
        messageResponse.setTransactionID((String)responseMap.get("transactionID"));
        return requestDocument;
    }


    public Map<String, String> parseResponseXML(String requestXML) throws Exception {


            Map<String, String> responseMap = new HashMap<String, String>();
            try {
                MessageResponse messageResponse = MessageResponseDocument.Factory.parse(requestXML).getMessageResponse();
                responseMap.put(SOURCE_NUMBER, messageResponse.getSourceNumber());
                responseMap.put(DESTINATION_NUMBER, messageResponse.getDestinationNumber());
                responseMap.put(MESSAGE_CHANNEL, USSD_CHANNEL);
                responseMap.put(TRANSACTION_ID, messageResponse.getTransactionID());
                responseMap.put(MESSAGE_BODY, messageResponse.getMessage());
                responseMap.put(TRAFFIC_FLOW, MESSAGE_OUTGOING);
                ///responseMap.put(TRANSACTION_TIME, dateUtil.generateISO8601Date(messageResponse.getTransactionTime().getTime()));
                responseMap.put(SESSION_STATE, messageResponse.getStage());
                responseMap.put(APPLICATION_TRANSACTION_ID, messageResponse.getApplicationTransactionID());
                if (messageResponse.getTransactionType() == null) {
                    responseMap.put(TRANSACTION_TYPE, "");
                } else {
                    responseMap.put(TRANSACTION_TYPE, messageResponse.getTransactionType().toString());
                }
            } catch (XmlException ex) {
                responseMap = null;
                logger.info("Failed to parse XML message from ESME " + ex.getMessage());
                throw new Exception(ex);
            }
            return responseMap;

    }



}