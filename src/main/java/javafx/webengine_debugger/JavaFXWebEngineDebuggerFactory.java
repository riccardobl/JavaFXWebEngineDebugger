package javafx.webengine_debugger;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.WeakHashMap;

import javafx.scene.web.WebEngine;

/**
 * @author Riccardo Balbo
 */
public final class JavaFXWebEngineDebuggerFactory{
	public static Class<? extends Server>  DEFAULT_SERVER=ServerImpl.class;

	private static final  WeakHashMap<WebEngine,DebuggerReference> _REFERENCES=new WeakHashMap<WebEngine,DebuggerReference> ();
	
	/**
	 * @description This class will take care of stopping the debugger when its webengine is deallocated.
	 */
	private static class DebuggerReference{ 
		JavaFXWebEngineDebugger get;
		public DebuggerReference(JavaFXWebEngineDebugger debugger){
			this.get=debugger;
		}
		@Override
		public void finalize(){
			get.stop();			
		}
	}
	
	private static int getFreePort(){
		int port=0;
		try{
			ServerSocket s=new ServerSocket(0);
			port=s.getLocalPort();
			s.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return port;
	}
	
	public static synchronized JavaFXWebEngineDebugger create(Class<? extends Server> server_class,WebEngine engine,int port){
		try{			
			if(port==0)port=getFreePort();
			JavaFXWebEngineDebugger  debugger = new JavaFXWebEngineDebugger(server_class,engine,port);
			_REFERENCES.put(engine,new DebuggerReference(debugger));
			debugger.start();
			return debugger;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static int countInstances(){
		return _REFERENCES.size();
	}
	
	
	
	
// #######	
	public static synchronized JavaFXWebEngineDebugger create(WebEngine engine,int port){
		return create(DEFAULT_SERVER,engine,port);
	}

	public static synchronized void destroy(JavaFXWebEngineDebugger debugger){
		debugger.stop();
	}
// #######	


}
