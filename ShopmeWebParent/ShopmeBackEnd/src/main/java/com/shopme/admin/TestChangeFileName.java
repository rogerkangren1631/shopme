package com.shopme.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

//這是將老師的附件中的空格去掉

public class TestChangeFileName {

	public static void main(String args[]) throws IOException {
		File file = new File("c:\\Users\\roger\\test\\product-images"); // 電腦里已經存在了這個文件夾，不用新建了
		String path = file.getPath(); 
		System.out.println("Path  is " + path);
	
	//	changeFileName(path);
		
		
	}

	public static void changeFileName(String path) {
		File file = new File(path);

		if (file.exists()) {
			File[] files = file.listFiles();

			if (null == files || files.length == 0) {
				System.out.println("文件夹是空的!");

				return;

			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						changeFileName(file2.getAbsolutePath());

					} else {
											
						System.out.println("Old File full name is: " + file2.getAbsolutePath() );

						String filePath = file2.getAbsolutePath();
						 
                        String oldFileName = filePath.substring( filePath.lastIndexOf("\\" ) + 1);      
                        
                        String newFileName = oldFileName.replaceAll(" ",  "-");
                        
						String fileName = filePath.substring(0, filePath.lastIndexOf("\\") +1) + newFileName;

						File oriFile = new File(filePath);
						boolean b = oriFile.renameTo(new File(fileName));
						System.out.println("New File full name is: " + fileName  );

					}

				}

			}

		} else {
			System.out.println("该路径不存在");

		}

	}

}
