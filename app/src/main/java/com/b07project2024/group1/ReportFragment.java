package com.b07project2024.group1;

import android.Manifest;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Spinner;
import android.widget.Toast;

public class ReportFragment extends Fragment {
    private ReportViewModel reportViewModel = new ReportViewModel();
    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment_report_view, container, false);

        Spinner report = view.findViewById(R.id.spinnerReportType);
        EditText parameter = view.findViewById(R.id.editTextParameter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.report_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report.setAdapter(adapter);

        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getActivity(), "Permission Acccepted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        btn = view.findViewById(R.id.reportButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportText = report.getSelectedItem().toString();
                String paramText = parameter.getText().toString();

                if(reportText.equals("Generate report by Lot number") && !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "lot");}
                else if(reportText.equals("Generate report by Name")&& !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "name");}
                else if(reportText.equals("Generate report by Category")&& !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "category");}
                else if(reportText.equals("Generate report by Category with Desciption and Picture only")&& !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "categoryDP");}
                else if(reportText.equals("Generate report by Period")&& !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "period");}
                else if(reportText.equals("Generate report by Period with Description and Picture only")&& !paramText.isEmpty()){reportViewModel.getWholeCatalog(paramText, "periodDP");}
                else if(reportText.equals("Generate report for all items")){reportViewModel.getWholeCatalog(paramText, "all");}
                else if(reportText.equals("Generate report for all items with Description and Picture only")){reportViewModel.getWholeCatalog(paramText, "allDP");}
                else{Toast.makeText(getActivity(), "Please fill out the parameter field", Toast.LENGTH_SHORT).show();}

                parameter.setText("");
            }
        });
        return view;
    }
}

