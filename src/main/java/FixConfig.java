import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FixConfig {
    private static Map<String, Properties> fixSessions = new HashMap<>();
    private static Map<String, Properties> tradingSessions = new HashMap<>();

    /**
     *
     * @throws IOException
     */
    public static void load() throws IOException {
        try(BufferedReader fixIni = new BufferedReader(new FileReader("conf/fix.ini"))){
            String line = "";
            String lastLine = "";
            boolean nextBlock = false;
            while( (line = fixIni.readLine()) != null){
                if(line.startsWith("[") || nextBlock){
                    String key = "";
                    if(nextBlock){
                        key = lastLine.substring(1, lastLine.indexOf("]"));
                    }else{
                        key = line.substring(1, line.indexOf("]"));
                    }
                    Properties properties = getFixSessions().get(key);
                    if(properties == null){
                        properties = getTradingSessions().get(key);
                    }
                    if(!nextBlock){
                        line = fixIni.readLine();
                    }else{
                        nextBlock = false;
                    }
                    while(line !=null){
                        if(line.startsWith("[")){
                            nextBlock = true;
                            lastLine = line;
                            break;
                        }
                        if(StringUtils.isNotBlank(line)){
                            String[] tuple = line.split("=");
                            properties.setProperty(tuple[0].trim(), tuple[1].trim());
                        }
                        line = fixIni.readLine();
                    }
                }else if(line.startsWith("connection=")){
                    getFixSessions().put(line.substring(line.indexOf("[")+1, line.indexOf("]")).trim(), new Properties());
                }else if(line.startsWith("trading_session=")){
                    getTradingSessions().put(line.substring(line.indexOf("[")+1, line.indexOf("]")).trim(), new Properties());
                }
            }
        }
    }

    public static Map<String, Properties> getFixSessions() {
        return fixSessions;
    }

    public static void setFixSessions(Map<String, Properties> fixSessions) {
        FixConfig.fixSessions = fixSessions;
    }

    public static Map<String, Properties> getTradingSessions() {
        return tradingSessions;
    }

    public static void setTradingSessions(Map<String, Properties> tradingSessions) {
        FixConfig.tradingSessions = tradingSessions;
    }
}
