package com.app.newsfeed.ui.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.newsfeed.R
import com.app.newsfeed.pojo.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_headline_details.*

class HeadlineDetailsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline_details)

        getIntentData()
        initListeners()
    }

    private fun initListeners() {
        imageViewBack.setOnClickListener(this)
    }

    private fun getIntentData() {
        intent?.let {
            if (it.hasExtra("ARTICLE")) {
                initData(it.getParcelableExtra("ARTICLE"))
            }
        }
    }

    private fun initData(article: Article) {
        textViewTitle.text = article.title
        textViewDescription.text = article.description
        textViewCnn.text = article.source.name
        Glide
            .with(this)
            .load(article.urlToImage)
            .into(imageViewBanner)
    }

    companion object {

        fun getInstance(context: Context, article: Article): Intent {
            return Intent(context, HeadlineDetailsActivity::class.java).apply {
                putExtra("ARTICLE", article)
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.imageViewBack -> {
                finish()
            }
        }
    }
}