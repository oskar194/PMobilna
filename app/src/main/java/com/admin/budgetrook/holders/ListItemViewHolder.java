package com.admin.budgetrook.holders;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admin.budgetrook.R;

public class ListItemViewHolder {
    private View row;
    private TextView name = null;
    private TextView amount = null;
    private TextView date = null;
    private LinearLayout readyLayout = null;
    private ProgressBar syncProgressBar = null;
    private ImageView notificationIcon = null;

    public ListItemViewHolder(View row) {
        this.row = row;
    }

    public TextView getName() {
        if (name == null) {
            name = (TextView) row.findViewById(R.id.name_tv);
        }
        return name;
    }

    public TextView getAmount() {
        if (amount == null) {
            amount = (TextView) row.findViewById(R.id.amount_tv);
        }
        return amount;
    }

    public TextView getDate() {
        if (date == null) {
            date = (TextView) row.findViewById(R.id.date_tv);
        }
        return date;
    }

    public LinearLayout getReadyLayout() {
        if (readyLayout == null) {
            readyLayout = (LinearLayout) row.findViewById(R.id.ready_layout);
        }
        return readyLayout;
    }

    public ProgressBar getSyncProgressBar() {
        if (syncProgressBar == null) {
            syncProgressBar = (ProgressBar) row.findViewById(R.id.sync_progress);
        }
        return syncProgressBar;
    }

    public ImageView getNotificationIcon() {
        if (notificationIcon == null) {
            notificationIcon = (ImageView)row.findViewById(R.id.notification_iv);
        }
        return notificationIcon;
    }
}
