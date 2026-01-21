package com.sentinelx.engine;

import android.util.Log;
import java.util.HashSet;
import java.util.Set;

public class RiskEngine {
    private static final String TAG = "RiskEngine";
    private static RiskEngine instance;

    private int currentScore = 0;
    private long lastTriggerAppTime = 0;
    private String lastTriggerPackage = "";
    
    // Scoring constants
    public static final int MAX_SCORE = 100;
    public static final int THRESHOLD_WARNING = 75;
    
    public static final int PTS_TRANSITION = 40;
    public static final int PTS_PERSISTENCE = 20;
    public static final int PTS_PRESSURE = 20;
    public static final int PTS_PANIC = 20;

    // Trigger Apps (Communication)
    private final Set<String> triggerApps = new HashSet<>();
    // Target Apps (Financial)
    private final Set<String> targetApps = new HashSet<>();
    // Pressure Keywords
    private final Set<String> pressureKeywords = new HashSet<>();

    private RiskEngine() {
        initData();
    }

    public static synchronized RiskEngine getInstance() {
        if (instance == null) {
            instance = new RiskEngine();
        }
        return instance;
    }

    private void initData() {
        // Communication Apps
        triggerApps.add("com.whatsapp");
        triggerApps.add("org.telegram.messenger");
        triggerApps.add("com.skype.raider");
        triggerApps.add("com.google.android.apps.meetings"); // Google Meet

        // Financial Apps
        targetApps.add("com.google.android.apps.nbu.paisa.user"); // GPay
        targetApps.add("com.phonepe.app");
        targetApps.add("com.paytm.payments");
        targetApps.add("net.one97.paytm");
        targetApps.add("in.org.npci.upiapp"); // BHIM

        // Pressure Keywords
        pressureKeywords.add("arrest");
        pressureKeywords.add("cbi");
        pressureKeywords.add("police");
        pressureKeywords.add("narcotics");
        pressureKeywords.add("customs");
        pressureKeywords.add("high court");
        pressureKeywords.add("fir");
        pressureKeywords.add("warrant");
    }

    public void reportAppSwitch(String packageName) {
        long currentTime = System.currentTimeMillis();
        
        if (triggerApps.contains(packageName)) {
            lastTriggerAppTime = currentTime;
            lastTriggerPackage = packageName;
            Log.d(TAG, "Trigger app detected: " + packageName);
        } else if (targetApps.contains(packageName)) {
            if (lastTriggerAppTime > 0 && (currentTime - lastTriggerAppTime) < 8000) {
                addScore(PTS_TRANSITION, "Rapid transition from " + lastTriggerPackage);
            }
        }
    }

    public void reportKeywordsFound(String keywords) {
        addScore(PTS_PRESSURE, "Pressure keywords detected: " + keywords);
    }

    public void reportLongSessionWithFinance() {
        addScore(PTS_PERSISTENCE, "Long communication session while accessing finance");
    }

    public void reportPanicInteraction() {
        addScore(PTS_PANIC, "Panic interaction pattern detected");
    }

    private synchronized void addScore(int points, String reason) {
        currentScore = Math.min(MAX_SCORE, currentScore + points);
        Log.w(TAG, "RISK INCREASE [" + points + "]: " + reason + " | Total: " + currentScore);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void resetScore() {
        currentScore = 0;
        lastTriggerAppTime = 0;
    }
}
