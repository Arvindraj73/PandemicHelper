package com.mcet.pandemichelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EssentialPassFragment extends Fragment {

    private TextInputLayout vehicleReqText;
    private LinearLayout vehicleLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_essential_pass, container, false);

        vehicleLayout = v.findViewById(R.id.vehicleInfo);

        vehicleReqText = v.findViewById(R.id.vehicle_count);
        vehicleReqText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog("Number of Vehicles Required");
            }
        });

        return v;

    }

    private void openNumberDialog(String s) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.number_picker_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        NumberPicker picker = view.findViewById(R.id.number_picker);
        if (s.equals("Number of Vehicles Required")) {
            picker.setMaxValue(10);
            picker.setMinValue(1);
        }
//        else if (s.equals("No of Passengers")) {
//            picker.setMaxValue(10);
//            picker.setMinValue(1);
//        }
        builder.setTitle("Select " + s)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (s.equals("Number of Vehicles Required")) {
                            Log.d("VehiclesRequired", String.valueOf(picker.getValue()));
                            vehicleReqText.getEditText().setText(String.valueOf(picker.getValue()));
                            showViews(picker.getValue());
                        }
//                        else if (s.equals("No of Passengers")) {
//                            Log.d("pass", String.valueOf(picker.getValue()));
//                            noOfPassengersText.getEditText().setText(String.valueOf(picker.getValue()));
//                        }
                    }
                });

        builder.show();
    }

    private void showViews(int count) {

        Log.d("ca", "called");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        TextInputLayout layout = new TextInputLayout(getContext());
        TextInputEditText editText = new TextInputEditText(layout.getContext());
        layout.setLayoutParams(params);
        editText.setLayoutParams(params);
        editText.setHint("Vehicle Number");

        vehicleLayout.addView(layout);
        for (int i = 1; i <= count; i++) {
        }

    }

}