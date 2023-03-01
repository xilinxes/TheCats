package com.example.thecats.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.thecats.R
import com.example.thecats.databinding.ItemCatBinding
import com.example.thecats.model.CatsModel
import com.example.thecats.utils.ext.dp


class CatsAdapter(
    private val context: Context,
    private val onShowContextMenuClicked: (String, String, View) -> Unit
) :
    RecyclerView.Adapter<CatsAdapter.CatViewHolder>() {

    private var cats: ArrayList<CatsModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder(
            ItemCatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context,
            onShowContextMenuClicked
        )
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        return holder.bind(cats[position])
    }

    fun updateCats(cats: List<CatsModel>) {
        val oldSize = this.cats.size
        this.cats.addAll(cats)
        notifyItemRangeChanged(oldSize, this.cats.size)
    }

    class CatViewHolder(
        private val binding: ItemCatBinding, private val context: Context,
        private val onShowContextMenuClicked: (String, String, View) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cat: CatsModel) {
            binding.catImage.layoutParams.height = cat.height.dp.toInt().coerceAtMost(1280)

            val requestBuilder: RequestBuilder<Drawable> =
                Glide.with(context).asDrawable().sizeMultiplier(0.2f)
            Glide
                .with(context)
                .load(cat.url)
                .thumbnail(requestBuilder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(32)))
                .into(binding.catImage)

            binding.settings.setOnClickListener {
                onShowContextMenuClicked.invoke(cat.id, cat.url, binding.settings)
            }
        }
    }
}