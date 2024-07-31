package com.b07project2024.group1;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * CatalogModule for Dependency Injection in CatalogFirebaseRepository using Hilt
 */
@Module
@InstallIn(SingletonComponent.class)
public class CatalogModule {
    @Provides
    public FirebaseManager provideFirebaseManager() {
        return FirebaseManager.getInstance();
    }

    @Provides
    public Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    public CatalogRepository provideCatalogRepository() {
        return new CatalogFirebaseRepository(provideFirebaseManager(), provideExecutor());
    }
}