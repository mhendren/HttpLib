package com.mhendren.HttpLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhendren on 4/7/2015.
 *
 * The HTTPRequest class takes the incoming request and parses it out into its components. This will not do
 * any evaluation beyond building up HTTPRequest components (At least at this time).
 */
public class HTTPRequest {
    String Method;
    String Uri;
    String Protocol;

    Map<String, String> Headers;

    String Body;

    private String[] getSections(String string) {
        return string.split("\\r\\n\\r\\n");
    }

    private String[] getLines(String lines) {
        return lines.split("\\r\\n");
    }

    private void ParseMethodLine(String string) {
        String[] segments = string.split(" ");
        this.setMethod(segments[0]);
        this.setUri(segments[1]);
        if (segments.length < 3) {
            this.setProtocol("HTTP/0.9");
        } else {
            this.setProtocol(segments[2]);
        }
    }

    private String[] splitHeader(String line) {
        int sep = line.indexOf(':');
        return new String[]{line.substring(0, sep), line.substring(sep + 1).replaceAll("^\\s+", "")};
    }

    private void ParseHeaders(String[] HeaderLines) {
        if (this.Protocol.equals("HTTP/0.9")) return;
        String lastHeader = null;
        String lastValue = null;
        Map<String, String> headers = new HashMap<String, String>();
        for(int i = 1; i < HeaderLines.length; i++) {
            String line = HeaderLines[i];
            if (line.charAt(0) != ' ') {
                if (lastHeader != null) {
                    headers.put(lastHeader, lastValue);
                    lastValue = null;
                }
                String[] splitAt = splitHeader(line);
                lastHeader = splitAt[0];
                if (splitAt.length > 1) {
                    lastValue = splitAt[1];
                }
            } else {
                lastValue += " " + line.replaceAll("^\\s+","");
            }
        }
        if (lastValue != null) {
            headers.put(lastHeader, lastValue);
        }
        this.setHeaders(headers);
    }

    public void Parse(String Blob) {
        String[] sections = getSections(Blob);
        String Headers = sections[0];
        String Body = null;
        if (sections.length > 1) {
            Body = sections[1];
        }
        String[] HeaderLines = getLines(Headers);
        ParseMethodLine(HeaderLines[0]);
        ParseHeaders(HeaderLines);
        this.setBody(Body);
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public String getProtocol() {
        return Protocol;
    }

    public void setProtocol(String protocol) {
        Protocol = protocol;
    }

    public Map<String, String> getHeaders() {
        return Headers;
    }

    public void setHeaders(Map<String, String> headers) {
        Headers = headers;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}