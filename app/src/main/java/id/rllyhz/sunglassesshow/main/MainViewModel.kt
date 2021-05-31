package id.rllyhz.sunglassesshow.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rllyhz.core.domain.model.Movie
import id.rllyhz.core.domain.model.TVShow
import id.rllyhz.core.domain.usecase.SunGlassesShowUseCase
import id.rllyhz.core.vo.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: SunGlassesShowUseCase
) : ViewModel() {

    val movies = useCase.getMovies().asLiveData()
    val tvShows = useCase.getTVShows().asLiveData()
    private val queryForSearchingMovies = MutableLiveData<String>()
    private val queryForSearchingTVShows = MutableLiveData<String>()

    private val searchingMoviesResults = object : MutableLiveData<Resource<List<Movie>>>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }

            viewModelScope.launch {
                queryForSearchingMovies.asFlow()
                    .debounce(250)
                    .distinctUntilChanged()
                    .collect {
                        if (it.isNotEmpty()) {
                            useCase.searchMovies(it).collect { resultValue ->
                                value = resultValue
                            }
                        } else {
                            useCase.getMovies().collect { resultValue ->
                                value = resultValue
                            }
                        }
                    }
            }
        }
    }

    fun getSearchingMoviesResults() = searchingMoviesResults

    private val searchingTVShowsResults = object : MutableLiveData<Resource<List<TVShow>>>() {
        override fun onActive() {
            super.onActive()
            value?.let { return }

            viewModelScope.launch {
                queryForSearchingTVShows.asFlow()
                    .debounce(250)
                    .distinctUntilChanged()
                    .collect {
                        if (it.isNotEmpty()) {
                            useCase.searchTVShows(it).collect { resultValue ->
                                value = resultValue
                            }
                        } else {
                            useCase.getTVShows().collect { resultValue ->
                                value = resultValue
                            }
                        }
                    }
            }
        }
    }

    fun getSearchingTVShowsResults() = searchingTVShowsResults

    fun setQueryForSearchingMovies(newQuery: String) {
        queryForSearchingMovies.value = newQuery
    }

    fun setQueryForSearchingTVShows(newQuery: String) {
        queryForSearchingTVShows.value = newQuery
    }
}