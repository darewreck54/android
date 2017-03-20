package com.codepath.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.nytimessearch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FilterDialogueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterDialogueFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.etBeginDate)
    TextView etBeginDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashionStyle) CheckBox cbFashionStyle;
    @BindView(R.id.cbSports) CheckBox cbSports;

    public FilterDialogueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilterDialogueFragment.
     */
    public static FilterDialogueFragment newInstance() {
        FilterDialogueFragment fragment = new FilterDialogueFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_dialogue, container, false);
        getDialog().getWindow().setLayout(200,400);

        ButterKnife.bind(this, view);
        SharedPreferences settings = getActivity().getSharedPreferences("filterSetting",Context.MODE_PRIVATE);
        this.cbArts.setChecked(settings.getBoolean("Arts", false));
        this.cbFashionStyle.setChecked(settings.getBoolean("FashionStyle", false));
        this.cbSports.setChecked(settings.getBoolean("Sports", false));
        int index = (int)settings.getLong("SortBy", 0);
        this.spSortOrder.setSelection(index);

        String date = settings.getString("BeginDate", null);
        if(date == null){
            this.etBeginDate.setText(null);
        } else {
            this.etBeginDate.setText(date);
        }

        return view;
    }

    @OnClick(R.id.btSave)
    public void onSaveClick() {
        SharedPreferences filterSetting = getActivity().getSharedPreferences("filterSetting",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = filterSetting.edit();
        editor.putBoolean("Arts", this.cbArts.isChecked());
        editor.putBoolean("Sports", this.cbSports.isChecked());
        editor.putBoolean("FashionStyle", this.cbFashionStyle.isChecked());
        editor.putLong("SortBy", this.spSortOrder.getSelectedItemId());
        if(TextUtils.isEmpty(this.etBeginDate.getText())){
            editor.putString("BeginDate",null);
        }
        else {
            editor.putString("BeginDate",this.etBeginDate.getText().toString());
        }

        editor.commit();
        getDialog().dismiss();
    }

    @OnClick(R.id.ivDatePicker)
    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(FilterDialogueFragment.this, 300);
        newFragment.show(fm, "datePicker");


    }
    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(c.getTime());

        etBeginDate.setText(date);
    }
}
