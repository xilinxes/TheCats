package com.example.thecats.injection.component

import com.example.thecats.base.BaseView
import com.example.thecats.injection.module.ContextModule
import com.example.thecats.injection.module.DatabaseModule
import com.example.thecats.injection.module.NetworkModule
import com.example.thecats.presenter.CatPresenter
import com.example.thecats.presenter.FavouritePresenter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ContextModule::class), (NetworkModule::class), (DatabaseModule::class)])
interface PresenterInjector {
    fun inject(catPresenter: CatPresenter)
    fun inject(favouritePresenter: FavouritePresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder
        fun dataBaseModule(dataBaseModule: DatabaseModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}