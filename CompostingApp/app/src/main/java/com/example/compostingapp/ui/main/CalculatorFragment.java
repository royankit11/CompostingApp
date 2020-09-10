package com.example.compostingapp.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.compostingapp.InfoPageActivity;
import com.example.compostingapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CalculatorFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    Button btnCalculate;
    Button btnCancel;
    EditText txtWeight;
    RadioGroup timePeriod;
    ImageView co2;
    TextView txtResult;

    public static CalculatorFragment newInstance(int index) {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calculator, container, false);

        btnCalculate = root.findViewById(R.id.btnCalculate);
        btnCancel = root.findViewById(R.id.btnCancel);
        txtWeight = root.findViewById(R.id.txtWeight);
        timePeriod = root.findViewById(R.id.radioGroup);
        co2 = root.findViewById(R.id.co2);
        txtResult = root.findViewById(R.id.txtResult);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtWeight.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Enter a weight value", Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                double weight = Double.parseDouble(txtWeight.getText().toString()) * 0.9;
                int selectedId = timePeriod.getCheckedRadioButtonId();
                int finalCarbonWeight;

                if(selectedId == R.id.radioButton) {
                    finalCarbonWeight = (int)(weight * 52);
                } else if(selectedId == R.id.radioButton2) {
                    finalCarbonWeight = (int)(weight * 12);
                } else if (selectedId == R.id.radioButton3) {
                    finalCarbonWeight = (int)(weight);
                } else {
                    Toast.makeText(getContext(), "Select a time period", Toast.LENGTH_SHORT).show();
                    return;
                }
                co2.setImageResource(R.drawable.co2);
                txtResult.setText(finalCarbonWeight + " pounds of " + getString(R.string.Co2) + " saved per year");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return root;
    }

}