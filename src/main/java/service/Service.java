/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import handler.GetHandler;
import handler.GetLightsState;
import handler.HeaderHandler;
import handler.IndexHandler;
import handler.PostHandler;
import handler.TestHandler;
import handler.TurnLightsOff;
import handler.TurnLightsOn;
import handler.SwitchSingleLight;
import java.util.HashMap;
import java.util.concurrent.Executors;

/**
 *
 * @author adrian.stoicescu
 */
public class Service {

    public static final int port = 8000;
    private static final int nThreads = 10;

    public static HashMap<String, Boolean> lights = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/status", new TestHandler());
        server.createContext("/", new IndexHandler());
        server.createContext("/getHeaders", new HeaderHandler());
        server.createContext("/get", new GetHandler());
        server.createContext("/post", new PostHandler());
        server.createContext("/lightsState", new GetLightsState());
        server.createContext("/turnLightsOff", new TurnLightsOff());
        server.createContext("/turnLightsOn", new TurnLightsOn());
        server.createContext("/switchSingleLight", new SwitchSingleLight());
        server.setExecutor(Executors.newFixedThreadPool(nThreads));
        server.start();
    }
}
