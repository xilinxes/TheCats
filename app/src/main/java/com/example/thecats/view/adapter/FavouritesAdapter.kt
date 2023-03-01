package com.example.thecats.view.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.thecats.R
import com.example.thecats.databinding.ItemFavouriteBinding
import com.example.thecats.model.FavouriteModel
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class FavouritesAdapter(private val context: Context) :
    RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {

    private var favourites: ArrayList<FavouriteModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        return FavouriteViewHolder(
            ItemFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context
        )
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        return holder.bind(favourites[position])
    }

    fun updateCats(favourites: List<FavouriteModel>) {
        val oldSize = this.favourites.size
        this.favourites.addAll(favourites)
        notifyItemRangeChanged(oldSize, this.favourites.size)
    }

    class FavouriteViewHolder(
        private val binding: ItemFavouriteBinding, private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteItem: FavouriteModel) {
            val requestBuilder: RequestBuilder<Drawable> =
                Glide.with(context).asDrawable().sizeMultiplier(0.2f)
            Glide
                .with(context)
                .load(favouriteItem.path)
                .thumbnail(requestBuilder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
                .into(binding.catImage)
        }
    }
}