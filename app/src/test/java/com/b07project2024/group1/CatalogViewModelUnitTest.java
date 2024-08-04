package com.b07project2024.group1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class CatalogViewModelUnitTest {
    private CatalogViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private CatalogRepository mockRepo;
    @Mock
    private Observer<List<CatalogItem>> observer;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        viewModel = new CatalogViewModel(mockRepo);
        viewModel.getInitialCatalogPage().observeForever(observer);
    }

    @Test
    public void catalogViewModel_getInitialPage()  {
        // Setup ArgumentCaptor
        ArgumentCaptor<List<CatalogItem>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.doAnswer(invocation -> {
            MutableLiveData<List<CatalogItem>> liveItems = invocation.getArgument(0);
            List<CatalogItem> items = new ArrayList<>(); // Populate with test data
            items.add(new CatalogItem("Lot Test 1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            items.add(new CatalogItem("Lot Test 2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            liveItems.postValue(items);
            return null;
        }).when(mockRepo).getCatalogPage(any());

        viewModel.getInitialCatalogPage();
        // Verify the result
        verify(observer, times(1)).onChanged(captor.capture());
        List<CatalogItem> capturedList = captor.getValue();
        assertNotNull(capturedList);
        assertEquals(2, capturedList.size());
        assertEquals("Lot Test 1", capturedList.get(0).getLot());
        assertEquals("Lot Test 2", capturedList.get(1).getLot());
    }

    @Test
    public void catalogViewModel_getNextCatalogPage()  {
        // Setup ArgumentCaptor
        ArgumentCaptor<List<CatalogItem>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.doAnswer(invocation -> {
            MutableLiveData<List<CatalogItem>> liveItems = invocation.getArgument(1);
            List<CatalogItem> items = new ArrayList<>(); // Populate with test data
            items.add(new CatalogItem("Lot Test 1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            items.add(new CatalogItem("Lot Test 2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            liveItems.postValue(items);
            return null;
        }).when(mockRepo).getNextCatalogPage(any(),any());

        viewModel.getNextCatalogPage();
        // Verify the result
        verify(observer, times(1)).onChanged(captor.capture());
        List<CatalogItem> capturedList = captor.getValue();
        assertNotNull(capturedList);
        assertEquals(2, capturedList.size());
        assertEquals("Lot Test 1", capturedList.get(0).getLot());
        assertEquals("Lot Test 2", capturedList.get(1).getLot());
    }

    @Test
    public void catalogViewModel_getNextCatalogPage_filterSet(){
        CatalogItem item = new CatalogItem();
        item.setPeriod("THIS");
        viewModel.setFilter(item);

        ArgumentCaptor<List<CatalogItem>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.doAnswer(invocation -> {
            MutableLiveData<List<CatalogItem>> liveItems = invocation.getArgument(1);
            List<CatalogItem> items = new ArrayList<>(); // Populate with test data
            items.add(new CatalogItem("Lot Test 1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            items.add(new CatalogItem("Lot Test 2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
            liveItems.postValue(items);
            return null;
        }).when(mockRepo).getNextCatalogPage(any(),any());

        viewModel.getNextCatalogPage();
        //Verify Next Page isn't called if filter has been set
        verify(observer, times(0)).onChanged(captor.capture());
    }

    @Test
    public void catalogViewModel_getNextCatalogPage_lastKeySet(){
        List<CatalogItem> items = new ArrayList<>(); // Populate with test data
        items.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));

        viewModel.setLiveList(items);

        Mockito.doAnswer(invocation -> {
            return null;
        }).when(mockRepo).getNextCatalogPage(any(),any());
        viewModel.getNextCatalogPage();
        assertEquals(viewModel.getLastKey(), "1");
    }

    @Test
    public void catalogViewModel_getNextCatalogPage_lastKeySet_empty(){
        List<CatalogItem> items = new ArrayList<>();
        viewModel.setLiveList(items);
        Mockito.doAnswer(invocation -> {
            return null;
        }).when(mockRepo).getNextCatalogPage(any(),any());
        viewModel.getNextCatalogPage();
        assertEquals(viewModel.getLastKey(),"-1");
    }

    @Test
    public void catalogViewModel_getInitialPage_withFilter(){
        // Setup ArgumentCaptor
        ArgumentCaptor<List<CatalogItem>> captor = ArgumentCaptor.forClass(List.class);
        CatalogItem item = new CatalogItem();
        item.setPeriod("Period Test 1");
        Mockito.doAnswer(invocation -> {
            MutableLiveData<List<CatalogItem>> liveItems = invocation.getArgument(1);
            List<CatalogItem> items = new ArrayList<>(); // Populate with test data
            items.add(new CatalogItem("Lot Test 1", "Name Test", "Cat Test", "Desc Test", "Period Test 1", "Pic Test"));
            liveItems.postValue(items);
            return null;
        }).when(mockRepo).getCatalogPageByItem(any(),any());
        viewModel.setFilter(item);
        viewModel.getInitialCatalogPage();
        // Verify the result
        verify(observer, times(1)).onChanged(captor.capture());
        List<CatalogItem> capturedList = captor.getValue();
        assertNotNull(capturedList);
        assertEquals(1, capturedList.size());
        assertEquals("Lot Test 1", capturedList.get(0).getLot());
    }

    @Test
    public void catalogViewModel_clearSearch(){
        viewModel.clearSearch();
        assertNull(viewModel.getFilter());
        assertEquals(viewModel.getLastKey(), "-1");
    }

    @Test
    public void catalogViewModel_setLastKey(){
        viewModel.setLastKey("0");
        assertEquals(viewModel.getLastKey(), "0");
    }
    @Test
    public void catalogViewModel_getLastKey(){
        assertEquals(viewModel.getLastKey(), "-1");
    }

    @Test
    public void catalogViewModel_set_get_LiveList(){
        List<CatalogItem> items = new ArrayList<>();
        items.add(new CatalogItem("1", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        items.add(new CatalogItem("2", "Name Test", "Cat Test", "Desc Test", "Period Test", "Pic Test"));
        viewModel.setLiveList(items);
        assertEquals(viewModel.getLiveList().getValue(), items);
    }

    @Test
    public void catalogViewModel_getFilterLive(){
        assertNull(viewModel.getFilterLive().getValue());
    }
}
