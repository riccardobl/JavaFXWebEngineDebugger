# JavaFXWebEngineDebugger

Small library that allows the debugging of javafx's WebView with Chrome devtools.

##Gradle
Repository `maven { url "https://jitpack.io" }`

Dependency `compile 'com.github.riccardobl:JavaFXWebEngineDebugger:-SNAPSHOT'`


##Usage:
~ Creation:

  `JavaFXWebEngineDebugger debugger = JavaFXWebEngineDebuggerFactory.create((WebEngine)webView.getEngine(),(int)port);`


~ Destruction:

  `JavaFXWebEngineDebuggerFactory.destroy((JavaFXWebEngineDebugger)debugger);`



---
See src/test/java/ for more details.

---

##License:

This library is released under the Unlicense (see LICENSE for more info).

Please note that the default Server implementation (ServerImpl.java) depends on an [external library](https://github.com/TooTallNate/Java-WebSocket) licensed under the MIT license (see build.gradle for more info)
