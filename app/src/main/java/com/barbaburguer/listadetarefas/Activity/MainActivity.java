package com.barbaburguer.listadetarefas.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.barbaburguer.listadetarefas.Adapter.TarefaAdapter;
import com.barbaburguer.listadetarefas.Helper.DbHelper;
import com.barbaburguer.listadetarefas.Helper.RecyclerItemClickListener;
import com.barbaburguer.listadetarefas.Helper.TarefaDAO;
import com.barbaburguer.listadetarefas.Model.Tarefa;
import com.barbaburguer.listadetarefas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);


        //Adicionar o evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //recuperar tarefa para ediçaõ
                        Tarefa tarefaSelecionada = listaTarefas.get(position);
                        //enviar a tarefa selecionada para a proxima activity
                        Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                        startActivity( intent);


                        
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //recuperar a tarefa que o usuário quer deletar
                        tarefaSelecionada = listaTarefas.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                        //Configurar o titulo e mensagem
                        dialog.setTitle("Confirmar Exclusão");
                        dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() +"?");

                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                if( tarefaDAO.deletar(tarefaSelecionada)){
                                    carregarListaTaregas();
                                    Toast.makeText(getApplicationContext(), "Sucesso ao excluir tarefa", Toast.LENGTH_SHORT).show();


                                }else{
                                    Toast.makeText(getApplicationContext(), "Erro ao excluir tarefa", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        dialog.setNegativeButton("Não", null);
                        //exibir Dialog
                        dialog.create();
                        dialog.show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }
    public void carregarListaTaregas(){
        //listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        //configurar adapter
                tarefaAdapter = new TarefaAdapter(listaTarefas);
        //configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);
    }

    protected void onStart(){
        carregarListaTaregas();
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
