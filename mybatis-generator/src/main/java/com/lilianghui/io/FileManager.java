package com.lilianghui.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

import com.lilianghui.entity.Config;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;


public class FileManager {

	public static void createFile(Config config, String file, String content)
			throws Exception {
		String path = config.getFileSavePath() + File.separator + file;
		createDir(path);
		//
		OutputStream bos = new FileOutputStream(new File(path));
		bos.write(content.getBytes());
		bos.flush();
		bos.close();
	}

	/**
	 * Method createDir.
	 * 
	 * @param path
	 *            建立一个目录
	 */
	public static void createDir(String path) {
		try {

			File f = new File(path);
			if (!f.exists()) { // 如果已经存在f文件了
				f.getParentFile().mkdirs();
				// getParentFile()返回File类型的父路径，mkdirs()创建所有的路径
				try {
					f.createNewFile(); // 至此真正在硬盘上创建了myTest.txt文件。
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	public static void fileCopy(Config config) throws IOException {
		String path = System.getProperty("user.dir") + File.separator+ "template"+ File.separator+"template.zip";
		unZipFiles(path, config.getFileSavePath()+"/");
//		copyDirectiory(path, config.getFileSavePath());
	}

	public static void copyFile(File sourcefile, File targetFile)
			throws IOException {

		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourcefile);
		BufferedInputStream inbuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream out = new FileOutputStream(targetFile);
		BufferedOutputStream outbuff = new BufferedOutputStream(out);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len = 0;
		while ((len = inbuff.read(b)) != -1) {
			outbuff.write(b, 0, len);
		}

		// 刷新此缓冲的输出流
		outbuff.flush();

		// 关闭流
		inbuff.close();
		outbuff.close();
		out.close();
		input.close();

	}

	public static void copy(File[] fl, File file) {
		if (!file.exists()) // 如果文件夹不存在
			file.mkdir(); // 建立新的文件夹
		for (int i = 0; i < fl.length; i++) {
			if (fl[i].isFile()) { // 如果是文件类型就复制文件
				try {
					FileInputStream fis = new FileInputStream(fl[i]);
					FileOutputStream out = new FileOutputStream(new File(
							file.getPath() + File.separator + fl[i].getName()));

					int count = fis.available();
					byte[] data = new byte[count];
					if ((fis.read(data)) != -1) {
						out.write(data); // 复制文件内容
					}
					out.close(); // 关闭输出流
					fis.close(); // 关闭输入流
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fl[i].isDirectory()) { // 如果是文件夹类型
				File des = new File(file.getPath() + File.separator
						+ fl[i].getName());
				des.mkdir(); // 在目标文件夹中创建相同的文件夹
				copy(fl[i].listFiles(), des); // 递归调用方法本身
			}
		}
	}

	public static void copyDirectiory(String sourceDir, String targetDir) {
		File sourFile = null, desFile = null;
		sourFile = new File(sourceDir);
		if (!sourFile.isDirectory() || !sourFile.exists()) {
			throw new RuntimeException("源文件夹不存在");
		} else {
			desFile = new File(targetDir);
			copy(sourFile.listFiles(), desFile); // 调用copy()方法
		}
	}
	
	/**
	 * 解压到指定目录
	 * @param zipPath
	 * @param descDir
	 * @author isea533
	 */
	public static void unZipFiles(String zipPath,String descDir)throws IOException{
		unZipFiles(new File(zipPath), descDir);
	}
	/**
	 * 解压文件到指定目录
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile,String descDir)throws IOException{
		File pathFile = new File(descDir);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for(Enumeration entries = zip.getEntries();entries.hasMoreElements();){
			ZipEntry entry = (ZipEntry)entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream((ZipArchiveEntry) entry);
			String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
			//判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if(!file.exists()){
				file.mkdirs();
			}
			//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if(new File(outPath).isDirectory()){
				continue;
			}
			//输出文件路径信息
			System.out.println(outPath);
			
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while((len=in.read(buf1))>0){
				out.write(buf1,0,len);
			}
			in.close();
			out.close();
			}
		System.out.println("******************解压完毕********************");
	}

	public static void main(String[] args) throws IOException {
		unZipFiles("C:\\Users\\junze50\\Desktop\\Paiban1.0(12).zip", "D://zips");
	}
}
