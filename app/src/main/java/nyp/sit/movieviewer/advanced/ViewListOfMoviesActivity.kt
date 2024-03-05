package nyp.sit.movieviewer.advanced

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_list_of_movies.*
import kotlinx.coroutines.*
import nyp.sit.movieviewer.advanced.entity.Movies
import java.lang.Exception

class ViewListOfMoviesActivity : AppCompatActivity() {

    var moviesAdapter : CustListAdapter ?= null

    private val moviesViewModel: MoviesViewModel by viewModels() {

        MoviesViewModelFactory((application as MyMovies).repo)
    }

    val SHOW_BY_TOP_RATED = 1
    val SHOW_BY_POPULAR = 2
    private var displayType = SHOW_BY_TOP_RATED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)
    }

    override fun onStart() {
        super.onStart()
        loadMovieData(displayType)
    }

    fun loadMovieData(viewType: Int) {
        var showTypeStr: String? = null
        when (viewType) {
            SHOW_BY_TOP_RATED -> showTypeStr = NetworkUtils.TOP_RATED_PARAM
            SHOW_BY_POPULAR -> showTypeStr = NetworkUtils.POPULAR_PARAM
        }
        if (showTypeStr != null) {
            displayType = viewType
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.sortPopular -> {
                loadMovieData(SHOW_BY_POPULAR)
                clearDatabase()
                loadMoviePopular()
            }
            R.id.sortTopRated -> {
                loadMovieData(SHOW_BY_TOP_RATED)
                clearDatabase()
                loadMovieRating()
            }
            R.id.viewFav -> {
                var i = Intent(this, FavMoviesActivity::class.java)
                startActivity(i)
            }
            R.id.signOut -> {
                AWSMobileClient.getInstance()
                    .signOut(SignOutOptions.builder().signOutGlobally(false).build(),

                        object : Callback<Void> {
                            override fun onResult(result: Void?) {
                                val i = Intent(applicationContext, LoginActivity::class.java)

                                startActivity(i)

                                finish()
                            }

                            override fun onError(e: Exception?) {

                            }
                        })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadMovieRating() {
        val movieList = ArrayList<String>()
        val imageList = ArrayList<String>()
        val query = "/top_rated"
        val apikey = "651cad9bc60b319fd14cb0f36091dbc3"

        var movieJob = CoroutineScope(Job() + Dispatchers.IO).async() {

            val movieRequestUrl = NetworkUtils.buildUrl(query, apikey)

            try {
                val jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl!!)

                val responseList = movieDBJsonUtils
                    .getMovieDetailsFromJson(this@ViewListOfMoviesActivity, jsonMovieResponse.toString())

                responseList
            } catch (e: Exception) {
                e.printStackTrace()

                null
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            val movieData = movieJob.await()
            if (movieData != null) {

                for (movieEntry in movieData) {
                    val posterPath = movieEntry.poster_path.toString()
                    val adult = movieEntry.adult!!
                    val overview = movieEntry.overview.toString()
                    val release_date = movieEntry.release_date.toString()
                    val genre_ids = movieEntry.genre_ids.toString()
                    val original_title = movieEntry.original_title.toString()
                    val original_language = movieEntry.original_langauge.toString()
                    val title = movieEntry.title.toString()
                    val backdrop_path = movieEntry.backdrop_path.toString()
                    val popularity = movieEntry.popularity
                    val vote_count = movieEntry.vote_count
                    val video = movieEntry.video!!
                    val vote_average = movieEntry.vote_average
                    movieList.add(original_title)
                    val image = NetworkUtils.buildImageUrl(posterPath)
                    imageList.add(image.toString())
                    moviesViewModel.insert(Movies(0, posterPath, adult, overview, release_date, genre_ids, original_title,
                        original_language, title, backdrop_path, popularity, vote_count, video, vote_average))
                }
                moviesAdapter = CustListAdapter(this@ViewListOfMoviesActivity, movieList, imageList)
                movielist.adapter = moviesAdapter
                movielist.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val movieImage = movieData[position].poster_path.toString()
                        val movieOverview = movieData[position].overview.toString()
                        val movieReleaseDate = movieData[position].release_date.toString()
                        val moviePopularity = movieData[position].popularity.toString()
                        val movieVoteCount = movieData[position].vote_count.toString()
                        val movieVoteAvg = movieData[position].vote_average.toString()
                        val movieLanguage = movieData[position].original_langauge.toString()
                        val movieAdult = movieData[position].adult.toString()
                        val movieVideo = movieData[position].video.toString()

                        val myIntent = Intent(this@ViewListOfMoviesActivity, ItemDetailActivity::class.java)
                        myIntent.putExtra("movieImage", movieImage)
                        myIntent.putExtra("movieOverview", movieOverview)
                        myIntent.putExtra("movieReleaseDate", movieReleaseDate)
                        myIntent.putExtra("moviePopularity", moviePopularity)
                        myIntent.putExtra("movieVoteCount", movieVoteCount)
                        myIntent.putExtra("movieVoteAvg", movieVoteAvg)
                        myIntent.putExtra("movieLanguage", movieLanguage)
                        myIntent.putExtra("movieAdult", movieAdult)
                        myIntent.putExtra("movieVideo", movieVideo)

                        startActivity(myIntent)
                    }
                }
            }
        }
    }

    fun loadMoviePopular() {
        val movieList = ArrayList<String>()
        val imageList = ArrayList<String>()
        val query = "/popular"
        val apikey = "651cad9bc60b319fd14cb0f36091dbc3"

        var movieJob = CoroutineScope(Job() + Dispatchers.IO).async() {

            val movieRequestUrl = NetworkUtils.buildUrl(query, apikey)

            try {
                val jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl!!)

                val responseList = movieDBJsonUtils
                    .getMovieDetailsFromJson(this@ViewListOfMoviesActivity, jsonMovieResponse.toString())

                responseList
            } catch (e: Exception) {
                e.printStackTrace()

                null
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            val movieData = movieJob.await()
            if (movieData != null) {

                for (movieEntry in movieData) {
                    val posterPath = movieEntry.poster_path.toString()
                    val adult = movieEntry.adult!!
                    val overview = movieEntry.overview.toString()
                    val release_date = movieEntry.release_date.toString()
                    val genre_ids = movieEntry.genre_ids.toString()
                    val original_title = movieEntry.original_title.toString()
                    val original_language = movieEntry.original_langauge.toString()
                    val title = movieEntry.title.toString()
                    val backdrop_path = movieEntry.backdrop_path.toString()
                    val popularity = movieEntry.popularity
                    val vote_count = movieEntry.vote_count
                    val video = movieEntry.video!!
                    val vote_average = movieEntry.vote_average
                    movieList.add(title)
                    val image = NetworkUtils.buildImageUrl(posterPath)
                    imageList.add(image.toString())
                    moviesViewModel.insert(
                        Movies(0, posterPath, adult, overview, release_date, genre_ids, original_title,
                        original_language, title, backdrop_path, popularity, vote_count, video, vote_average)
                    )
                }
                moviesAdapter = CustListAdapter(this@ViewListOfMoviesActivity, movieList, imageList)
                movielist.adapter = moviesAdapter
                movielist.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val movieImage = movieData[position].poster_path.toString()
                        val movieOverview = movieData[position].overview.toString()
                        val movieReleaseDate = movieData[position].release_date.toString()
                        val moviePopularity = movieData[position].popularity.toString()
                        val movieVoteCount = movieData[position].vote_count.toString()
                        val movieVoteAvg = movieData[position].vote_average.toString()
                        val movieLanguage = movieData[position].original_langauge.toString()
                        val movieAdult = movieData[position].adult.toString()
                        val movieVideo = movieData[position].video.toString()

                        val myIntent = Intent(this@ViewListOfMoviesActivity, ItemDetailActivity::class.java)
                        myIntent.putExtra("movieImage", movieImage)
                        myIntent.putExtra("movieOverview", movieOverview)
                        myIntent.putExtra("movieReleaseDate", movieReleaseDate)
                        myIntent.putExtra("moviePopularity", moviePopularity)
                        myIntent.putExtra("movieVoteCount", movieVoteCount)
                        myIntent.putExtra("movieVoteAvg", movieVoteAvg)
                        myIntent.putExtra("movieLanguage", movieLanguage)
                        myIntent.putExtra("movieAdult", movieAdult)
                        myIntent.putExtra("movieVideo", movieVideo)

                        startActivity(myIntent)
                    }
                }
            }
        }
    }

    class CustListAdapter(context: Context, data:ArrayList<String>, image:ArrayList<String>):
        BaseAdapter() {

        internal val sList:ArrayList<String> ?= ArrayList<String>()
        internal val iList:ArrayList<String> ?= ArrayList<String>()

        private val mInflater : LayoutInflater

        init {
            this.mInflater = LayoutInflater.from(context)
            sList?.addAll(data)
            iList?.addAll(image)
        }

        override fun getItem(p0: Int): Any? {

            return sList?.get(p0)
        }

        override fun getItemId(p0: Int): Long {

            return 0
        }

        override fun getCount(): Int {
            return if(sList == null) 0 else sList?.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val v : View
            v = this.mInflater.inflate(R.layout.list_item, parent, false)
            val label: TextView = v.findViewById(R.id.movielist)
            label.text = sList?.get(position)
            val iv: ImageView = v.findViewById(R.id.movieImage)
            Picasso.get().load(iList?.get(position)).into(iv)

            return v
        }
    }

    fun clearDatabase() {
        moviesViewModel.deleteAll()
    }

    override fun onResume() {
        clearDatabase()
        loadMovieRating()

        super.onResume()
    }

}

