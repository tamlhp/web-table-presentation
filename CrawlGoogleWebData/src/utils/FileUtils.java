package utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tuan Chau
 */
public class FileUtils {

    public static List<String> getList(String filename, String splitText) {
        List<String> result = new ArrayList<>();
        try {
            byte[] data = IOHelper.readFull(new FileInputStream(filename));
            String str = new String(data);
            String[] arr = str.split(splitText);

            for (String s : arr) {
                result.add(s.trim());
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static List<String> getList(File file, String splitText) {
        List<String> result = new ArrayList<>();
        try {
            byte[] data = IOHelper.readFull(new FileInputStream(file));
            String str = new String(data);
            String[] arr = str.split(splitText);

            for (String s : arr) {
                result.add(s.trim());
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static byte[] read(String filename) throws IOException {
        return IOHelper.readFull(new FileInputStream(filename));
    }

    public static void write(String filename, String data) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            bos.write(data.getBytes("utf8"));
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String filename, byte[] data) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
            bos.write(data);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(String src, String des) {
        try {
            write(des, read(src));
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        write("D:/epfl/test.txt", "con heo con hêu con mèo con đi lang thang trong vườn hoa");
    }
}
