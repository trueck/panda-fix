package com.panda.fix.util;

public class MessageUtil {

    public static String setValueOfTag(String message, String tag, String value){
        if("8".equals(tag)){
            return "8=" + value + " " + message.substring(message.indexOf(" ") + 1);
        }else{
            int start = message.indexOf(" " + tag + "=")+2+tag.length();
            int end = message.indexOf(' ', start);
            return message.substring(0, start) + value + message.substring(end);
        }
    }

    public static String setMessageLength(String message) {
        int tag9ValueStart = message.indexOf(" 9=")+3;
        int prefixLength = message.indexOf(' ', tag9ValueStart) + 1;
        int lengthExcludeTag10 = message.indexOf(" 10=");
        return setValueOfTag(message, "9", String.valueOf(message.length()-lengthExcludeTag10-prefixLength));
    }
}
