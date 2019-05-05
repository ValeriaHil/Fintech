package com.example.lenovo.myproject.events

import com.example.lenovo.myproject.api.Event
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView

interface EventsView: MvpLceView<List<Event>> {
}