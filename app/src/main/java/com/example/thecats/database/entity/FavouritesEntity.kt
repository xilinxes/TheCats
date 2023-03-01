package com.example.thecats.database.entity

import androidx.room.*
import com.example.thecats.model.CatsModel
import com.example.thecats.model.FavouriteModel

@Entity(
    tableName = FavouritesEntity.TABLE_NAME,
    indices = [
        Index(value = [FavouritesEntity.COLUMN_CAT_ID], unique = true)
    ]
)
data class FavouritesEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = COLUMN_CAT_ID)
    val catId: String = "",

    @ColumnInfo(name = COLUMN_PATH)
    val path: String,

    @ColumnInfo(name = COLUMN_TIMESTAMP)
    val timeStamp: Long

) {
    companion object {
        const val TABLE_NAME = "favourites"
        const val COLUMN_ID = "_id"
        const val COLUMN_CAT_ID = "cat_id"
        const val COLUMN_PATH = "path"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}

fun FavouritesEntity.toFavouriteModel() = FavouriteModel(id = id, catId = catId, path = path)