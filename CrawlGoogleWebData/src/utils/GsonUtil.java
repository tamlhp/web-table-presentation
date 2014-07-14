/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author Tuan Chau
 */
public class GsonUtil {

    static Gson gson = new Gson();

    public static <T> T fromGson(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }
    
    public static <T> T fromGson(File file, Class<T> cls){
        try{
            return gson.fromJson(new FileReader(file), cls);
        }
        catch(FileNotFoundException e){
            return null;
        }
    }
    
    public static String toJson(Object o){
        return gson.toJson(o);
    }
}
