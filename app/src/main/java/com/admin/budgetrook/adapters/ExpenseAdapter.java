package com.admin.budgetrook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.admin.budgetrook.R;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.holders.ListItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseAdapter extends BaseAdapter {
    private Context context;
    private List<ExpenseEntity> expenses;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Map<String, String> categoriesMap = new HashMap<String, String>();

    public ExpenseAdapter(Context context, List<ExpenseEntity> expenses, Map<String, String> categoriesMap) {
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
        ListItemViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expense_list_item, null, false);
            holder = new ListItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ListItemViewHolder) convertView.getTag();
        }
        Log.d("BUDGETROOK", "currentItem: " + currentItem.toString());
        Log.d("BUDGETROOK", "isSynchronized: " + currentItem.isSynchronized());
        if (currentItem.isSynchronized()) {
            holder.getSyncProgressBar().setVisibility(View.GONE);
            holder.getReadyLayout().setVisibility(View.VISIBLE);
            holder.getAmount().setText(String.format("%.2f", currentItem.getAmount()));
            holder.getDate().setText(dateFormat.format(currentItem.getDate()));
            holder.getName().setText(currentItem.getName());
            holder.getCategory().setText(categoriesMap.get(Integer.toString(currentItem.getCategoryId())));
            if (currentItem.isReviewed()) {
                holder.getNotificationIcon().setVisibility(View.INVISIBLE);
            } else {
                holder.getNotificationIcon().setVisibility(View.VISIBLE);
            }
        } else {
            holder.getSyncProgressBar().setVisibility(View.VISIBLE);
            holder.getReadyLayout().setVisibility(View.GONE);
            holder.getSyncProgressBar().setProgress(50);
        }
        return convertView;
    }


}
