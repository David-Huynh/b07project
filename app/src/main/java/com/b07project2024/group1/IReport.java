package com.b07project2024.group1;

import android.graphics.pdf.PdfDocument;

import java.util.List;

public interface IReport {
    public interface ReportPresenter {
        public void getWholeCatalog(String param, String type, boolean dp);
        public void createPage(CatalogItem item, PdfDocument pdfDocument, PdfDocument.PageInfo info, int pageNum, boolean dp);
        public void generateByLot(List<CatalogItem> list, String lot, boolean dp);
        public void generateByName(List<CatalogItem> list, String name, boolean dp);
        public void generateByCategory(List<CatalogItem> list, String category, boolean dp);
        public void generateByPeriod(List<CatalogItem> list, String period, boolean dp);
        public void generateAll(List<CatalogItem> list, boolean dp);
        public String getStatus();
    }
}
