package com.example.lenovo.myproject.api

data class Event(
    val title: String
)

data class Events(
    val active: List<Event>,
    val archive: List<Event>
)