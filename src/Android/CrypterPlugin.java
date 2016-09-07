
package com.xmexe.exe.crypter;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 */
public class CrypterPlugin extends CordovaPlugin {

  private static final String TAG = "CrypterPlugin";

  private Context myContext;

  private JSONArray requestArgs;
  private CallbackContext context;
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.myContext = this.cordova.getActivity().getApplicationContext();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Log.i(TAG, "插件调用");
//    JSONObject options = new JSONObject();

    requestArgs = args;
    context = callbackContext;

    if (action.equals("encrypt")) {
      Log.v(TAG, "encrypt");
//      options = args.getJSONObject(0);
      Log.v(TAG, "input_file " + args.getString(0));
      String inputfile = args.getString(0);
      String inputPath = Uri.parse(inputfile).getPath();
      Log.v(TAG, "inputPath " + inputPath);
//      File file = new File (Uri.parse(inputfile).getPath());

      FileEnDecryptManager.getInstance(myContext).doEncrypt(inputPath);
//      java.io.File file  = new java.io.File("/storage/emulated/0/1.成功会议的六大关键环节.mp4");

//      FileEnDecryptManager.getInstance().doEncrypt(file.getAbsolutePath());

//      File output = this.myContext.getFileStreamPath("temp.data");
//      java.io.File output  = new java.io.File("/storage/emulated/0/temp.mp4");
//      Log.v(TAG, "output_file " + output.getAbsolutePath());
//      Log.v(TAG, "time start " + System.currentTimeMillis());
//      long time = System.currentTimeMillis();
//      FileEnDecryptManager.getInstance(myContext).doDecrypt(file.getAbsolutePath(), output.getAbsolutePath());
//      Log.v(TAG, "time end " + (System.currentTimeMillis() - time));
      JSONObject r = new JSONObject();
      r.put("inputFile", inputPath);
//      callbackContext.success(r);

    } else if (action.equals("decrypt")) {
      Log.v(TAG, "decrypt");
      String inputfile  = args.getString(0);
      String inputPath = Uri.parse(inputfile).getPath();
      Log.v(TAG, "inputPath " + inputPath);
      FileEnDecryptManager.getInstance(myContext).doDecrypt(inputPath);
      JSONObject r = new JSONObject();
      r.put("inputFile", inputPath);
//      callbackContext.success(r);
      return true;
    }else if (action.equals("delete")){
      String inputfile  = args.getString(0);
      String inputPath = Uri.parse(inputfile).getPath();
      Log.v(TAG, "inputPath " + inputPath);
      FileEnDecryptManager.getInstance(myContext).deleteRecode(inputPath);
      JSONObject r = new JSONObject();
      r.put("inputFile", inputPath);
      callbackContext.success();
      return true;
    }else if (action.equals("encrypt_doc")){
      String inputfile  = args.getString(0);
      String inputPath = Uri.parse(inputfile).getPath();
      String outputfile = args.getString(1);
      String outputPath = Uri.parse(outputfile).getPath();
      Log.v(TAG, "inputPath " + inputPath);
      FileEnDecryptManager.getInstance(myContext).doEncryptDocument(inputPath, outputPath);
      JSONObject r = new JSONObject();
      callbackContext.success(r);
      return true;
    }else if (action.equals("decrypt_doc")){
      String inputfile  = args.getString(0);
      String inputPath = Uri.parse(inputfile).getPath();
//      String outputfile = args.getString(1);
//      String outputPath = Uri.parse(outputfile).getPath();
      File dir = this.myContext.getExternalCacheDir();
      File output = new File(dir, "temp.data");
      String outputPath = output.getAbsolutePath();
      Log.v(TAG, "outputPath  " + outputPath);
      FileEnDecryptManager.getInstance(myContext).doDecryptDocument(inputPath,outputPath);

      JSONObject r = new JSONObject();
      r.put("outputPath", outputPath);
      callbackContext.success(r);
      return true;
    }

    return false;
  }


}
