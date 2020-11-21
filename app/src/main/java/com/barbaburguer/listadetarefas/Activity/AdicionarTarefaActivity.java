package com.barbaburguer.listadetarefas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.barbaburguer.listadetarefas.Helper.TarefaDAO;
import com.barbaburguer.listadetarefas.Model.Tarefa;
import com.barbaburguer.listadetarefas.R;
import com.google.android.material.textfield.TextInputEditText;

public class AdicionarTarefaActivity extends AppCompatActivity {
    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        editTarefa = findViewById(R.id.textTarefa);

        //recuperar a tarefa edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar tarefa na caixa de texto
        if(tarefaAtual != null){
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TarefaDAO tarefaDAO= new TarefaDAO(getApplicationContext());

        switch (item.getItemId()) {
            case R.id.itemSalvar:

                if(tarefaAtual != null){//estou editando uma tarefa
                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar banco de dados
                        if(tarefaDAO.atualizar(tarefa)){
                            finish();

                            Toast.makeText(getApplicationContext(), "Sucesso ao atualizar a tarefa", Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao atualizar a tarefa", Toast.LENGTH_SHORT).show();

                        }


                    }
                }else {
                    //se eu não estiver fazendo a edição, então é uma nova tarefa
                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        if(tarefaDAO.salvar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar nova tarefa", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao salvar nova tarefa", Toast.LENGTH_SHORT).show();

                        }

                    }
                }


                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
