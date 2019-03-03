package com.admin.budgetrook.holders;

import android.view.View;
import android.widget.TextView;

import com.admin.budgetrook.R;

public class SummaryListItemViewHolder {

    private View row;
    private TextView category = null;
    private TextView name = null;
    private TextView amount = null;
    private TextView date = null;

    public SummaryListItemViewHolder(View row) {
        this.row = row;
    }

    public TextView getCategory() {
        if (category == null) {
            category = row.findViewById(R.id.summaryCategoryTV);
        }
        return category;
    }

    public TextView getName() {
        if (name == null) {
            name = row.findViewById(R.id.summaryNameTV);
        }
        return name;
    }

    public TextView getAmount() {
        if (amount == null) {
            amount = row.findViewById(R.id.summaryAmountTV);
        }
        return amount;
    }

    public TextView getDate() {
        if (date == null) {
            date = row.findViewById(R.id.summaryDateTV);
        }
        return date;
    }
}
