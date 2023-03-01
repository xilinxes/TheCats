package com.example.thecats.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.thecats.base.BaseActivity
import com.example.thecats.databinding.ActivityCatsBinding
import com.example.thecats.model.CatsModel
import com.example.thecats.presenter.CatPresenter
import com.example.thecats.view.adapter.CatsAdapter
import java.util.*

class CatsActivity : BaseActivity<CatPresenter>(), CatView {

    private lateinit var binding: ActivityCatsBinding

    private val catsAdapter = CatsAdapter(this) { catId, url, anchor ->
        presenter.prepareContextMenu(catId, url, anchor, this)
    }

    private var menuPopupHelperTime: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cats.apply {
            adapter = catsAdapter
            itemAnimator = null
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0) {
                            presenter.isLastItemDisplaying(recyclerView)
                        }
                    }

                })

        }

        binding.favourites.setOnClickListener {
            val myIntent = Intent(this, FavouritesActivity::class.java)
            this.startActivity(myIntent)
        }

        presenter.onViewCreated()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun updateCats(cats: List<CatsModel>) {
        catsAdapter.updateCats(cats)
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(text: Int) {
        Toast.makeText(this, this.getString(text), Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun instantiatePresenter(): CatPresenter {
        return CatPresenter(this)
    }

    @SuppressLint("RestrictedApi")
    override fun showContextMenu(menuBuilder: MenuBuilder, anchorView: View) {
        MenuPopupHelper(this, menuBuilder, anchorView).apply {
            setForceShowIcon(true)
            val showTime = SystemClock.elapsedRealtime()
            if (showTime - menuPopupHelperTime > 250L) tryShow(0, 20)
            menuPopupHelperTime = showTime
        }
    }
}