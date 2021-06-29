package com.app.newsfeed.ui.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.newsfeed.R
import com.app.newsfeed.pojo.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.headline_list_item_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HeadlineAdapter(
    private val onItemClicked: (Article) -> Unit,
    var list: ArrayList<Article>
) : RecyclerView.Adapter<HeadlineAdapter.ViewHolder>() {

    inner class ViewHolder(private val containerView: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(containerView) {
        init {
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun bind(data: Article) {

            itemView.textViewCnn.text = "${data.source.name}\t\t${
                SimpleDateFormat("yyyy-dd-mm").format(
                    SimpleDateFormat(
                        "yyyy-dd-mm",
                        Locale.ENGLISH
                    ).parse(data.publishedAt)
                )
            }"
            itemView.textViewTitle.text = data.title
            Glide
                .with(itemView.context)
                .load(data.urlToImage)
                .into(itemView.imageViewBanner)

            itemView.setOnClickListener { onItemClicked(data) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.headline_list_item_layout, parent, false)
        return ViewHolder(view) {
            onItemClicked(list[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(dataList: List<Article>) {

        /*val diffCallback = ArticleDiffCallback(list, dataList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(dataList)
        diffResult.dispatchUpdatesTo(this)*/

        val newList = ArrayList<Article>()
        dataList.forEach { article ->
            var flag: Boolean = false
            list.forEach {
                if (it.publishedAt.equals(article.publishedAt)) {
                    flag = true
                }
            }
            if (!flag) {
                newList.add(article)
            }
        }
        list.addAll(newList)
        notifyDataSetChanged()
    }
}