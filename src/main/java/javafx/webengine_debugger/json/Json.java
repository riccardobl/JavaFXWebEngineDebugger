package javafx.webengine_debugger.json;

import java.util.Map;

public interface Json{
	public Map<Object,Object> parse(String json);
	public String stringify(Map<Object,Object> map);
	
}
