package nyp.sit.movieviewer.advanced

import android.os.AsyncTask
import nyp.sit.movieviewer.advanced.entity.Movies

class MoviesRepository(private val moviesDao: MoviesDao) {

    val allMovies = moviesDao.retrieveAllMovies()

    fun insert(movie : Movies) {
        moviesDao.insert(movie)
    }

    fun delete(movie: Movies) {
        moviesDao.delete(movie)
    }

    fun deleteAll() {
        AsyncTask.execute {
            moviesDao?.deleteAll()
        }
    }
}