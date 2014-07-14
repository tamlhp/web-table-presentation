package ch.epfl.modularity.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamUtils {
	public static BufferedReader file2reader(File file) throws IOException{
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte []buffer = new byte[1024];
		int length = bis.read(buffer);
		while(length > 0){
			baos.write(buffer);
			length = bis.read(buffer);
		}
		
		buffer = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		BufferedReader reader = new BufferedReader(new InputStreamReader(bais));
		
		return reader;
	}
}
