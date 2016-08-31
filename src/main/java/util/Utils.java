/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import service.Service;
import static service.Service.lights;

/**
 *
 * @author adrian.stoicescu
 */
public class Utils {

    public static JSONObject parseQuery(String query) {
        JSONObject object = new JSONObject();
        if (query != null) {
            if (query.contains("&")) {
                while (query.contains("&")) {
                    String pair = query.substring(0, query.indexOf("&"));
                    object.put(pair.substring(0, pair.indexOf("=")), pair.substring(pair.indexOf("=") + 1));
                    query = query.substring(query.indexOf("&") + 1);
                }
                object.put(query.substring(0, query.indexOf("=")), query.substring(query.indexOf("=") + 1));
            } else {
                object.put(query.substring(0, query.indexOf("=")), query.substring(query.indexOf("=") + 1));
            }
        }
        return object;
    }

    public static String decode(String query) throws UnsupportedEncodingException {
        query = query.replaceAll("\\s", "");
        String decodedString = java.net.URLDecoder.decode(query, "UTF-8");
        decodedString = decodedString.substring(decodedString.indexOf("=") + 1);
        return decodedString;
    }

    public static HashMap<String, Boolean> parseLights(String decoded) {
        HashMap<String, Boolean> lights = new HashMap<>();
        try {
            JSONArray lightsList = (JSONArray) new JSONParser().parse(decoded);
            for (Object entry : lightsList) {
                JSONObject obj = (JSONObject) entry;
                if (obj.get("On").equals("on")) {
                    lights.put((String) obj.get("Light"), true);
                } else {
                    lights.put((String) obj.get("Light"), false);
                }
                System.out.println(obj.get("Light"));
                System.out.println(obj.get("On"));
            }
        } catch (ParseException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lights;
    }

    public static String parseFormData(String query) {
        query = query.replace("+", " ");
        if (query.substring(query.lastIndexOf("=")).equals("on")) {
            query = query.substring(0, query.lastIndexOf("=") + 1) + "off";
        } else {
            query = query.substring(0, query.lastIndexOf("=") + 1) + "on";
        }
        return query;
    }

    public static HashMap<String, Boolean> populateLights() {
        HashMap<String, Boolean> lights = new HashMap<>();
        lights.put("kitchen", true);
        lights.put("living room", true);
        lights.put("bedroom 1", false);
        lights.put("bedroom 2", false);
        lights.put("bathroom 1", false);
        lights.put("bathroom 2", true);
        lights.put("balcony 1", true);
        lights.put("balcony 2", true);
        return lights;
    }

    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf(".") + 1);
    }

    public static String getHeaderForExt(String ext) {
        switch (ext) {
            case "css":
                return "text/css";
            case "gif":
                return "image/gif";
            case "html":
                return "text/html";
            case "ico":
                return "image/gif";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "txt":
                return "text/plain";
            case "xml":
                return "text/xml";
            case "js":
                return "application/javascript";                
            default:
                return null;
        }
    }
}
