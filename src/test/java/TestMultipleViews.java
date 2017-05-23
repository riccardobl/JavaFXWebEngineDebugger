import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.webengine_debugger.JavaFXWebEngineDebugger;
import javafx.webengine_debugger.JavaFXWebEngineDebuggerFactory;

public class TestMultipleViews extends Application{
	public static void main(String[] args) {
		launch();		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root=new GridPane();		
		String urls[]=new String[]{
				"https://html5test.com/",
				"https://www.youtube.com/watch?v=XH0CSzdHwg0",
				"https://github.com/riccardobl"
		};		
		for(int i=0;i<3;i++){
			WebView wv=new WebView();
			wv.getEngine().load(urls[i]);
			JavaFXWebEngineDebugger debugger = JavaFXWebEngineDebuggerFactory.create(wv.getEngine(),0);
			System.out.println("["+i+"] chrome-devtools://devtools/bundled/inspector.html?ws=127.0.0.1:"+debugger.getPort());
			root.add(wv,i,0);
		}		
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}


}
