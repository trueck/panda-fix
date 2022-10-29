package com.panda.fix.config;

import com.panda.fix.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FixConfig {

    private static final Logger logger = LoggerFactory.getLogger(FixConfig.class);

    private Map<String, Properties> sessionProperties = new HashMap<>();
    private Map<String, Properties> tradingSessionProperties = new HashMap<>();

    public FixConfig() {
        load("conf/fix.ini");
    }

    public FixConfig(String configFile) {
        load(configFile);
    }

    public void load(String configFile) {
        try(BufferedReader fixIni = new BufferedReader(new FileReader(configFile))){
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
                    Properties properties = getSessionProperties().get(key);
                    if(properties == null){
                        properties = getTradingSessionProperties().get(key);
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
                    getSessionProperties().put(line.substring(line.indexOf("[")+1, line.indexOf("]")).trim(), new Properties());
                }else if(line.startsWith("trading_session=")){
                    getTradingSessionProperties().put(line.substring(line.indexOf("[")+1, line.indexOf("]")).trim(), new Properties());
                }
            }
        }catch (IOException e){
            logger.error("Got error when loading the fix ini.", e);
            throw new ApplicationException(e);
        }
    }

    public Map<String, Properties> getSessionProperties() {
        return sessionProperties;
    }

    public void setSessionProperties(Map<String, Properties> sessionProperties) {
        this.sessionProperties = sessionProperties;
    }

    public Map<String, Properties> getTradingSessionProperties() {
        return tradingSessionProperties;
    }

    public void setTradingSessionProperties(Map<String, Properties> tradingSessionProperties) {
        this.tradingSessionProperties = tradingSessionProperties;
    }
}
