package utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Tuan Chau
 */
public class IOHelper {
    public static byte[]readFull(InputStream is) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int length = is.read(buffer);
        
        while(length >= 0){
            baos.write(buffer, 0, length);
            length = is.read(buffer);
        }
        
        return baos.toByteArray();
    }
}
