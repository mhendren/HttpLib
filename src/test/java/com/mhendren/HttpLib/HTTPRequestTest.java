package com.mhendren.HttpLib;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class HTTPRequestTest {
    HTTPRequest stdRequest;

    @Before
    public void onSetUp() {
        stdRequest = new HTTPRequest();
        stdRequest.Parse("POST /FooBar HTTP/1.1\r\nHost: localhost:8080\r\nAccept-Encoding: gzip;deflate\r\nAccept: " +
                "text/plain\r\n text/xml\r\n application/json\r\nAccept-Language: en-US; en-GB\r\n" +
                "Content-Type: text/plain\r\nContent-Length: 5\r\n\r\nHello");
    }

    @Test
    public void testParseEmpty() throws Exception {
        HTTPRequest request = new HTTPRequest();
        request.Parse("GET /\r\n");
        assertThat(request.Method, is(equalTo("GET")));
        assertThat(request.Uri, is(equalTo("/")));
        assertThat(request.Protocol, is(equalTo("HTTP/0.9")));
        assertThat(request.Headers, is(nullValue()));
        assertThat(request.Body, is(nullValue()));
    }

    @Test
    public void testParseNoBody() throws Exception {
        HTTPRequest request = new HTTPRequest();
        request.Parse("GET /hello.html HTTP/1.1\r\nHost: localhost:8080\r\nAccept-Encoding: gzip;deflate\r\nAccept: " +
                "text/plain\r\n text/xml\r\n application/json\r\nAccept-Language: en-US; en-GB\r\n\r\n");
        assertThat(request.Method, is(equalTo("GET")));
        assertThat(request.Uri, is(equalTo("/hello.html")));
        assertThat(request.Protocol, is(equalTo("HTTP/1.1")));
        assertThat(request.Headers, is(notNullValue()));
        assertThat(request.Body, is(nullValue()));
        assertThat(request.Headers.size(), is(equalTo(4)));
        assertThat(request.Headers.get("Host"), is(equalTo("localhost:8080")));
    }

    @Test
    public void testParseWithBody() throws Exception {
        HTTPRequest request = new HTTPRequest();
        request.Parse("POST /FooBar HTTP/1.1\r\nHost: localhost:8080\r\nAccept-Encoding: gzip;deflate\r\nAccept: " +
                "text/plain\r\n text/xml\r\n application/json\r\nAccept-Language: en-US; en-GB\r\n" +
                "Content-Type: text/plain\r\nContent-Length: 5\r\n\r\nHello");
        assertThat(request.Method, is(equalTo("POST")));
        assertThat(request.Uri, is(equalTo("/FooBar")));
        assertThat(request.Protocol, is(equalTo("HTTP/1.1")));
        assertThat(request.Body, is(notNullValue()));
        assertThat(request.Body, is(equalTo("Hello")));
        assertThat(Integer.parseInt(request.Headers.get("Content-Length")), is(equalTo(5)));
        assertThat(request.Body.length(), is(equalTo(5)));
    }

    @Test
    public void testGetMethod() throws Exception {
        assertThat(stdRequest.getMethod(), is(equalTo("POST")));
    }

    @Test
    public void testSetMethod() throws Exception {
        stdRequest.setMethod("XMETH");
        assertThat(stdRequest.Method, is(equalTo("XMETH")));
    }

    @Test
    public void testGetUri() throws Exception {
        assertThat(stdRequest.getUri(), is(equalTo("/FooBar")));
    }

    @Test
    public void testSetUri() throws Exception {
        stdRequest.setUri("/ABCDEF");
        assertThat(stdRequest.Uri, is(equalTo("/ABCDEF")));
    }

    @Test
    public void testGetProtocol() throws Exception {
        assertThat(stdRequest.getProtocol(), is(equalTo("HTTP/1.1")));
    }

    @Test
    public void testSetProtocol() throws Exception {
        stdRequest.setProtocol("XPROT/1.0");
        assertThat(stdRequest.Protocol, is(equalTo("XPROT/1.0")));
    }

    @Test
    public void testGetHeaders() throws Exception {
        Map<String, String> headers = stdRequest.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.size(), is(equalTo(6)));
        assertThat(headers.get("Content-Type"), is(equalTo("text/plain")));
        assertThat(headers.get("Accept"), is(equalTo("text/plain text/xml application/json")));
        assertThat(headers.containsKey("Accept-Encoding"), is(true));
    }

    @Test
    public void testSetHeaders() throws Exception {
        Map<String, String> headers= new TreeMap<String, String>();
        headers.put("c", "C value");
        headers.put("b", "B value");
        headers.put("a", "A value");
        stdRequest.setHeaders(headers);
        assertThat(stdRequest.Headers.size(), is(equalTo(3)));
        assertThat(stdRequest.Headers.containsKey("a"), is(true));
        assertThat(stdRequest.Headers.containsKey("b"), is(true));
        assertThat(stdRequest.Headers.containsKey("c"), is(true));
    }

    @Test
    public void testGetBody() throws Exception {
        assertThat(stdRequest.getBody(), is(equalTo("Hello")));
    }

    @Test
    public void testSetBody() throws Exception {
        stdRequest.setBody("This is a test");
        assertThat(stdRequest.Body, is(equalTo("This is a test")));
    }
}