package nyp.sit.movieviewer.advanced

import android.content.Context
import nyp.sit.movieviewer.advanced.entity.MovieItem
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class movieDBJsonUtils() {

    companion object {

        @Throws(JSONException::class)
        fun getMovieDetailsFromJson(context: Context, movieDetailsJsonStr: String): ArrayList<MovieItem>? {

            val MOVIE_LIST = "results"
            val MOVIE_VOTE_COUNT = "vote_count"
            val MOVIE_ID = "id"
            val MOVIE_VIDEO = "video"
            val MOVIE_VOTE_AVERAGE = "vote_average"
            val MOVIE_TITLE = "title"
            val MOVIE_POPULARITY = "popularity"
            val MOVIE_POSTER_PATH = "poster_path"
            val MOVIE_ORIGINAL_LANG = "original_language"
            val MOVIE_ORIGINAL_TITLE = "original_title"
            val MOVIE_GENRE_IDS = "genre_ids"
            val MOVIE_BACKDROP_PATH = "backdrop_path"
            val MOVIE_ADULT = "adult"
            val MOVIE_OVERVIEW = "overview"
            val MOVIE_RELEASE_DATE = "release_date"

            val parsedMovieData = ArrayList<MovieItem>()
            val movieDetailsJson = JSONObject(movieDetailsJsonStr)
            val movieArray = movieDetailsJson.getJSONArray(MOVIE_LIST)

            for (i in 0 until movieArray.length()) {
                val movie = movieArray.getJSONObject(i)
                val voteCount = movie.getInt(MOVIE_VOTE_COUNT)
                val id = movie.getInt(MOVIE_ID)
                val video = movie.getBoolean(MOVIE_VIDEO)
                val voteAvg = movie.getDouble(MOVIE_VOTE_AVERAGE)
                val title = movie.getString(MOVIE_TITLE)
                val popularity = movie.getDouble(MOVIE_POPULARITY)
                val posterPath = movie.getString(MOVIE_POSTER_PATH)
                val language = movie.getString(MOVIE_ORIGINAL_LANG)
                val originalTitle = movie.getString(MOVIE_ORIGINAL_TITLE)
                val genreID = movie.getString(MOVIE_GENRE_IDS)
                val backdropPath = movie.getString(MOVIE_BACKDROP_PATH)
                val adult = movie.getBoolean(MOVIE_ADULT)
                val overview = movie.getString(MOVIE_OVERVIEW)
                val releaseDate = movie.getString(MOVIE_RELEASE_DATE)

                parsedMovieData.add(MovieItem(posterPath, adult, overview, releaseDate, genreID, id, originalTitle, language, title, backdropPath, popularity, voteCount, video, voteAvg))

            }
            return parsedMovieData
        }


    }

}