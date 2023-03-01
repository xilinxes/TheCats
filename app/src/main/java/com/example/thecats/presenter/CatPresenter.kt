package com.example.thecats.presenter

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.thecats.R
import com.example.thecats.base.BasePresenter
import com.example.thecats.repositories.CatsRepo
import com.example.thecats.view.CatView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.util.*
import java.util.concurrent.Callable
import javax.inject.Inject


class CatPresenter(catView: CatView) : BasePresenter<CatView>(catView) {

    @Inject
    lateinit var catsRepo: CatsRepo

    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated() {
        loadCats()
    }

    fun saveToDownLoads(url: String, context: Context) {
        try {
            val downloadManager =
                context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, url.substringAfterLast('/')
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isLastItemDisplaying(recyclerView: RecyclerView) {
        recyclerView.adapter?.let { adapter ->
            if (adapter.itemCount == 0) return
            val layoutManager = recyclerView.layoutManager ?: return

            val lastVisibleItemPositions =
                (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                    null
                )
            val lastVisibleItemPosition: Int = getLastVisibleItem(lastVisibleItemPositions)
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION
                && lastVisibleItemPosition == adapter.itemCount - 1
            ) {
                loadCats()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun prepareContextMenu(
        catId: String,
        url: String,
        anchorView: View,
        activity: AppCompatActivity
    ) {
        val menuBuilder = MenuBuilder(activity)
        activity.menuInflater.inflate(R.menu.main_menu, menuBuilder)

        menuBuilder.setCallback(object : MenuBuilder.Callback {

            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.favourites -> onFavouriteClicked()
                    R.id.save -> onDownloadClicked()
                }
                return false
            }

            override fun onMenuModeChange(menu: MenuBuilder) = Unit

            private fun onFavouriteClicked() {
                Glide.with(activity)
                    .asBitmap()
                    .load(url)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            saveToInternalStorage(
                                activity = activity,
                                name = url.substringAfterLast('/'),
                                bitmapImage = resource
                            ).subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSuccess { path: String? ->
                                    if (path != null) {
                                        val addFavouriteDisposable =
                                            catsRepo.addCatToFavourites(catId, path)
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe { view.showMessage(R.string.added) }
                                        compositeDisposable.add(addFavouriteDisposable)
                                    }
                                }
                                .subscribe()
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            view.showError(R.string.something_went_wrong)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }

            private fun onDownloadClicked() {
                saveToDownLoads(url, activity)
            }

        }
        )
        view.showContextMenu(menuBuilder, anchorView)
    }

    private fun loadCats() {
        view.showLoading()
        val subscription = catsRepo.loadCats()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.doOnTerminate { view.hideLoading() }
            ?.subscribe(
                { catList ->
                    view.updateCats(catList)
                    Log.d("List", catList.toString())
                },
                {
                    view.showError(it.localizedMessage)
                    Log.e("Error", it.localizedMessage)
                }
            )
        subscription?.let { compositeDisposable.add(it) }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    private fun saveToInternalStorage(
        bitmapImage: Bitmap,
        activity: AppCompatActivity,
        name: String
    ) = Single.fromCallable {
        val cw = ContextWrapper(activity.application)

        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)

        val myPath = File(directory, name)
        myPath.writeBitmap(bitmapImage, Bitmap.CompressFormat.PNG, 100)
        myPath.absolutePath
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }

    override fun onViewDestroyed() {
        compositeDisposable.dispose()
    }
}

