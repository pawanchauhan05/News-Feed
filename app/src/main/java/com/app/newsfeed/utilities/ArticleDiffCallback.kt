package com.app.newsfeed.utilities

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.app.newsfeed.pojo.Article

class ArticleDiffCallback(private val oldList: List<Article>, private val newList: List<Article>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].publishedAt === newList[newItemPosition].publishedAt
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, author, title) = oldList[oldPosition]
        val (_, author1, title1) = newList[newPosition]

        return author == author1 && title == title1
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }

}