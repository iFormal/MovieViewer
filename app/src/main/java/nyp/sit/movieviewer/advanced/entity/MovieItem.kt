package nyp.sit.movieviewer.advanced.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class MovieItem(var poster_path: String? = null, var adult: Boolean? = null,var overview: String? = null,
                var release_date: String? = null, var genre_ids: String? = null, var id: Int = 0,
                var original_title: String? = null, var original_langauge: String? = null,
                var title: String? = null, var backdrop_path: String? = null, var popularity: Double = 0.0,
                var vote_count: Int = 0, var video: Boolean? = null, var vote_average: Double = 0.0) {

    init{

        this.poster_path = poster_path
        this.adult = adult
        this.overview= overview
        this.release_date= release_date
        this.genre_ids = genre_ids
        this.id = id
        this.original_title = original_title
        this.original_langauge = original_langauge
        this.title = title
        this.backdrop_path = backdrop_path
        this.popularity = popularity
        this.vote_count = vote_count
        this.video = video
        this.vote_average = vote_average

    }
}

@Entity(tableName = "movies_table")
data class Movies(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id : Int,

                  @ColumnInfo(name="poster_path")val posterPath: String,

                  @ColumnInfo(name="adult")val adult: Boolean,

                  @ColumnInfo(name="overview")val overview: String,

                  @ColumnInfo(name="releaseDate")val release_date: String,

                  @ColumnInfo(name="genreID")val genre_ids: String,

                  @ColumnInfo(name="originalTitle")val original_title: String,

                  @ColumnInfo(name="language")val original_language: String,

                  @ColumnInfo(name="title")val title: String,

                  @ColumnInfo(name="backdropPath")val backdropPath: String,

                  @ColumnInfo(name="popularity")val popularity: Double,

                  @ColumnInfo(name="voteCount")val voteCount: Int,

                  @ColumnInfo(name="video")val video: Boolean,

                  @ColumnInfo(name="voteAvg")val voteAvg: Double)