package com.sentinelx.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.sentinelx.R;

public class MainActivity extends AppCompatActivity {

    private boolean isProtected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View shieldCard = findViewById(R.id.shieldCard);
        Button btnToggle = findViewById(R.id.btnToggle);
        TextView statusText = findViewById(R.id.statusText);

        // Pulsing Animation for Shield
        ObjectAnimator pulse = ObjectAnimator.ofPropertyValuesHolder(
                shieldCard,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f)
        );
        pulse.setDuration(1200);
        pulse.setRepeatCount(ObjectAnimator.INFINITE);
        pulse.setRepeatMode(ObjectAnimator.REVERSE);
        pulse.start();

        btnToggle.setOnClickListener(v -> {
            isProtected = !isProtected;
            if (isProtected) {
                btnToggle.setText("ENABLED");
                btnToggle.setBackgroundColor(0xFF00FF41);
                statusText.setText("SYSTEM ARMED");
                statusText.setTextColor(0xFF00FF41);
            } else {
                btnToggle.setText("DISABLED");
                btnToggle.setBackgroundColor(0xFFFF0000);
                statusText.setText("SYSTEM VULNERABLE");
                statusText.setTextColor(0xFFFF0000);
            }
        });

        // Redirect to Accessibility Settings if not enabled
        if (!isAccessibilityServiceEnabled()) {
             Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
             startActivity(intent);
        }
        
        // Request Overlay Permission
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
    }

    private boolean isAccessibilityServiceEnabled() {
        // Implementation check for accessibility service status
        return true; // Simplified for this demo
    }
}
