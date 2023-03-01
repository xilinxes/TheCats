package com.example.thecats.repositories

import android.content.Context
import com.example.thecats.database.FavouritesDatabase
import com.example.thecats.database.entity.FavouritesEntity
import com.example.thecats.database.entity.toFavouriteModel
import com.example.thecats.model.CatsModel
import com.example.thecats.model.FavouriteModel
import com.example.thecats.network.CatsAPI
import com.example.thecats.network.model.toCatsModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsRepo @Inject constructor(
    private val catApi: CatsAPI,
    private val favouritesDatabase: FavouritesDatabase
) {

    fun loadCats(): Observable<List<CatsModel>>? {
        return catApi
            .getCats()
            .subscribeOn(Schedulers.io())
            .map { list ->
                list.map { cats ->
                    cats.toCatsModel()
                }
            }
    }

    fun addCatToFavourites(catId: String, path: String): Completable {
        return Completable.fromCallable {
            favouritesDatabase.favourites()
                .insert(
                    FavouritesEntity(
                        catId = catId,
                        path = path,
                        timeStamp = System.currentTimeMillis()
                    )
                )
        }.subscribeOn(Schedulers.io())
    }

    fun getAllFavourites(): Observable<List<FavouriteModel>> {
        return favouritesDatabase.favourites().getAllFavourites()
            .toObservable()
            .subscribeOn(Schedulers.io())
            .map { list ->
                list.map {
                    it.toFavouriteModel()
                }
            }
    }
}