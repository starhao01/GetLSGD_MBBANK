package com.viettelpost.core.caches;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class JourneysCache implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static Long cachedMaxTime = 1000L * 60;

    private Map<String, List> journeysData = new WeakHashMap<>();
    private Map<String, Long> journeysTime = new WeakHashMap<>();

    public synchronized void pushJourney(String bill, List listJourneys) {
        journeysData.put(bill, listJourneys);
        journeysTime.put(bill, System.currentTimeMillis());
    }

    public List getJourney(String bill) {
        List lsTmp = journeysData.get(bill);
        if (lsTmp != null) {
            Long lastSync = journeysTime.get(bill);
            if (lastSync != null && System.currentTimeMillis() - lastSync < cachedMaxTime) {
                return lsTmp;
            }
        }
        return null;
    }

    private static JourneysCache instance;

    public static JourneysCache getInstance() {
        if (instance == null) {
            instance = new JourneysCache();
        }
        return instance;
    }
}
