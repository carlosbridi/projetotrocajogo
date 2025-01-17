package com.ce2apk.projetotrocajogo.Jogo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ce2apk.projetotrocajogo.Helper.PersistenceHelper;
import com.ce2apk.projetotrocajogo.Jogo.Plataforma.Plataforma;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlosbridi on 19/12/15.
 */
public class TempJogoBuscaCRUD {

    private SQLiteDatabase db;
    private PersistenceHelper banco;

    public TempJogoBuscaCRUD(Context context){
        banco = new PersistenceHelper(context);
    }

    public long inserirJogoColecao(List<JogoBuscaUsuario> listaJogo){
        int xQuantidadeInclusao = 0;
        for (JogoBuscaUsuario jogoColecaoUsuarios : listaJogo) {
            xQuantidadeInclusao += inserirJogoColecao(jogoColecaoUsuarios);
        }
        return xQuantidadeInclusao;
    }

    public long inserirJogoColecao(JogoBuscaUsuario itensJogoTroca){
        ContentValues contentValues;
        long resultado;

        db = banco.getWritableDatabase();
        db.beginTransaction();
        try {
            contentValues = new ContentValues();
            contentValues.put("id", itensJogoTroca.getId());
            contentValues.put("idjogotroca", itensJogoTroca.getId());
            contentValues.put("idusuariotroca", itensJogoTroca.getIdUsuarioTroca());
            contentValues.put("nomeusuario", itensJogoTroca.getNomeUsuarioTroca());
            contentValues.put("nomejogo", itensJogoTroca.getNomejogo());
            contentValues.put("descricao", itensJogoTroca.getDescricao());
            contentValues.put("categoria", itensJogoTroca.getCategoria());
            contentValues.put("plataforma", itensJogoTroca.getPlataforma().getId());
            contentValues.put("idjogoplataforma", itensJogoTroca.getIdJogoPlataforma());
            contentValues.put("ano", itensJogoTroca.getAno());
            contentValues.put("imagem", itensJogoTroca.getImagem());
            resultado = db.insert("temp_jogobuscausuarios", null, contentValues);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
        db.close();

        return resultado;
    }

    public long removerTodaColecao(){
        long resultado;

        String where = "id > 0";

        db = banco.getReadableDatabase();
        db.beginTransaction();
        try {
            resultado = db.delete("temp_jogobuscausuarios", where, null);
            db.setTransactionSuccessful();
        }catch (Exception e){
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
        db.close();

        return resultado;
    }

    public List<JogoBuscaUsuario> listarJogo() {

        String sql = "select * from TEMP_JOGOBUSCAUSUARIOS";
        List<JogoBuscaUsuario> listaJogos = new ArrayList<JogoBuscaUsuario>();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()){
                JogoBuscaUsuario itensJogoTroca = new JogoBuscaUsuario();
                itensJogoTroca.setId(cursor.getInt(cursor.getColumnIndex("id")));
                itensJogoTroca.setId(cursor.getInt(cursor.getColumnIndex("idjogotroca")));
                itensJogoTroca.setNomeUsuarioTroca(cursor.getString(cursor.getColumnIndex("nomeusuario")));
                itensJogoTroca.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                itensJogoTroca.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                itensJogoTroca.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                itensJogoTroca.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                itensJogoTroca.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                itensJogoTroca.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                itensJogoTroca.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                listaJogos.add(itensJogoTroca);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return listaJogos;
    }

    public JogoBuscaUsuario obterDadosJogo(int idJogo){

        String sql = "select * from temp_jogobuscausuarios where id = " + idJogo ;
        JogoBuscaUsuario jogo = new JogoBuscaUsuario();

        try{
            db = banco.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToNext()) {
                jogo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogo.setId(cursor.getInt(cursor.getColumnIndex("idjogotroca")));
                jogo.setNomeUsuarioTroca(cursor.getString(cursor.getColumnIndex("nomeusuario")));
                jogo.setIdUsuarioTroca(cursor.getInt(cursor.getColumnIndex("idusuariotroca")));
                jogo.setNomejogo(cursor.getString(cursor.getColumnIndex("nomejogo")));
                jogo.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
                jogo.setPlataforma(new Plataforma(cursor.getInt(cursor.getColumnIndex("plataforma"))));
                jogo.setIdJogoPlataforma(cursor.getInt(cursor.getColumnIndex("idjogoplataforma")));
                jogo.setCategoria(cursor.getInt(cursor.getColumnIndex("categoria")));
                jogo.setAno(cursor.getInt(cursor.getColumnIndex("ano")));
                jogo.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return jogo;

    }
}