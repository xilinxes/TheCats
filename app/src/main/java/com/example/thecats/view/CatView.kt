package com.example.thecats.view

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import com.example.thecats.base.BaseView
import com.example.thecats.model.CatsModel

interface CatView : BaseView {

    fun updateCats(cats: List<CatsModel>)

    fun showError(error: String)

    fun showError(@StringRes errorResId: Int) {
        this.showError(getContext().getString(errorResId))
    }

    fun showMessage(text: Int)

    fun showLoading()

    fun hideLoading()

    fun showContextMenu(menuBuilder: MenuBuilder, anchorView: View)
}