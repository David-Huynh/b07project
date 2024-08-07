package com.b07project2024.group1;

import android.graphics.pdf.PdfDocument;

import java.util.List;

public interface IReport {
    public interface ReportViewModel {
        public void getWholeCatalog(String param, String type);
        public void createPage(CatalogItem item, PdfDocument pdfDocument, PdfDocument.PageInfo info, int pageNum, boolean dp);
        public CatalogItem generateByLot(List<CatalogItem> list, String lot);
        public CatalogItem generateByName(List<CatalogItem> list, String name);
        public CatalogItem generateByCategory(List<CatalogItem> list, String category, boolean dp);
        public CatalogItem generateByPeriod(List<CatalogItem> list, String period, boolean dp);
        public CatalogItem generateAll(List<CatalogItem> list, boolean dp);
    }

    public interface ReportFragment {
        public void displayAlert(String alert);
    }
}
