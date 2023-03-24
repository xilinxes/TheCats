package com.example.thecats.injection.module

import android.app.Application
import android.content.Context
import com.example.thecats.base.BaseView
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Scope

@Module
object ContextModule {

    @Provides
    @JvmStatic
    fun provideContext(baseView: BaseView): Context {
        return baseView.getContext()
    }

    @Provides
    @JvmStatic
    fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }
}