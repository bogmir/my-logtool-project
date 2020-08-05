package com.embevolter.logtool.impl.logsEPA;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.embevolter.logtool.Utils;
import com.embevolter.logtool.model.Datetime;
import com.embevolter.logtool.model.LogLine;
import com.embevolter.logtool.model.ServerRequest;

/**
*    Line processing class for the EPA (1995) style logs
*/
public class EPALogtoolForLine implements ILogtoolForLine{
   
    static final Logger logger = Logger.getLogger(EPALogtoolForLine.class.getName());
    
    
    /**
     * Constructor
    */
    public EPALogtoolForLine() {
    }

    public static enum LineProcesserRegexEnum {
        HOST("(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])"),
        IP_ADDRESS("(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"),
        DATETIME("\\[\\d{2}:\\d{2}:\\d{2}:\\d{2}\\]"),
        REQUEST_METHOD("\"[A-Z]+"),
        REQUEST_INVALID("\"\\s400"),
        URL_PATH("\"[A..Z]*\\s[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]*"),
        PROTOCOL("[A-Z]+/\\d+\\.\\d"),
        RESPONSE_STATUS("[\\d]+\\s[\\d-]+$"),
        RESPONSE_SIZE("\\s[\\d-]+$"),
        ASCII_CONTROL_CHARACTERS("\\p{Cc}"),
        POST_REQUEST("\"POST"),
        HEAD_REQUEST("\"HEAD"),
        GET_REQUEST("\"GET");

    
        private String regex;
     
        LineProcesserRegexEnum(String regex) {
            this.regex = regex;
        }
    
        /**
         * Override the toString() method to return the label instead of the declared name.
         */
        @Override
        public String toString() {
            return this.regex;
        }
    }
    
    /**
     * Read every log entries and process it
     * @return list of LogLine objects
     */
    public LogLine processLine(String lineText) {
        String line = Utils.removeASCIIControlCharactersFromString(lineText);

        return this.readLine(line);
    }


    /**
     * Read and process individual line
     * @return new LogLine object
     */
    public LogLine readLine(String lineText) {
        if (lineText == null) return null;

        String[] lineTokens = lineText.split(" ");
        int lineTokensLength = lineTokens.length;

        String host = "", responseCode, documentSize;
        Datetime datetime = null;
        ServerRequest serverRequest;

        //the array is split to capture each resource fromo the current line in the log
        try {
            //resource: host
            host = lineTokens[0];

            //resource: datetime {dd,hh,mm,ss}
            datetime = readDatetimeChunk(lineTokens[1]);

            //because length is variable for serverRequest resources, a dynamic array is captured
            String[] serverRequestTokens = Utils.getArraySubset(lineTokens, 2, lineTokensLength - 2);
                                
            //resource: serverRequest [requestMethod, urlPath, protocol/protocolVersion] is parsed
            serverRequest = readServerRequestChunk(serverRequestTokens);
            
            //resource: responseCode
            responseCode = lineTokens[lineTokensLength-2];

            //resource: documentSize
            documentSize = lineTokens[lineTokensLength-1];

        } catch (Exception e) {
            logger.warning(
                String.format(
                    "Error while reading line: %s [%s]", 
                     host, datetime!= null ? datetime.toString() : "[]")
            );

            return null;
        }

        return new LogLine(host, datetime, serverRequest, responseCode, documentSize);
    } 

    /**
     * DateTime sequence from Log line is parsed into its component elements
     * @return new Datetime object
     */
    private Datetime readDatetimeChunk(String datetimeSequence) {
        //"Datetime" sequence is stripped of leading and trailing brackets
        //and split into its discrete elements
        String datetimeElements[] = datetimeSequence
            .substring(1, datetimeSequence.length()-1)
            .split(":");

        return new Datetime(datetimeElements[0], datetimeElements[1], datetimeElements[2], datetimeElements[3]);
    }

    /**
     * The httpRequestedMethod component exists only if the chunk starts with a " followed by
     * capitalized sequence of characters, i.e.: "GET", "POST", "HEAD", etc. 
     * 
     * @return httpRequestMethod String
     */  
    private String getHttpRequestMethodFromChunk(String[] serverRequestSequence) {
        String firstSequence = serverRequestSequence[0];

        Pattern httpRequestMethodPattern = Pattern.compile(LineProcesserRegexEnum.REQUEST_METHOD.toString());
        Matcher httpRequestMethodMatcher = httpRequestMethodPattern.matcher(firstSequence);
        boolean httpMethodMatcherFound = httpRequestMethodMatcher.find();

        /*
        * Remove first character (a quotation mark) of the first sequence of characters or chunk
        */
        firstSequence = firstSequence.substring(1);

        return (httpMethodMatcherFound) ? firstSequence: "";
    }

    /*
    * Determining the existence of the third and last component in the chunk, 
    * the ending position of the URL sequence can be inferred

    * @return protocol String
    */
    private String getProtocolSequenceFromChunk(String[] serverRequestSequence) {
        String lastSequence = serverRequestSequence[serverRequestSequence.length-1];

        Pattern protocolPattern = Pattern.compile(LineProcesserRegexEnum.PROTOCOL.toString());
        Matcher protocolMatcher = protocolPattern.matcher(lastSequence);
        boolean protocolFound = protocolMatcher.find();

        /*
        * Remove last character (a quotation mark) of the last sequence of characters or chunk
        */
        lastSequence = lastSequence.substring(0, lastSequence.length()-1);

        return (protocolFound) ? lastSequence : "";
    }


    /**
     * ServerRequest sequence is a part of a log line that does not always come as expected
     * Some elements might be missing or the URL might contain space characters
     * @return new ServerRequest object
     */
    private ServerRequest readServerRequestChunk(String[] serverRequestSequence) {
        
        int sequenceArrayLength = serverRequestSequence.length;

        /*
        * ----------------  FIRST SEQUENCE ---------------
        */
        String httpRequestMethod = this.getHttpRequestMethodFromChunk(serverRequestSequence);

        /*
        * ----------------  LAST SEQUENCE ---------------
        */

        String protocolSequence = this.getProtocolSequenceFromChunk(serverRequestSequence);

        int protocolSequenceSeparatorIndex = (!Utils.isNullOrEmpty(protocolSequence)) 
            ? protocolSequence.indexOf("/") : 0;

        String protocol = (!Utils.isNullOrEmpty(protocolSequence)) 
            ? protocolSequence.substring(0, protocolSequenceSeparatorIndex) : "";
            
        String protocolVersion = (!Utils.isNullOrEmpty(protocolSequence)) 
            ? protocolSequence.substring(
                protocolSequenceSeparatorIndex+1, protocolSequence.length()) : "";


        /*
        * ----------------  MIDDLE SEQUENCE (URL SEQUENCE) ---------------
        * this middle sequence is determined by exclusion because it may contain
        * space characters and therefore, difficult to pin down 
        */

        /**
         * The initial position of the URL component (the next component sought in the chunk) 
         * is 0 if the httpRequestmethod was not found
         */      
        int urlSequenceStartPosition = (!Utils.isNullOrEmpty(httpRequestMethod)) ? 1 : 0;

        /**
         * The initial position of the URL component (the next component sought in the chunk) 
         * is 0 if the httpRequestmethod was not found
         */
        int urlSequenceEndPosition = (!Utils.isNullOrEmpty(protocolSequence)) 
            ?  sequenceArrayLength-1 : sequenceArrayLength;   

        String urlSequence = 
            Utils.getArraySubsetToString(serverRequestSequence, urlSequenceStartPosition, urlSequenceEndPosition);
        
        //TODO: chapuza 
        if (Utils.isNullOrEmpty(protocolSequence)) {
            urlSequence = urlSequence.substring(1);
        }
        if (Utils.isNullOrEmpty(protocolSequence)) {
            urlSequence = urlSequence.substring(0, urlSequence.length() - 1);
        }

        return new ServerRequest(httpRequestMethod, urlSequence, protocol, protocolVersion);
    }

}