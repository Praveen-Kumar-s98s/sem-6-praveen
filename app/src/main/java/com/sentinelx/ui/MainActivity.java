package com.sentinelx.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.sentinelx.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private View[] dots = new View[4];
    private boolean isProtectionActive = false;
    private Handler animationHandler = new Handler(Looper.getMainLooper());
    private Runnable breathingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide action bar for immersive experience
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setupViewPager();
        setupPageIndicators();
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.viewPager);
        OnboardingAdapter adapter = new OnboardingAdapter();
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePageIndicators(position);
                if (position == 1) {
                    // Status page - start breathing animation
                    startBreathingAnimation();
                    setupStatusRingClick();
                } else {
                    stopBreathingAnimation();
                }
                if (position == 2) {
                    setupPermissionClicks();
                }
            }
        });
    }

    private void setupPageIndicators() {
        dots[0] = findViewById(R.id.dot1);
        dots[1] = findViewById(R.id.dot2);
        dots[2] = findViewById(R.id.dot3);
        dots[3] = findViewById(R.id.dot4);
    }

    private void updatePageIndicators(int activePosition) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setBackgroundResource(
                    i == activePosition ? R.drawable.indicator_active : R.drawable.indicator_inactive);
        }
    }

    private void startBreathingAnimation() {
        View ringContainer = findViewById(R.id.ringContainer);
        if (ringContainer == null)
            return;

        breathingRunnable = new Runnable() {
            boolean expanding = true;

            @Override
            public void run() {
                float scale = expanding ? 1.05f : 1.0f;
                ringContainer.animate()
                        .scaleX(scale)
                        .scaleY(scale)
                        .setDuration(1500)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
                expanding = !expanding;
                animationHandler.postDelayed(this, 1500);
            }
        };
        animationHandler.post(breathingRunnable);
    }

    private void stopBreathingAnimation() {
        if (breathingRunnable != null) {
            animationHandler.removeCallbacks(breathingRunnable);
        }
    }

    private void setupStatusRingClick() {
        View ringContainer = findViewById(R.id.ringContainer);
        if (ringContainer == null)
            return;

        ringContainer.setOnClickListener(v -> {
            isProtectionActive = !isProtectionActive;
            animateStatusChange();
        });
    }

    private void animateStatusChange() {
        View ringContainer = findViewById(R.id.ringContainer);
        View mainRing = findViewById(R.id.mainRing);
        View outerGlow = findViewById(R.id.outerGlow);
        TextView statusValue = findViewById(R.id.statusValue);

        if (ringContainer == null || mainRing == null)
            return;

        // Contract animation
        ObjectAnimator contractX = ObjectAnimator.ofFloat(ringContainer, "scaleX", 1f, 0.9f);
        ObjectAnimator contractY = ObjectAnimator.ofFloat(ringContainer, "scaleY", 1f, 0.9f);

        // Expand animation
        ObjectAnimator expandX = ObjectAnimator.ofFloat(ringContainer, "scaleX", 0.9f, 1.05f, 1f);
        ObjectAnimator expandY = ObjectAnimator.ofFloat(ringContainer, "scaleY", 0.9f, 1.05f, 1f);

        // Glow animation
        ObjectAnimator glowAlpha = ObjectAnimator.ofFloat(outerGlow, "alpha",
                isProtectionActive ? 0.6f : 0.3f);

        AnimatorSet contractSet = new AnimatorSet();
        contractSet.playTogether(contractX, contractY);
        contractSet.setDuration(100);

        AnimatorSet expandSet = new AnimatorSet();
        expandSet.playTogether(expandX, expandY, glowAlpha);
        expandSet.setDuration(300);

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(contractSet, expandSet);
        fullAnimation.start();

        // Update colors and text
        int ringColor = isProtectionActive ? ContextCompat.getColor(this, R.color.cyber_cyan)
                : ContextCompat.getColor(this, R.color.ring_off);

        statusValue.setText(isProtectionActive ? "ACTIVE" : "OFF");
        statusValue.setTextColor(ringColor);

        // Change ring drawable tint (requires API 21+)
        mainRing.getBackground().setTint(ringColor);
    }

    private void setupPermissionClicks() {
        LinearLayout permAccessibility = findViewById(R.id.permAccessibility);
        LinearLayout permOverlay = findViewById(R.id.permOverlay);

        if (permAccessibility != null) {
            permAccessibility.setOnClickListener(v -> {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            });
        }

        if (permOverlay != null) {
            permOverlay.setOnClickListener(v -> {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBreathingAnimation();
    }
}
