package com.b07project2024.group1;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Dependency Injection provider for login repo
 */
@Module
@InstallIn(SingletonComponent.class)
public class LoginModule {
    @Provides
    public LoginRepository provideLoginRepository() {
        return new LoginFirebaseRepository();
    }
}
