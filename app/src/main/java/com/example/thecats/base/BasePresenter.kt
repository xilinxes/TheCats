package com.example.thecats.base

import com.example.thecats.injection.component.DaggerPresenterInjector
import com.example.thecats.injection.component.PresenterInjector
import com.example.thecats.injection.module.ContextModule
import com.example.thecats.injection.module.DatabaseModule
import com.example.thecats.injection.module.NetworkModule
import com.example.thecats.presenter.CatPresenter
import com.example.thecats.presenter.FavouritePresenter

abstract class BasePresenter<out V : BaseView>(protected val view: V) {
    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .networkModule(NetworkModule)
        .dataBaseModule(DatabaseModule)
        .build()

    init {
        inject()
    }

    open fun onViewCreated(){}

    open fun onViewDestroyed(){}

    private fun inject() {
        when (this) {
            is CatPresenter -> injector.inject(this)
            is FavouritePresenter -> injector.inject(this)
        }
    }
}