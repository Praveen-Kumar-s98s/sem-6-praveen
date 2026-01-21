package com.sentinelx.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sentinelx.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.PageViewHolder> {

    private static final int[] PAGE_LAYOUTS = {
            R.layout.page_welcome,
            R.layout.page_status,
            R.layout.page_permissions,
            R.layout.page_privacy
    };

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(PAGE_LAYOUTS[viewType], parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        // Binding logic handled in MainActivity via page change callback
    }

    @Override
    public int getItemCount() {
        return PAGE_LAYOUTS.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        PageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
