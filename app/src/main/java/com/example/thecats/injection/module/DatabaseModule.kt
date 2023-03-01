package com.example.thecats.injection.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thecats.database.FavouritesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavouritesDatabase(context: Context): FavouritesDatabase {
        return getDatabase(context, FavouritesDatabase::class.java, "favourites.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun <T : RoomDatabase> getDatabase(
        context: Context,
        databaseClass: Class<T>,
        databaseName: String
    ): RoomDatabase.Builder<T> {
        return Room.databaseBuilder(context, databaseClass, databaseName)
    }
}