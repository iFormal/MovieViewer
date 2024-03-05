package nyp.sit.movieviewer.advanced

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.advanced.entity.Movies

@Dao
interface MoviesDao {
    @Query("Select * from movies_table")
    fun retrieveAllMovies() : Flow<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(newMovies : Movies)

    @Delete
    fun delete(delMovie : Movies)

    @Query("DELETE FROM movies_table")
    fun deleteAll()
}