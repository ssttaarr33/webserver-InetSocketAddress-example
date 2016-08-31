/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import util.Utils;

/**
 *
 * @author adrian
 */
public class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        String response = Utils.parseQuery(query).toJSONString();
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
