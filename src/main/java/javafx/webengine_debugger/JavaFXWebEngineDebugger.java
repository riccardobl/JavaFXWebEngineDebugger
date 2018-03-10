package javafx.webengine_debugger;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import com.sun.javafx.scene.web.Debugger;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import javafx.webengine_debugger.translayer.ChromeTranslationLayer;
import javafx.webengine_debugger.translayer.TranslationLayer;

/**
 * @author Riccardo Balbo
 */
public class JavaFXWebEngineDebugger{
	protected final WeakReference<WebEngine> _ENGINE;
	protected final Server _SERVER;
	protected boolean STOPPED=true;
	protected final TranslationLayer _TRANSLAYER;
	
//	protected JavaFXWebEngineDebugger(Class<? extends Server> server_class,WebEngine engine,int port) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
//		this(server_class,ChromeTranslationLayer.class,engine,port);
//	}
//	
	protected JavaFXWebEngineDebugger(Class<? extends Server> server_class,WebEngine engine,int port,TranslationLayer translayer) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		_TRANSLAYER=translayer;
		_ENGINE=new WeakReference<WebEngine>(engine);
		_SERVER=server_class.getDeclaredConstructor(String.class,Integer.class).newInstance("127.0.0.1",port);
		_SERVER.setOnData((data)->{
			Platform.runLater(()->{
				Debugger d=getDebugger();
				if(d!=null&&d.isEnabled()){
					String dx=data;
					if(_TRANSLAYER!=null)dx=_TRANSLAYER.in(dx);
//					System.out.println("<<< "+dx);
					d.sendMessage(dx);
				}else{
					System.err.println("Debugger not available");
				}
			});
		});
	}
	
	public int getPort(){
		return _SERVER.getPort();
	}
	
	protected void start(){
		if(STOPPED==false)return;
		STOPPED=false;
		Debugger d=getDebugger();
		if(d==null)return;
		d.setEnabled(true);
		d.sendMessage("{\"id\" : -1, \"method\" : \"Network.enable\"}");
		d.setMessageCallback((data) -> {
			String dx=data;
			if(_TRANSLAYER!=null)dx=_TRANSLAYER.out(dx);
//			System.out.println(">>> "+dx);
			_SERVER.sendData(dx);
			return null;
		});
		_SERVER.start();
	}
	
	protected void stop() {
		if(STOPPED)return;
		STOPPED=true;
		Debugger d=getDebugger();
		if(d!=null)d.setEnabled(false);
		_SERVER.stop();
	}
	
	@SuppressWarnings("deprecation")
	private Debugger getDebugger() {
		WebEngine e=_ENGINE.get();
		if(e==null)return null;
		return e.impl_getDebugger();
	}
}
