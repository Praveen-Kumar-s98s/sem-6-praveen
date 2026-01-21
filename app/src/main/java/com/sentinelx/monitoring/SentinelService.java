package com.sentinelx.monitoring;

import android.accessibilityservice.AccessibilityService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

import com.sentinelx.engine.RiskEngine;
import com.sentinelx.ui.WarningOverlay;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class SentinelService extends AccessibilityService {
    private static final String TAG = "SentinelService";
    private RiskEngine riskEngine;
    private WarningOverlay warningOverlay;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        riskEngine = RiskEngine.getInstance();
        warningOverlay = new WarningOverlay(this);
        Log.d(TAG, "SentinelService Connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
            riskEngine.reportAppSwitch(packageName);
            checkSessionPersistence(packageName);
        }

        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            scanForKeywords(getRootInActiveWindow());
        }

        // Check if overlay is needed
        if (riskEngine.getCurrentScore() >= RiskEngine.THRESHOLD_WARNING && !warningOverlay.isShowing()) {
            warningOverlay.show();
        }
    }

    private void scanForKeywords(AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getText() != null) {
            String text = node.getText().toString().toLowerCase();
            // Simple keyword check (can be optimized)
            if (text.contains("police") || text.contains("arrest") || text.contains("cbi") || text.contains("narcotics")) {
                riskEngine.reportKeywordsFound(text);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            scanForKeywords(node.getChild(i));
        }
    }

    private void checkSessionPersistence(String currentPackage) {
        // Logic to check how long the user was in a communication app using UsageStatsManager
        // This is a simplified check for implementation demonstration
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long now = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 60 * 15, now);
        
        if (stats != null) {
            for (UsageStats usageStats : stats) {
                // If a comm app was running for a long time recently and we are now in finance
                if (usageStats.getPackageName().equals("com.whatsapp") && usageStats.getTotalTimeInForeground() > 600000) { // 10 mins
                    // Note: This would need more precise logic for "simultaneous" check
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "SentinelService Interrupted");
    }
}
