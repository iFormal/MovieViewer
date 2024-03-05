package nyp.sit.movieviewer.advanced

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.entity.Movies

class MoviesViewModel(val repo: MoviesRepository) : ViewModel() {

    val allMovies: LiveData<List<Movies>> = repo.allMovies.asLiveData()

    fun insert(movieItem : Movies) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(movieItem)
    }

    fun remove(movieItem : Movies) = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(movieItem)
    }

    fun deleteAll() {
        repo.deleteAll()
    }
}

class MoviesViewModelFactory(private val repo : MoviesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {

            return MoviesViewModel(repo) as T
        }

        throw IllegalArgumentException("Unknown viewmodel class")
    }
}