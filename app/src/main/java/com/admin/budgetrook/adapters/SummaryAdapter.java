package com.admin.budgetrook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.admin.budgetrook.R;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.holders.SummaryListItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryAdapter extends BaseAdapter {
    private Context context;
    private List<ExpenseEntity> expenses;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    private Map<String, String> categoriesMap = new HashMap<String, String>();

    public SummaryAdapter(Context context, List<ExpenseEntity> expenses, Map<String, String> categoriesMap) {
        this.context = context;
        this.expenses = expenses;
        this.categoriesMap = categoriesMap;
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return expenses.get(position).getUid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenseEntity currentItem = expenses.get(position);
        SummaryListItemViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.summary_list_item, null, false);
            holder = new SummaryListItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SummaryListItemViewHolder) convertView.getTag();
        }
        if (currentItem.isSynchronized()) {
            holder.getAmount().setText(String.format("%.2f", currentItem.getAmount()));
            holder.getDate().setText(dateFormat.format(currentItem.getDate()));
            holder.getName().setText(currentItem.getName());
            holder.getCategory().setText(categoriesMap.get(Long.toString(currentItem.getCategoryId())));
        }
        return convertView;
    }
}
