package com.b07project2024.group1;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportFragment extends Fragment {
    private IReport.ReportPresenter reportPresenter = new ReportPresenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment_report_view, container, false);

        Spinner report = view.findViewById(R.id.spinnerReportType);
        EditText parameter = view.findViewById(R.id.editTextParameter);
        Switch dp = view.findViewById(R.id.switchDP);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.report_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report.setAdapter(adapter);

        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getActivity(), "Permission Accepted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        Button button = view.findViewById(R.id.reportButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportText = report.getSelectedItem().toString();
                String paramText = parameter.getText().toString();
                String toast = "null";
                boolean DP = dp.isChecked();

                if(reportText.equals("Generate report by Lot number") && !paramText.isEmpty()){reportPresenter.getWholeCatalog(paramText, "lot", DP);}
                else if(reportText.equals("Generate report by Name")&& !paramText.isEmpty()){reportPresenter.getWholeCatalog(paramText, "name", DP);}
                else if(reportText.equals("Generate report by Category")&& !paramText.isEmpty()){reportPresenter.getWholeCatalog(paramText, "category", DP);}
                else if(reportText.equals("Generate report by Period")&& !paramText.isEmpty()){reportPresenter.getWholeCatalog(paramText, "period", DP);}
                else if(reportText.equals("Generate report for all items")){reportPresenter.getWholeCatalog(paramText, "all", DP);}
                else{Toast.makeText(getActivity(), "Please fill out the parameter field", Toast.LENGTH_SHORT).show();}

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String msg = reportPresenter.getStatus();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                }, 100);

                parameter.setText("");
                dp.setChecked(false);
            }
        });
        return view;
    }

}

