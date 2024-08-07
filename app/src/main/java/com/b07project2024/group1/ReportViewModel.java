package com.b07project2024.group1;

import static android.app.PendingIntent.getActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportViewModel extends ViewModel implements IReport.ReportViewModel {
    IReport.ReportFragment fragment = new ReportFragment();
    List<CatalogItem> catalog;
    int pageHeight = 1920;
    int pagewidth = 1080;
    int imgHeight = 550;
    int imgWidth = 700;
    String fileName;

    public ReportViewModel(){
        this.catalog = new ArrayList<>();
    }

    public void getWholeCatalog(String param, String type){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child("catalog");
        fileName = "Catalog Report.pdf";

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CatalogItem> list = new ArrayList<>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    CatalogItem item = child.getValue(CatalogItem.class);
                    list.add(item);
                }

                CatalogItem item = new CatalogItem();

                if(type.equals("lot")){item = generateByLot(list, param);}
                else if(type.equals("name")){item = generateByName(list, param);}
                else if(type.equals("category")){item = generateByCategory(list, param, false);}
                else if(type.equals("categoryDP")){item = generateByCategory(list, param, true);}
                else if(type.equals("period")){item = generateByPeriod(list, param, false);}
                else if(type.equals("periodDP")){item = generateByPeriod(list, param, true);}
                else if(type.equals("all")){item = generateAll(list, false);}
                else if(type.equals("allDP")){item = generateAll(list, true);}

                if(item == null){
                    fragment.displayAlert("Error: No items found in parameters");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void generatePDF(List<CatalogItem> list, int items, int index, boolean dp) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, items).create();

        if(index != -1){createPage(list.get(index), pdfDocument, info, 1, dp);}
        else{
            for(int i=0; i<items; i++){
                createPage(list.get(i), pdfDocument, info, i, dp);
            }
        }

        if(dp){fileName = fileName.replace(".pdf", "(DP Only).pdf");}
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloads, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();

        } catch (FileNotFoundException e) {
            fragment.displayAlert("Error");
            throw new RuntimeException(e);
        } catch (IOException e) {
            fragment.displayAlert("Error");
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
            Log.d("error", "error");
            return null;
        }
    }

    public void createPage(CatalogItem item, PdfDocument pdfDocument, PdfDocument.PageInfo info, int pageNum, boolean dp){
        PdfDocument.Page myPage = pdfDocument.startPage(info);
        Canvas canvas = myPage.getCanvas();

        int height = imgHeight + 150;
        TextPaint textPaint = new TextPaint();
        TextPaint t = new TextPaint();

        textPaint.setColor(Color.BLACK );
        textPaint.setTextSize(34);
        t.setColor(Color.BLACK );
        t.setTextSize(38);

        Bitmap picBig = getBitmap(item.getImageURLs().get(0));
        Bitmap pic = Bitmap.createScaledBitmap(picBig, imgWidth, imgHeight, false);
        if(pic != null){
            canvas.drawBitmap(pic, pagewidth/2-imgWidth/2, 100, t);
        }

        if(!dp) {
            StaticLayout t1 = new StaticLayout("Lot #" + item.getLot(), t, canvas.getWidth() - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            StaticLayout t2 = new StaticLayout(item.getName(), t, canvas.getWidth() - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            StaticLayout t3 = new StaticLayout("Category: " + item.getCategory(), t, canvas.getWidth() - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            StaticLayout t4 = new StaticLayout("Period: " + item.getPeriod(), t, canvas.getWidth() - 100, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

            canvas.save();
            canvas.translate(pagewidth / 2 - 500, height);
            height += 50;
            t1.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(pagewidth / 2 - 500, height);
            height += 50;
            t2.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(pagewidth / 2 - 500, height);
            height += 50;
            t3.draw(canvas);
            canvas.restore();

            canvas.save();
            canvas.translate(pagewidth / 2 - 500, height);
            height += 75;
            t4.draw(canvas);
            canvas.restore();
        }

        StaticLayout textLayout = new StaticLayout(item.getDescription(), textPaint, canvas.getWidth()-100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(100, height);
        textLayout.draw(canvas);
        canvas.restore();

        pdfDocument.finishPage(myPage);
    }

    public CatalogItem generateByLot(List<CatalogItem> list, String lot){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getLot().equals(lot)){
                fileName = "Lot " + lot + " Report.pdf";
                generatePDF(list, 1, i, false);
                return list.get(i);
            }
        }
        fragment.displayAlert("Error: Not Found");
        return null;
    }

    public CatalogItem generateByName(List<CatalogItem> list, String name){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getName().equals(name)){
                fileName = name + " Report.pdf";
                generatePDF(list, 1, i, false);
                return list.get(i);
            }
        }
        fragment.displayAlert("Error: Not Found");
        return null;
    }

    public CatalogItem generateByCategory(List<CatalogItem> list, String category, boolean dp){
        int count = 0;
        List<CatalogItem> newList = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            if (list.get(i).getCategory().equals(category)) {
                newList.add(list.get(i));
                count++;
            }
        }
        if (count == 0) {
            fragment.displayAlert("Error: Not Found");
            return null;
        }

        fileName = category + " Report.pdf";
        generatePDF(newList, count, -1, dp);
        return list.get(0);
    }

    public CatalogItem generateByPeriod(List<CatalogItem> list, String period, boolean dp){
        int count = 0;
        List<CatalogItem> newList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getPeriod().equals(period)){
                newList.add(list.get(i));
                count++;
            }
        }
        if (count == 0) {
            fragment.displayAlert("Error: Not Found");
            return null;
        }

        fileName = period + " Report.pdf";
        generatePDF(newList, count, -1, dp);
        return list.get(0);
    }

    public CatalogItem generateAll(List<CatalogItem> list, boolean dp){
        generatePDF(list, list.size(), -1, dp);
        return list.get(0);
    }


}
