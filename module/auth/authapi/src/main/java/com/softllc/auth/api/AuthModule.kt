package com.softllc.auth.api

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class AuthGo2BankService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthFirebaseService


@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

   // @AuthGo2BankService
//    @Provides
//    fun provideAuthServiceGo2Bank(@ApplicationContext context: Context): AuthService {
//        return AuthGo2BankServiceImpl()
//    }

   // @AuthFirebaseService
    @Singleton
    @Provides
    fun provideAuthServiceFirebase(@ApplicationContext context: Context): AuthService {
        return AuthFirebaseServiceImpl()
    }

}