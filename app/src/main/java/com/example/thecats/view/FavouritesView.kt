package com.example.thecats.view

import androidx.annotation.StringRes
import com.example.thecats.base.BaseView
import com.example.thecats.model.FavouriteModel

interface FavouritesView : BaseView {

    fun updateFavourites(favourites: List<FavouriteModel>)

    fun showError(error: String)

    fun showError(@StringRes errorResId: Int) {
        this.showError(getContext().getString(errorResId))
    }

    fun showMessage(text: Int)

    fun showLoading()

    fun hideLoading()
}