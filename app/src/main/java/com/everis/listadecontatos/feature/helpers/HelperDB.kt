package com.everis.listadecontatos.feature.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO

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
            "$COLUMNS_ID INTEGER NOT NULL, " +
            "$COLUMNS_NOME TEXT NOT NULL, " +
            "$COLUMNS_TELEFONE TEXT NOT NULL, " +
            "" +
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT) " +
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

    fun buscarContatos (busca: String, isBuscaPorID: Boolean = false) : List <ContatosVO> {
        // Note que sempre que se faz uso do banco de dados consideramos a possibilidade
        // de a resposta ser nula
        val db = readableDatabase ?: return mutableListOf <ContatosVO>();
        var lista = mutableListOf <ContatosVO>()
//        //Busca com SQL exposto
//        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE ?"
//        var buscaComPercentual = "%$busca%"
//        var cursor = db.rawQuery(sql, arrayOf(buscaComPercentual)) ?: return mutableListOf <ContatosVO>()

        var where: String? = null
        var argumentosDeBusca: Array<String> = arrayOf()
        if (isBuscaPorID == true) {
            where = "$COLUMNS_ID = ?"
            argumentosDeBusca = arrayOf("$busca")
        }
        else {
            //Busca com SQL implicito
            where = "$COLUMNS_NOME LIKE ?"
            argumentosDeBusca = arrayOf("%$busca%")
        }



        var cursor = db.query(TABLE_NAME, null,where, argumentosDeBusca,null, null, null)

        if (cursor == null) {
            db.close()
            return mutableListOf <ContatosVO>();
        }
        while (cursor.moveToNext()) {
            var contato = ContatosVO (
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
            )
            lista.add(contato)
        }
        db.close()
        return lista;
    }

    fun salvarContato (contato: ContatosVO) {
        val db = writableDatabase ?: return
        //Maneira Preferida de se fazer inserts, SQL visivel
//        val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE) VALUES (?,?)"
//        val array = arrayOf(contato.nome, contato.telefone)
//        db.execSQL(sql,array)

        // Outra maneira de se fazer, SQL suprimido
        var content = ContentValues()
        content.put(COLUMNS_NOME, contato.nome)
        content.put(COLUMNS_TELEFONE, contato.telefone)
        db.insert(TABLE_NAME, null, content)

        db.close()
    }

    fun deletarContato(id: Int){
        val db = writableDatabase ?: return
        val array = arrayOf<String>("$id")
        // SQL implicito
//        val where = "id = ?"
//        db.delete(TABLE_NAME, where, array)
        //SQL explicito
        val sql = "DELETE FROM $TABLE_NAME WHERE id = ?"
        db.execSQL(sql, array)
        db.close()
    }

    fun updateContato(contato: ContatosVO) {
        val db = writableDatabase ?: return

        //SQL Implicito
//        val content = ContentValues()
//        content.put(COLUMNS_NOME, contato.nome)
//        content.put(COLUMNS_TELEFONE, contato.telefone)
//        val where = "id = ?"
//        val array = arrayOf<String>("${contato.id}")
//        db.update(TABLE_NAME,content, where, array)
        // SQL explícito
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_NOME = ?, $COLUMNS_TELEFONE = ? WHERE $COLUMNS_ID = ?"
        val array = arrayOf(contato.nome,contato.telefone,contato.id)
        db.execSQL(sql, array)
        db.close()
    }

}