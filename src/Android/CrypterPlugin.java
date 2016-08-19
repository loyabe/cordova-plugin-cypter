
package com.xmexe.exe.crypter;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
    JSONObject options = new JSONObject();

    requestArgs = args;
    context = callbackContext;

    if (action.equals("encrypt")) {
      Log.v(TAG, "encrypt");
//      options = args.getJSONObject(0);
      Log.v(TAG, "input_file " + args.getString(0));
      String inputfile = args.getString(0);
      FileEnDecryptManager.getInstance(myContext).doEncrypt(inputfile);
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
      r.put("inputFile", inputfile);
      callbackContext.success(r);

    } else if (action.equals("decrypt")) {
      Log.v(TAG, "decrypt");
      String file  = args.getString(0);

      FileEnDecryptManager.getInstance(myContext).doDecrypt(file);
      JSONObject r = new JSONObject();
      r.put("inputFile", file);
      callbackContext.success(r);
    }else if (action.equals("delete")){
      String file  = args.getString(0);

      FileEnDecryptManager.getInstance(myContext).deleteRecode(file);
      JSONObject r = new JSONObject();
      r.put("inputFile", file);
      callbackContext.success(r);
    }

    return false;
  }


}
