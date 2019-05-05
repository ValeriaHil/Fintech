package com.example.lenovo.myproject.events

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.api.Event
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EventsPresenter : MvpBasePresenter<EventsView>() {
    private val repo = App.instance.eventsRepo

    fun loadEvents() {
        val result = repo.getActiveEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ events: List<Event> -> view?.setData(events) },
                { error -> view?.showError(error, false) })
    }
}