package com.example.lenovo.myproject.events

import com.example.lenovo.myproject.App
import com.example.lenovo.myproject.SPHandler
import com.example.lenovo.myproject.api.Event
import com.example.lenovo.myproject.api.TinkoffApi
import io.reactivex.Observable
import javax.inject.Inject

class EventsRepository {

    @Inject
    lateinit var network: TinkoffApi

    init {
        App.instance.getRepositoryComponent().inject(this)
    }


    fun getActiveEvents(): Observable<List<Event>> {
        return network.events(SPHandler.getCookie())
            .map { response -> response.active}
    }
}