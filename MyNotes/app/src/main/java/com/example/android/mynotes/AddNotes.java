package com.example.android.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNotes extends AppCompatActivity {
    private ListView nListViewNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        nListViewNotes=(ListView)findViewById(R.id.Listview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent("com.example.android.mynotes.CreateNote");
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_notes, menu);
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

    protected void onResume() {
        super.onResume();
        nListViewNotes.setAdapter(null);

        ArrayList<Note> notes =Utilities.getAllSavednotes(this);

        if(notes==null || notes.size() ==0){
            Toast.makeText(this,"No Notes To Show",Toast.LENGTH_SHORT).show();
        }else{
            Note_Adapter na= new Note_Adapter(this, R.layout.item_note, notes);
            nListViewNotes.setAdapter(na);

            nListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName=((Note)nListViewNotes.getItemAtPosition(position)).getDateTime()+".bin";

                    Intent viewNoteIntent= new Intent(getApplicationContext(),CreateNote.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);

                }
            });

        }

    }
}
