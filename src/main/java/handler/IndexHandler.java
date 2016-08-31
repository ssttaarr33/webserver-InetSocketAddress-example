/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import util.Utils;

/**
 *
 * @author adrian
 */
public class IndexHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        String root = "var/www/";
        URI uri = he.getRequestURI();
        File file;
        if (uri.getPath().equals("/")) {
            file = new File(root + "index.html").getCanonicalFile();
        } else {
            file = new File(root + uri.getPath()).getCanonicalFile();
        }
        if (!file.isFile()) {
            // Object does not exist or is not a file: reject with 404 error.
            String response = "404 (Not Found)\n";
            he.sendResponseHeaders(404, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Object exists and is a file: accept with response code 200.                
            Headers h = he.getResponseHeaders();
            String contentType = Utils.getHeaderForExt(Utils.getExtension(file.getPath()));
            if (contentType != null) {
                h.set("Content-Type", contentType);
            }
            h.set("Connection", "Keep-Alive");
            he.sendResponseHeaders(200, 0);
            OutputStream os = he.getResponseBody();
            FileInputStream fs = new FileInputStream(file);
            final byte[] buffer = new byte[0x10000];
            int count = 0;
            while ((count = fs.read(buffer)) >= 0) {
                os.write(buffer, 0, count);
            }
            fs.close();
            os.close();
        }
    }
}
