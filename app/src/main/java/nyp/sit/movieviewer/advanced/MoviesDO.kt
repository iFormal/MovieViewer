package nyp.sit.movieviewer.advanced

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import nyp.sit.movieviewer.advanced.entity.MovieItem
import java.util.*

@DynamoDBTable(tableName = "UserData")
class MoviesDO {

    @DynamoDBHashKey(attributeName = "id")
    var id: String? = null

    @DynamoDBAttribute(attributeName = "favMovie")
    var favMovie: List<MovieItem>? = null

}