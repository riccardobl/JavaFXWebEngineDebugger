import java.util.Map;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.webengine_debugger.JavaFXWebEngineDebugger;
import javafx.webengine_debugger.JavaFXWebEngineDebuggerFactory;
import javafx.webengine_debugger.json.Json;
import javafx.webengine_debugger.translayer.ChromeTranslationLayer;

public class ChromeTranslationLayerTest  extends Application{
	public static void main(String[] args) {
		launch();
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create the WebView
		WebView wv=new WebView();
		wv.getEngine().loadContent("<script>var i=0;setInterval(function(){console.log('abc'+(i++));},2000);</script>");
		// --
//		wv.getEngine().load("https://html5test.com/");

		Scene scene=new Scene(wv);
		primaryStage.setScene(scene);
		primaryStage.show(); 

		// Create a debugger and bind it to port 9999. If the port is set to 0, a random free port will be used.
		JavaFXWebEngineDebugger debugger=JavaFXWebEngineDebuggerFactory.create(wv.getEngine(),9999,new ChromeTranslationLayer(new Json(){
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
		System.out.println("Open this url with chrome: chrome-devtools://devtools/bundled/inspector.html?ws=127.0.0.1:"+debugger.getPort());
				
		System.out.println("Press a key to stop the server.");
		scene.setOnKeyPressed((e) -> {
			// Stop the debugger. This is not mandatory, the debugger is stopped automatically when the GC deallocates the WebEngine 
			JavaFXWebEngineDebuggerFactory.destroy(debugger);
			System.out.println("Stopped!");
			scene.setOnKeyPressed(null);
		});

	}

}
