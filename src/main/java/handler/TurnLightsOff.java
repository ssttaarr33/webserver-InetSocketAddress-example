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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import object.Light;
import object.State;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import service.Service;
import static service.Service.lights;
import util.Utils;

/**
 *
 * @author adrian
 */
public class TurnLightsOff implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        JSONObject response = new JSONObject();
        JSONArray tasks = new JSONArray();
        JSONArray results = new JSONArray();
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            body.append(line);
        }
        String query = body.toString();
        String decodedString = Utils.decode(query);
        lights = Utils.parseLights(decodedString);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        List<Future<State>> list = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : lights.entrySet()) {
            JSONObject obj = new JSONObject();
            Callable<State> light = new Light(entry.getKey(), entry.getValue(), 1);
            obj.put("Light found", ((Light) light).getLocation());
            obj.put("On", ((Light) light).getState());
            Future<State> future = executor.submit(light);
            list.add(future);
            tasks.add(obj);
        }
        JSONArray newStates = new JSONArray();
        for (Future<State> event : list) {
            try {
                JSONObject obj = new JSONObject();
                JSONObject state = new JSONObject();
                obj.put("task", new Date() + " - " + event.get().getMessage());
                state.put("Light", event.get().getField());
                state.put("On", event.get().getState());
                results.add(obj);
                newStates.add(state);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        executor.shutdown();
        response.put("Lights", tasks);
        response.put("results", results);
        response.put("newStates", newStates);
        String responseString = response.toJSONString();
        Headers h = he.getResponseHeaders();
        h.set("Content-Type", "application/json");
        he.sendResponseHeaders(200, responseString.length());
        OutputStream os = he.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }
}
