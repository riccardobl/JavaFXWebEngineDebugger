package javafx.webengine_debugger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.function.Consumer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * @author Riccardo Balbo
 */
public class ServerImpl implements Server{
	protected Consumer<String> ON_DATA;
	protected final WebSocketServer _SERVER;
	
	public ServerImpl(String ip,Integer port) throws IOException{
		if(port==null){
			port=0;
		}
		_SERVER=new WebSocketServer(new InetSocketAddress(ip,port)){
			@Override
			public void onOpen(WebSocket conn, ClientHandshake handshake) {
			}

			@Override
			public void onClose(WebSocket conn, int code, String reason, boolean remote) {
			}

			@Override
			public void onMessage(WebSocket conn, String message) {
				if(ON_DATA!=null)ON_DATA.accept(message);				
			}

			@Override
			public void onError(WebSocket conn, Exception ex) {}			
		};
    }

	
	@Override
	public void start() {
		_SERVER.start();		
	}

	@Override
	public void stop() {
		try{
			_SERVER.stop();
		}catch(IOException e){
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}		
	}
	
	@Override
	public void sendData(String data) {
		Collection<WebSocket> con = _SERVER.connections();
		synchronized ( con ) {
			for(WebSocket c:con)c.send(data);
		}
	}

	@Override
	public int getPort() {
		return _SERVER.getPort();
	}

	@Override
	public void setOnData(Consumer<String> f) {
		ON_DATA=f;		
	}
}
