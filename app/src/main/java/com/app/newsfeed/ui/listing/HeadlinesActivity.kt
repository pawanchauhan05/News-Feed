package com.app.newsfeed.ui.listing

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.newsfeed.AppController
import com.app.newsfeed.R
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.pojo.EmptyView
import com.app.newsfeed.ui.details.HeadlineDetailsActivity
import com.app.newsfeed.utilities.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_headlines.*
import kotlinx.coroutines.*

class HeadlinesActivity :  AppCompatActivity() {

    private  val TAG = "HeadlinesActivity"

    lateinit var headlineAdapter: HeadlineAdapter

    private val headlinesViewModel by viewModels<HeadlinesViewModel> {
        HeadlinesViewModelFactory((applicationContext as AppController).dataRepository)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@HeadlinesActivity)
    }

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var page: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines)

        headlineAdapter = HeadlineAdapter({
            startActivity(HeadlineDetailsActivity.getInstance(this, it))
        }, ArrayList())

        recyclerView.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = headlineAdapter
        }

        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                page += 1
                headlinesViewModel.getArticles(page)
            }

        })

        initObservers()

        headlinesViewModel.getArticles(page)

    }

    private fun initObservers() {
        headlinesViewModel.articleList.observe(this, Observer {
            when (it) {
                is ResultData.Success -> {
                    isLoading = false
                    headlineAdapter.updateList(it.dataList)

                    if(headlineAdapter.list.isEmpty()) {
                        textViewError.text = "No Articles Found"
                        recyclerView.visibility = View.GONE
                        textViewError.visibility = View.VISIBLE
                    }
                }
                is ResultData.Failure -> {
                    isLoading = false
                }
            }
        })

        headlinesViewModel.dataLoading.observe(this, Observer {
            when(it) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                }
                false -> {
                    progressBar.visibility = View.GONE
                }
            }
        })

        headlinesViewModel.empty.observe(this, Observer {
            when(it) {
                is EmptyView.EmptyData -> {
                    textViewError.text = it.message
                    recyclerView.visibility = View.GONE
                    textViewError.visibility = View.VISIBLE
                }

                is EmptyView.NetworkError -> {
                    when(headlineAdapter.list.isNotEmpty()) {
                        true -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        false -> {
                            textViewError.text = it.message
                            recyclerView.visibility = View.GONE
                            textViewError.visibility = View.VISIBLE
                        }
                    }
                }

                is EmptyView.LastPage -> {
                    isLastPage = true
                }
            }
        })
    }
}