package javafx.webengine_debugger;
import java.util.function.Consumer;

/**
 * @author Riccardo Balbo
 */
public interface Server{
	public void sendData(String data);
	public void setOnData(Consumer<String> f);
	public void start();
	public void stop();
	public int getPort();	
}
