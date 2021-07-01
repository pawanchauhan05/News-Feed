package com.app.newsfeed.ui.listing

import androidx.lifecycle.*
import com.app.newsfeed.data.IDataRepository
import com.app.newsfeed.data.source.ResultData
import com.app.newsfeed.pojo.EmptyView
import com.app.newsfeed.utilities.Config
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val dataRepository: IDataRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val TAG = "HeadlinesViewModel"

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _empty = MutableLiveData<EmptyView>()
    val empty: LiveData<EmptyView> = _empty

    private val _articleList = MutableLiveData<ResultData>()
    val articleList: LiveData<ResultData> = _articleList

    fun getArticles(page: Int) {

        _dataLoading.postValue(true)

        var queryParams = mutableMapOf(
            "page" to "$page",
            "country" to "us",
            "apiKey" to "${Config.API_KEY}",
            "pageSize" to "${Config.PAGE_SIZE}"
        )

        viewModelScope.launch(ioDispatcher) {
            dataRepository.getHeadlines(queryParams, page).collect {
                when (it) {
                    is ResultData.Success -> {
                        if(it.dataList.isNotEmpty()) {
                            _articleList.postValue(ResultData.Success(it.dataList))
                        } else {
                            _empty.postValue(EmptyView.LastPage(""))
                        }
                    }

                    is ResultData.Failure -> {
                        when (it.exception) {
                            is SocketTimeoutException -> {
                                _empty.postValue(EmptyView.NetworkError("TIMEOUT ERROR!"))
                            }
                            is UnknownHostException -> {
                                _empty.postValue(EmptyView.NetworkError("NO INTERNET!"))
                            }
                            else -> {
                                _empty.postValue(EmptyView.NetworkError(it.exception.message ?: "Something went wrong"))
                            }
                        }
                    }
                }
            }
        }.invokeOnCompletion {
            _dataLoading.postValue(false)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class HeadlinesViewModelFactory @Inject constructor(
    private val dataRepository: IDataRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (HeadlinesViewModel(dataRepository) as T)
}
