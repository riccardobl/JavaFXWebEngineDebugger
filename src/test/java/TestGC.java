import javafx.application.Application;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.webengine_debugger.JavaFXWebEngineDebuggerFactory;
import javafx.webengine_debugger.ServerImpl;

public class TestGC extends Application{
	public TestGC(){
		for(int i=0;i<10;i++){
			WebView wv=new WebView();
			JavaFXWebEngineDebuggerFactory.create(ServerImpl.class,wv.getEngine(),8988+i);
			System.out.println("Create instance: "+i+" alive instances: "+JavaFXWebEngineDebuggerFactory.countInstances());
		}
		while(true){
			try{
				System.out.println("Call GC");
				System.gc();
				System.out.println("Alive instances: "+JavaFXWebEngineDebuggerFactory.countInstances());
				Thread.sleep(4000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		launch();		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {	
	}
}
