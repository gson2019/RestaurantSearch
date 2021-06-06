package com.alltrails.restaurantsearch.ui

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object RxRestaurantSearch {
    fun fromSearchView(searchView : SearchView) : Observable<String> {
        val subject: BehaviorSubject<String> = BehaviorSubject.create()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    subject.onNext(newText)
                }
                return true
            }
        })

        return subject
    }
}