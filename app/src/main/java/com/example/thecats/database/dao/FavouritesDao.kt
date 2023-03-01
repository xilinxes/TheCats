package com.example.thecats.database.dao

import androidx.room.*
import com.example.thecats.database.entity.FavouritesEntity
import com.example.thecats.database.entity.FavouritesEntity.Companion.COLUMN_TIMESTAMP
import com.example.thecats.database.entity.FavouritesEntity.Companion.TABLE_NAME
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: FavouritesEntity)

    @Query(
        "SELECT * FROM $TABLE_NAME" +
                " ORDER BY $COLUMN_TIMESTAMP DESC"
    )
    fun getAllFavourites(): Flowable<List<FavouritesEntity>>
}