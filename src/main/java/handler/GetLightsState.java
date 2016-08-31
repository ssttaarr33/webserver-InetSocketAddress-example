/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import object.Light;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import static service.Service.lights;
import util.Utils;

/**
 *
 * @author adrian
 */
public class GetLightsState implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        JSONObject obj = new JSONObject();
        JSONArray tasks = new JSONArray();
        lights = Utils.populateLights();
        for (Map.Entry<String, Boolean> entry : lights.entrySet()) {
            JSONObject object = new JSONObject();
            Light light = new Light(entry.getKey(), entry.getValue(), 1);
            object.put("Light", ((Light) light).getLocation());
            object.put("On", ((Light) light).getState());
            tasks.add(object);
        }
        obj.put("Lights", tasks);
        String response = obj.toJSONString();
        Headers h = he.getResponseHeaders();
        h.set("Content-Type", "application/json");
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
