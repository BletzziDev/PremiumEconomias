package com.premiumsetups.premiumeconomias.tools;

import java.util.List;

public class Text {
    public static String colorTranslate(String string) {
        return string.replace("&","§");
    }
    public static List<String> colorTranslate(List<String> stringList) {
        for(int i=0;i< stringList.size();i++) {
            stringList.set(i,stringList.get(i).replace("&","§"));
        }
        return stringList;
    }
    public static List<String> listReplace(List<String> list, String from, String to) {
        for(int i=0;i< list.size();i++) {
            list.set(i,list.get(i).replace(from,to));
        }
        return list;
    }
}
