package com.ce2apk.projetotrocajogo.Jogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Jogo.Jogo;
import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;
import com.ce2apk.projetotrocajogo.Util.ListaJogos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 26/12/15.
 */
public class JogoInteresseCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;

    public JogoInteresseCRUD(Context context){
        banco = new PersistenceHelper(context);
    }

    public long inserirJogosInteresse(List<Jogo> listaJogos){
        for (Jogo xJogo : listaJogos) {
            inserirJogoInteresse(xJogo);
        }
        return 0;
    }
    
    public long inserirJogoInteresse(Jogo jogo){
        ContentValues contentValues;
        long resultado;

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            contentValues = new ContentValues();
            contentValues.put("id", jogo.getId());
            contentValues.put("nomejogo", jogo.getNomejogo());
            contentValues.put("descricao", jogo.getDescricao());
            contentValues.put("categoria", jogo.getCategoria());
            contentValues.put("plataforma", jogo.getPlataforma().getId());
            contentValues.put("idjogoplataforma", jogo.getIdJogoPlataforma());
            contentValues.put("ano", jogo.getAno());
            contentValues.put("imagem", jogo.getImagem());
            contentValues.put("interesse", true);

            resultado = db.insert("jogousuario", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
        return resultado;
    }


    public void removerJogosInteresse(){
        String where = "1=1 and interesse = 1 ";
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {

            db.delete("jogousuario", where, null);

            db.setTransactionSuccessful();
        }catch(Exception e){
            db.endTransaction();
            throw  e;
        }

        db.endTransaction();
        db.close();
    }


    public long removerInteresse(Jogo jogo){
        long resultado = 1;

        String where = "id = "+ jogo.getId()+ " and plataforma = "+jogo.getPlataforma().getId()+ " and categoria = " + jogo.getCategoria() + "and interesse = 1";
        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {
            resultado = db.delete("jogousuario", where, null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();

        return resultado;
    }

    public List<Jogo> obterListaJogosInteresse(){
        return listarInteresse().getListaJogo();
    }

    public ListaJogos listarInteresse(){
        String sql = "select * from jogousuario where interesse = 1 ORDER BY nomejogo, ID";

        ListaJogos listaJogos = new ListaJogos() ;
        db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            Jogo jogo = new Jogo();
            jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
            jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
            jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
            jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
            jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
            jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
            jogo.setInteresse(true);
            listaJogos.addJogo(jogo);
        }

        return listaJogos;
    }

}
