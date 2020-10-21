
import 'dart:async';

import 'package:flutter/services.dart';

class Flutterpluginwework {
  static const MethodChannel _channel =
  const MethodChannel('flutter_wecome');
  
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  
  /// Register app to Wecome with [appid, schema, agentid]
  static Future<dynamic> register(String appid, String schema, String agentid) async {
    var result = await _channel.invokeMethod(
        'register',
        {
          'appid': appid,
          'schema': schema,
          'agentid': agentid,
        }
    );
    return result;
  }
  
  /// Check if Wecome app has was installed.
  static Future<dynamic> isWecomeInstalled() async {
    var result = await _channel.invokeMethod(
        'isWecomeInstalled'
    );
    return result;
  }
  
  /// Get Wecome sdk api version.
  static Future<dynamic> getApiVersion() async {
    var result = await _channel.invokeMethod(
        'getApiVersion'
    );
    return result;
  }
  
  /// open Wecome app.
  static Future<dynamic> openWecome() async {
    var result = await _channel.invokeMethod(
        'openWecome'
    );
    return result;
  }
  
  /// Sharing
  /// arguments object structure
  /// ```
  /// {
  ///   "kind": "text",
  ///   "to": "session",
  ///   "title": "the title of message",
  ///   "description": "short description of message.",
  ///   "coverUrl": "https://example.com/path/to/cover/image.jpg",
  ///   "resourceUrl": "https://example.com/path/to/resource.mp4"
  /// }
  /// ```
  ///
  /// when kind is `music` or `video`, the `resourceUrl` means the data url of the mucis or video
  // static Future<dynamic> share(Map<String, dynamic> arguments) async {
  //   arguments['kind'] = arguments['kind'] ?? 'text';
  //   arguments['to'] = arguments['to'] ?? 'session';
  //
  //   var result = await _channel.invokeMethod(
  //       'share',
  //       arguments
  //   );
  //
  //   return result;
  // }
  
  /// Login
  /// arguments object structure:
  /// ```
  /// {
  ///   "state": "customestate"
  /// }
  /// ```
  static Future<dynamic> login(Map<String, String> arguments) async {
    var result = await _channel.invokeMethod(
        'login',
        arguments
    );
    return result;
  }
  
}
