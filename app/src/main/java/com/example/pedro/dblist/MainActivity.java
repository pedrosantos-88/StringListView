package com.example.pedro.dblist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.net.IDN;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private EditText editText;
    private ListView listView;
    private SQLiteDatabase database;
    private ArrayAdapter<String> ItemsAdapter;
    private ArrayList<String> items;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mBtn = findViewById(R.id.Button);
            editText = findViewById(R.id.EditText);

            database = openOrCreateDatabase("db", MODE_PRIVATE, null);
            listView = findViewById(R.id.listView);

            database.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR )  ");
            mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = editText.getText().toString();
                    SalvarTarefa(textoDigitado);
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    position = ids.get(position);
                    removerTarefa(position);
                    ItemsAdapter.notifyDataSetChanged();
                    return false;
                }
            });

        recuperarTarefas();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SalvarTarefa(String texto) {


        try {

            if (texto.equals("")) {
                Toast.makeText(MainActivity.this, "Please insert Tarefa", Toast.LENGTH_SHORT).show();
            } else {
                database.execSQL("INSERT INTO tarefas(tarefa) VALUES ('" + texto + "')");
                Toast.makeText(MainActivity.this, "Inserted Tarefa", Toast.LENGTH_SHORT).show();
                recuperarTarefas();
                editText.setText("");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recuperarTarefas() {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM tarefas ORDER BY id DESC ", null);
            int idColumnID = cursor.getColumnIndex("id");
            int idColumTarefa = cursor.getColumnIndex("tarefa");

            //Criar Adapter
            items = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            ItemsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, items);

            listView.setAdapter(ItemsAdapter);


            //listar dos dados inseridos na db as tarefas neste caso um char
            cursor.moveToFirst();

            while (cursor != null) {

                Log.i("inserido : ", "tarefa : " + cursor.getString(idColumTarefa));
                items.add(cursor.getString(idColumTarefa));
                ids.add(Integer.valueOf(cursor.getString(idColumnID)));


                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void removerTarefa(Integer id) {

        try {
            database.execSQL("DELETE FROM tarefas WHERE id=" + id);
            recuperarTarefas();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
