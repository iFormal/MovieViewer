package nyp.sit.movieviewer.advanced

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val movieImageFromMain = intent.getStringExtra("movieImage")
        val movieOverviewFromMain = intent.getStringExtra("movieOverview")
        val movieReleaseDateFromMain = intent.getStringExtra("movieReleaseDate")
        val moviePopularityFromMain = intent.getStringExtra("moviePopularity")
        val movieVoteCountFromMain = intent.getStringExtra("movieVoteCount")
        val movieVoteAvgFromMain = intent.getStringExtra("movieVoteAvg")
        val movieLanguageFromMain = intent.getStringExtra("movieLanguage")
        val movieAdultFromMain = intent.getStringExtra("movieAdult")
        val movieVideoFromMain = intent.getStringExtra("movieVideo")

        var image = NetworkUtils.buildImageUrl(movieImageFromMain)
        Picasso.get().load("$image").into(posterIV)
        movie_overview.text = movieOverviewFromMain
        movie_release_date.text = movieReleaseDateFromMain
        movie_popularity.text = moviePopularityFromMain
        movie_vote_count.text = movieVoteCountFromMain
        movie_vote_avg.text = movieVoteAvgFromMain
        movie_langauge.text = movieLanguageFromMain
        movie_is_adult.text = movieAdultFromMain
        movie_hasvideo.text = movieVideoFromMain
    }

}