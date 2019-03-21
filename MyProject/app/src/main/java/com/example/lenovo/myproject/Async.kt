package com.example.lenovo.myproject

import android.os.Handler
import kotlin.random.Random

object Async {
    fun generateScores(count: Int, handler: Handler) {
        Thread(Runnable {
            val res = ArrayList<String>()
            for (x in 0 until count) {
                res.add(Random.nextInt(0, 500).toString())
            }
            val message = handler.obtainMessage(MainActivity.GENERATE_SCORES, res)
            handler.sendMessage(message)
        }).start()
    }
}