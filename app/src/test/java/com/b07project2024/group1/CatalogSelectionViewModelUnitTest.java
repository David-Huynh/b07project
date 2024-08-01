package com.b07project2024.group1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.recyclerview.selection.Selection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class CatalogSelectionViewModelUnitTest {
    private CatalogSelectionViewModel viewModel;
    @Before
    public void setup(){
        viewModel = new CatalogSelectionViewModel();
    }

    @Test
    public void catalogSelectionViewModel_setSelectedItems_nullSelected(){
        List<CatalogItem> items = new ArrayList<>();
        items.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        viewModel.setSelectedItems(items,null);
        assertEquals(viewModel.getSelectedItems().getValue(), new ArrayList<>());
    }
    @Test
    public void catalogSelectionViewModel_setSelectedItems_emptySelected(){
        List<CatalogItem> items = new ArrayList<>();
        items.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("3", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));

        Selection<String> selections = mock(Selection.class);
        when(selections.iterator()).thenReturn(Collections.emptyIterator());

        viewModel.setSelectedItems(items, selections);

        List<CatalogItem> expectedItems = new ArrayList<>();

        assertEquals(expectedItems, viewModel.getSelectedItems().getValue());
    }

    @Test
    public void catalogSelectionViewModel_setSelectedItems_selectedItems(){
        List<CatalogItem> items = new ArrayList<>();
        items.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("3", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));

        Selection<String> selections = mock(Selection.class);
        when(selections.iterator()).thenReturn(Arrays.asList("1","3").iterator());

        viewModel.setSelectedItems(items, selections);

        List<CatalogItem> expectedItems = new ArrayList<>();
        expectedItems.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        expectedItems.add(new CatalogItem("3", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));

        assertEquals(expectedItems, viewModel.getSelectedItems().getValue());
    }

    @Test
    public void catalogSelectionViewModel_setSelectedItems_noCatalog_selectedItems(){
        List<CatalogItem> items = new ArrayList<>();

        Selection<String> selections = mock(Selection.class);
        when(selections.iterator()).thenReturn(Arrays.asList("1","3").iterator());

        viewModel.setSelectedItems(items, selections);

        List<CatalogItem> expectedItems = new ArrayList<>();

        assertEquals(expectedItems, viewModel.getSelectedItems().getValue());
    }

    @Test
    public void catalogSelectionViewModel_setSelectedItems_nullCatalog_selectedItems(){
        Selection<String> selections = mock(Selection.class);
        when(selections.iterator()).thenReturn(Arrays.asList("1","3").iterator());

        viewModel.setSelectedItems(null, selections);

        List<CatalogItem> expectedItems = new ArrayList<>();

        assertEquals(expectedItems, viewModel.getSelectedItems().getValue());
    }

    @Test
    public void catalogSelectionViewModel_clearSelectedItems(){
        viewModel.clearSelectedItems();
        assertNull(viewModel.getSelectedItems().getValue());
    }
}
