package com.b07project2024.group1;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.gms.common.internal.Asserts.checkNull;
//TODO Resolve duplicate imports

public class ReportFragment extends Fragment {
    private CatalogViewModel catalogViewModel;
    Button btn;
    int pageHeight = 1920;
    int pagewidth = 1080;
    int imgHeight = 600;
    int imgWidth = 750;
    Bitmap bmp, scaledbmp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        View view = inflater.inflate(R.layout.fragment_report_view, container, false);
        catalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        EditText lot = view.findViewById(R.id.buttonEditLot);

        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        Toast.makeText(getActivity(), "Permission acccepted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    }
                });
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

        btn = view.findViewById(R.id.reportButton);
//        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_24dp);
//        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotText = lot.getText().toString();
                Log.d("LOTOT", "Lot is given as " + lotText);

                CatalogItem search = new CatalogItem();
                search.setLot(lotText);
                LiveData<List<CatalogItem>> list = catalogViewModel.getLiveList();

                if(list.getValue() != null){
                    for(int i=0; i<list.getValue().size(); i++){
                        if(list.getValue().get(i).getLot().equals(search.getLot())){
                            generatePDF(list.getValue().get(i));
                            return;
                        }
                    }
                    Log.d("ERRRR", "NOT FOUND");
                }
            }
        });
        return view;
    }

    private void generatePDF(CatalogItem item) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        TextPaint t = new TextPaint();
        TextPaint textPaint = new TextPaint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        textPaint.setColor(Color.BLACK );
        textPaint.setTextSize(42);
        t.setColor(Color.BLACK );
        t.setTextSize(42);

        String lotStr = "Lot # ";
        String lot = item.getLot();

        String name = item.getName();
        Log.d("pic", item.getImageURLs().get(0));

        String catStr = "Category: ";
        String cat = item.getCategory();

        String perStr = "Period: ";
        String per = item.getPeriod();

        String descStr = "Description: ";
        String desc = item.getDescription();

        Bitmap picBig = getBitmap(item.getImageURLs().get(0));
        Bitmap pic = Bitmap.createScaledBitmap(picBig, imgWidth, imgHeight, false);
        if(pic != null){
            canvas.drawBitmap(pic, pagewidth/2-imgWidth/2, 100, paint);
            Log.d("err", "making image");
        }
//        canvas.drawText(lotStr + lot, pagewidth/2-100, 150 + imgHeight, t);
//        canvas.drawText(name, pagewidth/2-100, 200 + imgHeight, t);
//        canvas.drawText(cat, pagewidth/2-100, 250 + imgHeight, t);
//        canvas.drawText(per, pagewidth/2-100, 300 + imgHeight, t);

        StaticLayout textLayout = new StaticLayout(desc + "aukwhfuiohawfuhifwauh more filler frnraodmm sodtoest mfeiller ieand eoteaxt isflelra sifdnwame", textPaint, canvas.getWidth()-100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout t1 = new StaticLayout(lotStr + lot, t, canvas.getWidth()-100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        StaticLayout t2 = new StaticLayout(name, t, canvas.getWidth()-100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        StaticLayout t3 = new StaticLayout(catStr + cat, t, canvas.getWidth()-100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        StaticLayout t4 = new StaticLayout(perStr + per, t, canvas.getWidth()-100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        canvas.save();
        canvas.translate(pagewidth/2-500, 150 + imgHeight);
        t1.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(pagewidth/2-500, 200 + imgHeight);
        t2.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(pagewidth/2-500, 250 + imgHeight);
        t3.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(pagewidth/2-500, 300 + imgHeight);
        t4.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(100, 400 + imgHeight);
        textLayout.draw(canvas);
        canvas.restore();

        pdfDocument.finishPage(myPage);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String nam = "test.pdf";

        File file = new File(downloads, nam);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();
            Toast.makeText(getContext(), "written success", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Log.d("ERRR1", "printing errror");
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.d("ERRR2", "printing errror");
            throw new RuntimeException(e);
        }
    }

    public static Bitmap getBitmap(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap pic = BitmapFactory.decodeStream(input);
            return pic;
        } catch(IOException e){
            Log.d("errr", "error");
            return null;
        }
    }


}

