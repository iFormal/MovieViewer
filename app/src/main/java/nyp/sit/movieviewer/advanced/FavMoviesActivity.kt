package nyp.sit.movieviewer.advanced

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import kotlinx.coroutines.*
import nyp.sit.movieviewer.advanced.databinding.ActivityFavMoviesBinding
import nyp.sit.movieviewer.advanced.entity.MovieItem
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FavMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavMoviesBinding
    var activityCoroutineScope: CoroutineScope? = null

    var dynamoDBMapper: DynamoDBMapper? = null

    var currentMoviesDO: MoviesDO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_movies)

        binding = ActivityFavMoviesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        activityCoroutineScope?.launch() {

            try {
                var credentials: AWSCredentials = AWSMobileClient.getInstance().awsCredentials
                var dynamoDBClient = AmazonDynamoDBClient(credentials)

                dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(
                        AWSMobileClient.getInstance().configuration
                    )
                    .build()

                runRefreshMovies(view)

            } catch (ex: Exception) {
                Log.d("advanceLog", "Exception ${ex.message}")
            }
        }
    }

    fun runAddMovie(v: View) {

        activityCoroutineScope?.launch() {
            dynamoDBMapper?.save(currentMoviesDO)
        }


    }

    fun runRefreshMovies(v: View) {

        activityCoroutineScope?.launch() {
            var eav = HashMap<String, AttributeValue>()
            eav.put(":val1", AttributeValue().withS(AWSMobileClient.getInstance().username))
            var queryExpression =
                DynamoDBScanExpression().withFilterExpression("id = :val1")
                    .withExpressionAttributeValues(eav)

            var itemList =
                dynamoDBMapper?.scan(MoviesDO::class.java, queryExpression)

            if (itemList?.size != 0 && itemList != null) {
                for (i in itemList!!.iterator()) {
                    currentMoviesDO = i

                }

                var str = ""

                for (movie in currentMoviesDO?.favMovie!!.iterator()) {

                    str = str.plus("${movie.title}")
                }

                withContext(Dispatchers.Main) {
                    binding.retrievedMovies.text = str
                }
            }
            else {

                currentMoviesDO = MoviesDO()

                currentMoviesDO?.apply {

                    id = AWSMobileClient.getInstance().username



                }
            }
        }
    }
}