package com.everis.listadecontatos.feature.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    //como quem vai ser o responsável pela database é esta classe,
    //colocamos dentro dela o seu nome e versão
    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 1

    }

    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE = "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_NOME TEXT NOT NULL," +
            "$COLUMNS_TELEFONE TEXT NOT NULL" +
            "" +
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, versaoVelha: Int, versaoNova: Int) {
        if (versaoVelha != versaoNova) {
            db?.execSQL(DROP_TABLE)
            onCreate(db)
        }
    }

}