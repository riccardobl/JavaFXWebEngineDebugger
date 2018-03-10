# JavaFXWebEngineDebugger

Small library that allows the debugging of javafx's WebView with Chrome devtools.

## Gradle
#### Repository 
```
jcenter()
maven { url "https://jitpack.io" }
```

#### Dependency 
```
compile 'com.github.riccardobl:JavaFXWebEngineDebugger:-SNAPSHOT'
```

#### Additional dependency for the default server implementation

```
compile "org.java-websocket:Java-WebSocket:1.3.0"
```

## Usage:
#### Creation:

```java
JavaFXWebEngineDebugger debugger = JavaFXWebEngineDebuggerFactory.create((WebEngine)webView.getEngine(),(int)port);
```


#### Destruction:

```java
JavaFXWebEngineDebuggerFactory.destroy((JavaFXWebEngineDebugger)debugger);
```


## Chrome translation layer

The class `javafx.webengine_debugger.translayer.ChromeTranslationLayer` translates the [webkit debug protocol](https://github.com/WebKit/webkit/tree/master/Source/JavaScriptCore/inspector/protocol) used by the fx webengine to [chrome debug protocol](https://chromedevtools.github.io/devtools-protocol/) compatible with Google Chrome.

For now it's limited only to part of the Console api.

See implementation below.


##### Required dependency
```
	compile 'com.google.code.gson:gson:2.8.0'
```

##### Example

```java
JavaFXWebEngineDebugger debugger=JavaFXWebEngineDebuggerFactory.create((WebEngine)webView.getEngine(),(int)port,new ChromeTranslationLayer(new Json(){
    Gson gson=new Gson();
    
    @Override 
    public Map<Object,Object> parse(String json) {
        return gson.fromJson(json,Map.class);
    }
    
    @Override 
    public String stringify(Map<Object,Object> map) {
        return gson.toJson(map);
    }		
},ChromeTranslationLayer.Version.TIP_OF_TREE));
 ```

---
See src/test/java/ for more details.

---

## License:

This library is released under the Unlicense (see LICENSE for more info).

Please note that the default Server implementation (ServerImpl.java) depends on an [external library](https://github.com/TooTallNate/Java-WebSocket) licensed under the MIT license (see build.gradle for more info)
