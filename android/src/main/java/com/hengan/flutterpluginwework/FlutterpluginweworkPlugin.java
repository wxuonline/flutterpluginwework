package com.hengan.flutterpluginwework;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.wework.api.IWWAPI;
import com.tencent.wework.api.IWWAPIEventHandler;
import com.tencent.wework.api.WWAPIFactory;
import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWAuthMessage;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterpluginweworkPlugin */
public class FlutterpluginweworkPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private static IWWAPI api;
  private String appid;
  private String agentid;
  private String schema;

  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_wecome");
    channel.setMethodCallHandler(this);
//    context = flutterPluginBinding.getApplicationContext();
  }

//  @Override
//  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
//    if (call.method.equals("getPlatformVersion")) {
//      result.success("Android " + android.os.Build.VERSION.RELEASE);
//    } else {
//      result.notImplemented();
//    }
//  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull final Result result) {
    if (call.method.equals("getPlatformVersion")) {
//            Toast.makeText(context, appid, Toast.LENGTH_SHORT).show();
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("register")) {
      appid = call.argument("appid");
      schema = call.argument("schema");
      agentid = call.argument("agentid");
//            Toast.makeText(context, "register"+schema, Toast.LENGTH_SHORT).show();
      api = WWAPIFactory.createWWAPI(context);
      result.success(api.registerApp(schema));
    } else if (call.method.equals("isWecomeInstalled")) {
//            Toast.makeText(context, "Check", Toast.LENGTH_SHORT).show();
      // Check if wecome app installed
      if (api == null) {
        result.success(false);
      } else {
        result.success(api.isWWAppInstalled());
      }
    } else if (call.method.equals("getApiVersion")) {
      result.success(api.isWWAppSupportAPI());
    } else if (call.method.equals("openWecome")) {
//            Toast.makeText(context, "openwecome", Toast.LENGTH_SHORT).show();
      result.success(api.openWWApp());
    } else if (call.method.equals("login")) {
      final WWAuthMessage.Req req = new WWAuthMessage.Req();
      final String state = call.argument("state").toString();
      req.sch = schema;
      req.appId = appid;
      req.agentId = agentid;
      req.state = state;
//            Toast.makeText(context, schema+","+appid+","+agentid, Toast.LENGTH_SHORT).show();
      api.sendMessage(req, new IWWAPIEventHandler() {
        @Override
        public void handleResp(BaseMessage resp) {
          if (resp instanceof WWAuthMessage.Resp) {
            WWAuthMessage.Resp rsp = (WWAuthMessage.Resp) resp;
            if (rsp.errCode == WWAuthMessage.ERR_CANCEL) {
              result.success("登录取消");
              Toast.makeText(context, "登录取消", Toast.LENGTH_SHORT).show();
            }else if (rsp.errCode == WWAuthMessage.ERR_FAIL) {
              result.success("登录失败");
              Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
            } else if (rsp.errCode == WWAuthMessage.ERR_OK) {
              result.success(rsp.code);
              Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            }
          } else {
            result.success("登录失败");
          }
        }
      });
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    context = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
