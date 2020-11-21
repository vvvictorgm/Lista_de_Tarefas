package com.barbaburguer.listadetarefas.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.barbaburguer.listadetarefas.Model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements ITarefaDAO {

    private SQLiteDatabase escreve, le;

    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        try {
            escreve.insert(DbHelper.TABELA_TAREFAS, null, cv);
            Log.i("INFO DB", "Tarefa salva com sucesso");
        }catch (Exception e){
            Log.i("INFO DB", "Erro ao salvar tarefa" + e.getMessage());
            return false;


        }
        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        try {
            String[]args ={tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv, "id=?", args);
            Log.i("INFO DB", "Tarefa atualizada com sucesso");
        }catch (Exception e){
            Log.i("INFO DB", "Erro ao atualizar a tarefa" + e.getMessage());
            return false;


        }
        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        try {
            String[]args ={tarefa.getId().toString()};

            escreve.delete(DbHelper.TABELA_TAREFAS, "id=?", args);
            Log.i("INFO DB", "Tarefa removida com sucesso");
        }catch (Exception e){
            Log.i("INFO DB", "Erro ao remover a tarefa" + e.getMessage());
            return false;


        }

        return true;
    }

    @Override
    public List<Tarefa> listar() {
        List<Tarefa>tarefas = new ArrayList<>();

        String sql = "SELECT *FROM " + DbHelper.TABELA_TAREFAS +" ;";
        Cursor c = le.rawQuery(sql, null);

        while(c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);

            tarefas.add( tarefa);

        }

        return tarefas;
    }
}
