package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.admin.budgetrook.R;
import com.admin.budgetrook.adapters.SummaryAdapter;
import com.admin.budgetrook.dialogs.MultiSelectDialog;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;
import com.admin.budgetrook.wrappers.SummaryRequestWrapper;
import com.admin.budgetrook.wrappers.SummaryWrapper;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SummaryFragment extends Fragment {

    private ChartFragmentInterface mListener;

    public static final String TAG = "BUDGETROOK";

    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();
    private List<Date> dates = new ArrayList<>();

    private Set<String> years;
    private List<String> categoryNames;

    private boolean[] selectedCategories = null;
    private String selectedYear = null;
    private TextView total;

    private Button buttonOk;
    private Button buttonSelectCategories;
    private Spinner yearsSpinner;

    SummaryAdapter summaryAdapter;

    private ListView listView;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary, container, false);
        yearsSpinner = v.findViewById(R.id.summaryDateSpinner);
        total = v.findViewById(R.id.summaryTotal);
        total.setText("Total: -");
        mListener.getSetupDataForSummary();
        buttonSelectCategories = v.findViewById(R.id.summaryCategoryButton);
        buttonSelectCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + categoryNames);
                MultiSelectDialog dialog = MultiSelectDialog.newInstance(new ArrayList<String>(categoryNames), selectedCategories);
                dialog.show(getFragmentManager(), "multiPickerDialog");
            }
        });
        buttonOk = v.findViewById(R.id.summaryOkButton);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SummaryRequestWrapper wrapper = buildRequestWrapper();
                    mListener.getExpensesForSummary(wrapper);
                } catch (InvalidParameterException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
        listView = v.findViewById(R.id.summaryRecycler);
        return v;
    }

    private SummaryRequestWrapper buildRequestWrapper() throws InvalidParameterException {
        selectedYear = yearsSpinner.getSelectedItem().toString();
        if (years != null && categories != null && selectedYear != null && selectedCategories != null) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.valueOf(selectedYear));
            c.set(Calendar.DAY_OF_YEAR, 1);
            Date from = c.getTime();

            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DAY_OF_MONTH, 31);
            Date to = c.getTime();

            List<Long> categoryIds = new ArrayList<Long>();

            for (int i = 0; i < categories.size(); i++) {
                if (selectedCategories[i]) {
                    categoryIds.add(categories.get(i).getUid());
                }
            }
            return new SummaryRequestWrapper(from, to, categoryIds);
        } else {
            throw new InvalidParameterException();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof ChartFragmentInterface) {
            mListener = (ChartFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChartFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setup(SummaryWrapper wrapper) {
        summaryAdapter = new SummaryAdapter(getActivity(), wrapper.expenses, wrapper.categoriesMap);
        listView.setAdapter(new SummaryAdapter(getActivity(), wrapper.expenses, wrapper.categoriesMap));
        summaryAdapter.notifyDataSetChanged();
        total.setText("Total: " + String.format("%.2f", wrapper.sum));
    }

    public void setupConfig(SummaryWrapper wrapper) {
        years = new HashSet<String>();
        categoryNames = new ArrayList<String>();
        dates = wrapper.dates;
        if (dates != null) {
            for (Date date : dates) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                years.add(Integer.toString(c.get(Calendar.YEAR)));
            }
        }
        categories = wrapper.categories;
        if (categories != null) {
            for (CategoryEntity categoryEntity : categories) {
                categoryNames.add(categoryEntity.getName());
            }
            Log.d(TAG, "setupConfig: " + categoryNames.toString());
            selectedCategories = new boolean[categoryNames.size()];
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.custom_spinner_item, new ArrayList<String>(years));
        yearsSpinner.setAdapter(dataAdapter);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        yearsSpinner.setAdapter(dataAdapter);
    }

    public void okClickedInDialog(boolean[] selectedItems) {
        if (selectedItems != null) {
            this.selectedCategories = selectedItems;
        }
    }
}

