package fi.videosambo.iptools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Lang {
	enum Language {
		EN,
		FI
	}

	private static GUI gui;
	private static String filePath;
	
	private Language lang;
	
	public Lang(GUI gui,Language lang) {
		this.gui = gui;
		this.lang = lang;
		filePath = "lang/"+lang.toString().toLowerCase()+".json";
	}
	
	public String getMessage(String path) {
		String msg = fromString(path).getAsString();
		if (msg == null) {
			gui.logConsole("CRIT ERROR: " + path + " not found");
			return path;
		} else {
			return msg;
		}
	}
	
	private static JsonElement fromString(String path) throws JsonSyntaxException {
        ClassLoader classLoader = gui.getClass().getClassLoader();
        File jsonFile = new File(classLoader.getResource(filePath).getPath().replace("%20", " "));
        String content = null;
		try {
			content = new String(Files.readAllBytes(jsonFile.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	    JsonObject obj = new GsonBuilder().create().fromJson(content, JsonObject.class);
	    String[] seg = path.split("\\.");
	    for (String element : seg) {
	        if (obj != null) {
	            JsonElement ele = obj.get(element);
	            if (!ele.isJsonObject()) 
	                return ele;
	            else
	                obj = ele.getAsJsonObject();
	        } else {
	            return null;
	        }
	    }
	    return obj;
	}
}
