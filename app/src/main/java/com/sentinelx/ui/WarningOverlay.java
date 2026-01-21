package com.sentinelx.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.sentinelx.R;
import com.sentinelx.engine.RiskEngine;

public class WarningOverlay {
    private final Context context;
    private final WindowManager windowManager;
    private View overlayView;
    private boolean isShowing = false;
    private final Vibrator vibrator;

    public WarningOverlay(Context context) {
        this.context = context;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void show() {
        if (isShowing) return;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;

        overlayView = LayoutInflater.from(context).inflate(R.layout.layout_warning_overlay, null);
        
        final TextView timerText = overlayView.findViewById(R.id.timerText);
        final View dismissButton = overlayView.findViewById(R.id.btn_dismiss);
        dismissButton.setVisibility(View.GONE);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Resuming in " + (millisUntilFinished / 1000) + "s");
                startHeartbeatVibration();
            }

            public void onFinish() {
                timerText.setText("Safety Check Complete");
                dismissButton.setVisibility(View.VISIBLE);
            }
        }.start();

        dismissButton.setOnClickListener(v -> hide());

        windowManager.addView(overlayView, params);
        isShowing = true;
    }

    private void startHeartbeatVibration() {
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 100, 200, 100}; // Quick heartbeat double-pulse
            vibrator.vibrate(pattern, -1);
        }
    }

    public void hide() {
        if (isShowing && overlayView != null) {
            windowManager.removeView(overlayView);
            isShowing = false;
            RiskEngine.getInstance().resetScore();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }
}
