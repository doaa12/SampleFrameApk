package com.wl.git.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
/**
 * 
 * @Description: ͼƬ���湤����
 * @author gengsong
 * @date 2013-10-16 ����4:12:43 
 * @version V1.0
 */
public class FileCache {

	private static FileCache fileCache; // ���������
	private String strImgPath; // ͼƬ�����·��
	private String strJsonPath;// Json�����·��

	private FileCache() {
		// this.context = context;
		String strPathHead = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			strPathHead = Environment.getExternalStorageDirectory().toString();
		} else{
			strPathHead = "/data/data/com.feihong.viewpager";
		}
		strImgPath = strPathHead + "/feihong/images/";
		strJsonPath = strPathHead + "/feihong/json/";
	}

	public static FileCache getInstance() {
		if (null == fileCache) {
			fileCache = new FileCache();
		}
		return fileCache;
	}

	public boolean saveData(String strApiUrl, String dataJson,String imgurl, Bitmap bmp) {
		String fileName = this.toHexString(strApiUrl);
		String imgName = imgurl.substring(
				imgurl.lastIndexOf('/') + 2,
				imgurl.length());
		File jsonFile = new File(strJsonPath);
		File imgFile = new File(strImgPath);
		if (!jsonFile.exists()) {
			jsonFile.mkdirs();
		}
		if (!imgFile.exists()) {
			imgFile.mkdirs();
		}
		File fTXT = new File(strJsonPath + fileName + ".txt");
		File fImg = new File(strImgPath + imgName);
		this.writeToFile(dataJson, fTXT);
		this.writeToFile(bmp, fImg);
		return true;
	}

	/**
	 * ����json����
	 * */
	public boolean savaJsonData(String strApiUrl, String dataJson) {
		String fileName = this.toHexString(strApiUrl);
		File jsonFile = new File(strJsonPath);
		if (!jsonFile.exists()) {
			jsonFile.mkdirs();
		}
		File fTXT = new File(strJsonPath + fileName + ".txt");
		if (fTXT.exists()) {
			fTXT.delete();
		}
		this.writeToFile(dataJson, fTXT);
		return true;
	}

	// ��ͼƬ��URL������ͼƬ��������ͼƬ
	public boolean savaBmpData(String imgurl, Bitmap bmp) {
		String imgName = imgurl.substring(
				imgurl.lastIndexOf('/') + 2,
				imgurl.length());
		File imgFileDirs = new File(strImgPath);
		if (!imgFileDirs.exists()) {
			imgFileDirs.mkdirs();
		}
		File fImg = new File(strImgPath + imgName);
		if (fImg.exists()) {
			fImg.delete();
		}
		this.writeToFile(bmp, fImg);
		return true;
	}

	// �Լ���ͼƬ����������ͼƬ
	public boolean saveBmpDataByName(String bmpName, Bitmap bmp) {
		File imgFileDirs = new File(strImgPath);
		if (!imgFileDirs.exists()) {
			imgFileDirs.mkdirs();
		}
		File fImg = new File(strImgPath + bmpName);
		if (fImg.exists()) {
			fImg.delete();
		}
		this.writeToFile(bmp, fImg);
		return true;
	}

	/**
	 * 
	 * ����fileName���� fileName��saveData()�����е�fileName����һ��ʱ�����ܶ����뱣��ʱһ�µ�����
	 * 
	 * */
	public String getJson(String strApiUrl) {
		String fileName = this.toHexString(strApiUrl);
		File file = new File(strJsonPath + fileName + ".txt");
		StringBuffer sb = new StringBuffer();
		if (file.exists()) {
			Reader reader = null;
			try {
				reader = new java.io.FileReader(file);
				BufferedReader br = new BufferedReader(reader);
				String str;
				while (null != (str = br.readLine())) {
					sb.append(str);
				}
				return sb.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ����ͼƬ��URL��ַ�������Bitmap
	 * */
	public Bitmap getBmp(String imgurl) {
		
		String imgName = imgurl.substring(
				imgurl.lastIndexOf('/') + 2,
				imgurl.length());

		File imgFile = new File(strImgPath + imgName);
		if (imgFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(imgFile);
				return BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			Log.v("����", "Ҫ�����ͼƬ�ļ�������");
		return null;
	}

	// ͨ��ͼƬ���������ͼƬ
	public Bitmap getBmpByName(String bmpName) {
		File imgFile = new File(strImgPath + bmpName);
		if (imgFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(imgFile);
				return BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.v("����", "Ҫ�����ͼƬ�ļ�������");
		}
		return null;
	}

	/**
	 * ����ͼƬ��URL�����ͼƬ�ļ�
	 * */
	public File getImgFile(String imgurl) {
		String imgName = imgurl.substring(
				imgurl.lastIndexOf('/') + 2,
				imgurl.length());
		File imgFile = new File(strImgPath + imgName);
		return imgFile;
	}

	private boolean writeToFile(String strData, File file) {
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(strData.getBytes());
			bos.flush();
			bos.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private boolean writeToFile(Bitmap bmp, File file) {
		if (file.exists()) {
			file.delete();
		}
		String name = file.getName();
		String geShi = name.substring(name.lastIndexOf('.'), name.length());

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			if (null != bmp) {
				if (".JPEG".equalsIgnoreCase(geShi)
						|| ".JPG".equalsIgnoreCase(geShi)) {
					bmp.compress(CompressFormat.JPEG, 100, bos);
					bos.flush();
					bos.close();
				} else if (".PNG".equalsIgnoreCase(geShi)) {
					bmp.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
				}
				return true;
			} else
				bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.v("����", "ͼƬд�뻺���ļ�����");
				}
			}
		}
		return false;
	}

	public boolean clearImgByImgUrl(String imgurl) {
		File imgFile = this.getImgFile(imgurl);
		if (imgFile.exists()) {
			imgFile.delete();
			return true;
		}
		return false;
	}

	/**
	 * ɾ��SD���ϵ�ȫ������
	 * */
	public int clearAllData() {
		File imgDir = new File(strImgPath);
		File txtDir = new File(strJsonPath);
		File[] imgFiles = imgDir.listFiles();
		File[] txtFiles = txtDir.listFiles();
		int m = imgFiles.length;
		int x = txtFiles.length;

		int g = 0;
		int t = 0;
		for (int i = 0; i < m; i++) {
			if (imgFiles[i].exists()) {
				if (imgFiles[i].delete())
					g++;
			} else
				g++;

		}
		for (int i = 0; i < x; i++) {
			if (txtFiles[i].exists()) {
				if (txtFiles[i].delete()) {
					t++;
				}
			} else
				t++;
		}
		if (g == m && t == x) {
			return 1;
		}
		return 0;
	}
	private String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return "0x" + str;// 0x��ʾʮ������
	}

	// ת��ʮ�����Ʊ���Ϊ�ַ���
	private String toStringHex(String s) {
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
}