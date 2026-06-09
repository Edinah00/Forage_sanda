package com.forage.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class Util {
    private Util() {}

    
    private static final Map<Integer, String> TYPE_TO_SIGLE;
    
    private static final Map<String, Integer> SIGLE_TO_ID;

    static {
        Map<Integer, String> typeToSigle = new HashMap<>();
        
        typeToSigle.put(1, "DEC");
        typeToSigle.put(2, "DFC");
        TYPE_TO_SIGLE = Collections.unmodifiableMap(typeToSigle);

        Map<String, Integer> sigleToId = new HashMap<>();
        
        sigleToId.put("DFC", 4);
        sigleToId.put("DEC", 2); 
        SIGLE_TO_ID = sigleToId;
    }

    public static String getSigleForType(int typeId) {
        return TYPE_TO_SIGLE.getOrDefault(typeId, "ETU");
    }

    public static Integer getStatutIdBySigle(String sigle) {
        return SIGLE_TO_ID.get(sigle);
    }

    public static void registerSigleId(String sigle, int statutId) {
        SIGLE_TO_ID.put(sigle, statutId);
    }
}
