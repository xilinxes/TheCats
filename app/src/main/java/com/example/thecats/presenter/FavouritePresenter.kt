package com.example.thecats.presenter

import com.example.thecats.base.BasePresenter
import com.example.thecats.repositories.CatsRepo
import com.example.thecats.view.FavouritesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouritePresenter(favouritesView: FavouritesView) :
    BasePresenter<FavouritesView>(favouritesView) {

    @Inject
    lateinit var catsRepo: CatsRepo

    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated() {
        loadImagesFromStorage()
    }

    private fun loadImagesFromStorage() {
        view.showLoading()
        val getFavouritesDisposable = catsRepo.getAllFavourites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.updateFavourites(it)
                view.hideLoading()
            }, {
                view.hideLoading()
                view.showError(it.localizedMessage)
            })
        compositeDisposable.add(getFavouritesDisposable)
    }

    override fun onViewDestroyed() {
        compositeDisposable.dispose()
    }
}