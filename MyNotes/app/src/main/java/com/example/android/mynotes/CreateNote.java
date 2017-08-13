package com.example.android.mynotes;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.prompt;

public class CreateNote extends AppCompatActivity {
    EditText nEtTitle;
    EditText nEtContent;
    private String nNoteFileName;
    private Note nLoadedNote;

    private final int REQ_CODE_SPEECH_INPUT=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nEtTitle=(EditText) findViewById(R.id.editTextTitle);
        nEtContent=(EditText) findViewById(R.id.editTextcontent);
        nNoteFileName=getIntent().getStringExtra("NOTE_FILE");
        if(nNoteFileName!=null && !nNoteFileName.isEmpty()) {
            nLoadedNote = Utilities.getNoteByName(this,nNoteFileName);

            if (nLoadedNote != null) {
                nEtTitle.setText(nLoadedNote.getTitle());
                nEtContent.setText(nLoadedNote.getContent());
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput()
    {
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    nEtContent.append(result.get(0));
                }
                break;
            }

            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save_note:
                saveNote();

                break;
            case R.id.action_delete_note:
                deleteNote();

        }
        return true;
    }

    private void saveNote(){
        Note note;
        if(nEtTitle.getText().toString().trim().isEmpty() || nEtContent.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Enter a Title and Content",Toast.LENGTH_SHORT).show();
            return;
        }
        if(nLoadedNote==null) {
            note = new Note(System.currentTimeMillis(),nEtTitle.getText().toString(), nEtContent.getText().toString());
        }else{
            note= new Note(nLoadedNote.getDateTime(), nEtTitle.getText().toString(),nEtContent.getText().toString());
        }
        if(Utilities.saveNote(this,note)){
            Toast.makeText(this, "Note Saved SuccessFully", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "Note Can't be Saved, Please Check you have Enough Space!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    private void deleteNote(){
        if(nLoadedNote==null){
            finish();
        } else{
            AlertDialog.Builder dialog=new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("You are about to delete "+nEtTitle.getText().toString()+"Are you Sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.deleteNote(getApplicationContext(),nLoadedNote.getDateTime()+".bin");
                            Toast.makeText(getApplicationContext(),nEtTitle.getText().toString()+ "is successfully deleted",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No",null)
                    .setCancelable(false);
            dialog.show();
        }
    }
}

