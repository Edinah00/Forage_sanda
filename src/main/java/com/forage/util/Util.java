package com.forage.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Util {
    private Util() {}

    /**
     * Correspondance TypeDevis.id → sigle du Statut à créer automatiquement.
     *   1 = Étude   → "DEC" (Devis Étude créé)
     *   2 = Forage  → "DFC" (Devis Forage créé)
     *
     * RÈGLE MÉTIER : on ne peut pas créer un Devis Forage si le Devis Étude
     * n'est pas encore en statut "DET" (Devis Étude terminé).
     * Cette vérification est faite dans DevisService.saveDevisWithStatus().
     */
    private static final Map<Integer, String> TYPE_TO_SIGLE;
    private static final Map<String, Integer> SIGLE_TO_ID;

    static {
        Map<Integer, String> t = new HashMap<>();
        t.put(1, "DEC");   // TypeDevis Étude   → Devis Étude créé
        t.put(2, "DFC");   // TypeDevis Forage  → Devis Forage créé
        TYPE_TO_SIGLE = Collections.unmodifiableMap(t);

        Map<String, Integer> s = new HashMap<>();
        s.put("DC",  1);
        s.put("DEC", 2);
        s.put("DET", 3);
        s.put("DER", 4);
        s.put("DFC", 5);
        s.put("DFT", 6);
        s.put("DFR", 7);
        SIGLE_TO_ID = s;
    }

    public static String getSigleForType(int typeId) {
        return TYPE_TO_SIGLE.get(typeId); // null si inconnu
    }

    public static Integer getStatutIdBySigle(String sigle) {
        return SIGLE_TO_ID.get(sigle);
    }

    /**
     * Vérifie que le Devis Forage peut être créé :
     * la demande doit avoir un statut "DET" dans son historique.
     */
    public static boolean peutCreerDevisForage(
            java.util.List<com.forage.model.StatutDemande> historique) {
        int idDET = SIGLE_TO_ID.getOrDefault("DET", -1);
        return historique.stream()
                .anyMatch(sd -> sd.getStatut().getId() == idDET);
    }
}