
package com.xmexe.exe.crypter;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by IntelliJ IDEA.
 * Desc:加密解密管理类
 * 加密算法 : 将文件的数据流的每个字节与加密解密key对应字符做异或运算.
 * 解密算法 : 已经加密的文件再执行一次对文件的数据流的每个字节与加密解密key对应字符做异或运算.
 * this method can decrypt or encrypt a large file in 100 milliseconds,just have a try and see
 */
public class FileEnDecryptManager {

  private String key = "EXE"; // 加密解密key(Encrypt or decrypt key)
  Database db;
  private FileEnDecryptManager() {

  }
  private FileEnDecryptManager(Context context) {
    db = new Database(context);
  }
  private static FileEnDecryptManager instance = null;

//  public static FileEnDecryptManager getInstance() {
//    synchronized (FileEnDecryptManager.class) {
//      if (instance == null) {
//        instance = new FileEnDecryptManager();
//      }
//    }
//    return instance;
//  }

  public static FileEnDecryptManager getInstance(Context context) {
    synchronized (FileEnDecryptManager.class) {
      if (instance == null) {
        instance = new FileEnDecryptManager(context);
      }
    }
    return instance;
  }

  /**
   * 加密入口(encrypt intrance)
   *
   * @param fileUrl 文件绝对路径
   * @return
   */
  public boolean doEncrypt(String fileUrl) {
    try {
      //没有加密的情况
      if (!isEncrypted(fileUrl)) {
        if (encrypt(fileUrl)) {
          // 可在此处保存加密状态到数据库或文件(you can save state into db or file)
          db.addFile(new File(fileUrl));
          LogUtils.d("doEncrypt succeed");
          return true;
        } else {
          LogUtils.d("encrypt failed");
          return false;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private final int REVERSE_LENGTH = 28; // 加解密长度(Encryption length)

  /**
   * 加解密(Encrypt or deprypt method)
   *
   * @param strFile 源文件绝对路径
   * @return
   */
  private boolean encrypt(String strFile) {
    int len = REVERSE_LENGTH;
    try {
      File f = new File(strFile);
      if (f.exists()) {
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        long totalLen = raf.length();

        if (totalLen < REVERSE_LENGTH)
          len = (int) totalLen;

        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(
          FileChannel.MapMode.READ_WRITE, 0, REVERSE_LENGTH);
        byte tmp;
        for (int i = 0; i < len; ++i) {
          byte rawByte = buffer.get(i);
          if (i <= key.length() - 1) {
            tmp = (byte) (rawByte ^ key.charAt(i)); // 异或运算(XOR operation)
          } else {
            tmp = (byte) (rawByte ^ i);
          }
          buffer.put(i, tmp);
        }
        buffer.force();
        buffer.clear();
        channel.close();
        raf.close();
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   * 加解密(Encrypt or deprypt method)
   *
   * @param strFile 源文件绝对路径
   * @return
   */
  private boolean _decrypt(String strFile, String outFile) {
      //copy file
    if (copyFile(strFile, outFile)){
      return  encrypt(outFile);
    }else {
      return false;
    }
  }

  ///////////////////////复制文件//////////////////////////////
  /**
   * 复制单个文件
   * @param oldPath String 原文件路径 如：c:/fqf.txt
   * @param newPath String 复制后路径 如：f:/fqf.txt
   * @return boolean
   */
  public boolean copyFile(String oldPath, String newPath) {
    boolean isok = true;
    try {
      int bytesum = 0;
      int byteread = 0;
      File oldfile = new File(oldPath);
      if (oldfile.exists()) { //文件存在时
        InputStream inStream = new FileInputStream(oldPath); //读入原文件
        FileOutputStream fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[1024];
        int length;
        while ( (byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; //字节数 文件大小
          //System.out.println(bytesum);
          fs.write(buffer, 0, byteread);
        }
        fs.flush();
        fs.close();
        inStream.close();
      }
      else
      {
        isok = false;
      }
    }
    catch (Exception e) {
      // System.out.println("复制单个文件操作出错");
      // e.printStackTrace();
      isok = false;
    }
    return isok;

  }
  public void doDecrypt(String fileUrl, String outFile) {
        decrypt(fileUrl, outFile);
  }

  private void decrypt(String fileUrl, String outFile) {
    if (_decrypt(fileUrl, outFile)) {
      // 可在此处保存解密状态到数据库或文件(you can save state into db or file)
      LogUtils.d("decrypt succeed");
    }
  }

  /**
   * 解密入口(decrypt intrance)
   *
   * @param fileUrl 源文件绝对路径
   */
  public void doDecrypt(String fileUrl) {
    try {
      if (isEncrypted(fileUrl)) {
        decrypt(fileUrl);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void decrypt(String fileUrl) {
    if (encrypt(fileUrl)) {
      // 可在此处保存解密状态到数据库或文件(you can save state into db or file)
      LogUtils.d("decrypt succeed");
      db.deleteFile(fileUrl);
    }
  }

  /**
   * fileName 文件是否已经加密(Whether file has been decrypted)
   *
   * @param filePath
   * @return
   * @throws IOException
   */
  private boolean isEncrypted(String filePath) throws IOException {
    // 从数据库或者文件中取出此路径对应的状态(get state out from db or file)
    return db.isExist(filePath);
  }

  public void deleteRecode(String filePath){
    db.deleteFile(filePath);
  }
  public void removeAllRecode(){
    db.removeAll();
  }
}
