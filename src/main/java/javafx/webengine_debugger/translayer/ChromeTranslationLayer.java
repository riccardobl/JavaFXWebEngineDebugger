package javafx.webengine_debugger.translayer;

import java.util.Collection;
import java.util.Map;

import javafx.webengine_debugger.json.Json;

public class ChromeTranslationLayer implements TranslationLayer{

	protected Json JSON;
	protected Version VERSION;

	public static enum Version{
		TIP_OF_TREE
	}

	public ChromeTranslationLayer(Json json){
		this(json,Version.TIP_OF_TREE);
	}

	public ChromeTranslationLayer(Json json,Version version){
		JSON=json;
		VERSION=version;
	}

	@Override
	public String in(String s) {
		if(VERSION==Version.TIP_OF_TREE){
			Map<Object,Object> map=JSON.parse(s);
			switch((String)map.get("method")){
				case "Log.enable":{
					map.put("method","Console.enable");
					break;
				}
				case "Log.startViolationsReport":{
					map=null;
					break;
				}
			}
			if(map!=null) s=JSON.stringify(map);
		}
		return s;
	}

	protected String LAST_MSG;
	int i=0;
	@Override
	public String out(String s) {
		if(VERSION==Version.TIP_OF_TREE){
			Map<Object,Object> map=JSON.parse(s);
			String method=(String)map.get("method");
			
			boolean donot_save_msg=false;
			
			if(method!=null){
				switch(method){
					case "Console.messageRepeatCountUpdated":
						s=LAST_MSG;
						map=JSON.parse(s);
						method=(String)map.get("method");
						donot_save_msg=true;
					case "Console.messageAdded":{
//						s="{\"method\":\"Log.entryAdded\",\"params\":{\"entry\":{\"source\":\"javascript\",\"level\":\"verbose\",\"text\":\"BLOCKED: interaction.editing is on the blacklist for this browser!"+(i++)+"\",\"column\":30.0,\"url\":\"https://html5test.com/scripts/8/engine.js\",\"repeatCount\":1.0,\"lineNumber\":4262,\"timestamp\":"+System.currentTimeMillis()+"}}}";
//						LAST_MSG=s;
//						if(1==1)break;
						
						if(!donot_save_msg) LAST_MSG=s;

						map.put("method","Log.entryAdded");
						Map<Object,Object> params=(Map<Object,Object>)map.get("params");
						//						if(params!=null){
						Map<Object,Object> message=(Map<Object,Object>)params.remove("message");
						//							if(message!=null){
						Number line=((Number)message.remove("line"));
						if(line!=null){
							message.put("lineNumber",line.intValue());
						}

						String source=(String)message.get("source");
						//								if(source!=null){
						switch(source){
							case "css":{
								source="rendering";
								break;
							}
							case "other":
							case "console-api":{
								source="javascript";
								break;
							}
							case "content-blocker":{
								source="deprecation";
								break;							
							}
						}
						message.put("source",source);
						
						
						
						

						String level=(String)message.get("level");
						switch(level){
							case "debug":
							case "log":{
								level="verbose";
								break;
							}
						}
						message.put("level",level);
						message.put("timestamp",System.currentTimeMillis());
						//								}
						//							}
						
						
						Collection<Map> prs=(Collection<Map> )message.remove("parameters");
						if(prs!=null){
							String text="";
							for(Map pr:prs){
								String v=pr.get("value").toString();
								text+=" "+v;
							}
							message.put("text",text);
						}
						message.remove("stackTrace");
						message.remove("type");

						params.put("entry",message);
						//						}
						
						s=JSON.stringify(map);

						break;
					}
				}
				
			}
		}
		return s;
	}

}
