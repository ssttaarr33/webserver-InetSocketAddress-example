/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import util.Utils;

/**
 *
 * @author adrian
 */
public class SwitchSingleLight implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                body.append(line);
            }
            String responseString = body.toString();            
            responseString = Utils.parseQuery(Utils.parseFormData(responseString)).toJSONString();  
            Headers h = he.getResponseHeaders();
            h.set("Content-Type", "application/json");
            he.sendResponseHeaders(200, responseString.length());
            OutputStream os = he.getResponseBody();
            os.write(responseString.getBytes());
            os.close();
        }

    }
