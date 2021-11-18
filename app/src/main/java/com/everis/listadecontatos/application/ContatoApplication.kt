package com.everis.listadecontatos.application

import android.app.Application
import android.widget.HeterogeneousExpandableList
import com.everis.listadecontatos.feature.helpers.HelperDB

class ContatoApplication : Application () {

    var helperDB :HelperDB? = null
        private set
    // com este private set, só esta classe pode instanciar este objeto,
    // impedindo que outras classes o façam e sejam forçadas a usarem
    // este objeto

    companion object {
        lateinit var instance : ContatoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }

}