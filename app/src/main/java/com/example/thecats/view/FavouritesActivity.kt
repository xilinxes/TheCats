package com.example.thecats.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.thecats.base.BaseActivity
import com.example.thecats.databinding.ActivityFavouritesBinding
import com.example.thecats.model.FavouriteModel
import com.example.thecats.presenter.FavouritePresenter
import com.example.thecats.view.adapter.FavouritesAdapter


class FavouritesActivity : BaseActivity<FavouritePresenter>(), FavouritesView {

    private lateinit var binding: ActivityFavouritesBinding

    private val favouritesAdapter = FavouritesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.cats.apply {
            adapter = favouritesAdapter
            itemAnimator = null
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        presenter.onViewCreated()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        this.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun updateFavourites(favourites: List<FavouriteModel>) {
        favouritesAdapter.updateCats(favourites)
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

    override fun instantiatePresenter(): FavouritePresenter {
        return FavouritePresenter(this)
    }

}