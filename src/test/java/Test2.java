import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.webengine_debugger.JavaFXWebEngineDebugger;
import javafx.webengine_debugger.JavaFXWebEngineDebuggerFactory;

public class Test2  extends Application{
	public static void main(String[] args) {
		launch();
		
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create the WebView
		WebView wv=new WebView();
		wv.getEngine().load("https://output.jsbin.com/kibiparuda");
		Pattern get_v=Pattern.compile("AppleWebKit/([^ ]+)");
		Matcher m=get_v.matcher(wv.getEngine().getUserAgent());
		if(m.find()){
			System.out.println("Engine: AppleWebKit/"+m.group(1));			
		}
		
		// --

		Scene scene=new Scene(wv);
		primaryStage.setScene(scene);
		primaryStage.show(); 

		// Create a debugger and bind it to port 9999. If the port is set to 0, a random free port will be used.
		JavaFXWebEngineDebugger debugger=JavaFXWebEngineDebuggerFactory.create(wv.getEngine(),9999);
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
