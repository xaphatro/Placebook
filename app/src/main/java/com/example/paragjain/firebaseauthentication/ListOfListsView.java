package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by paragjain on 11/10/17.
 */

public class ListOfListsView extends Activity {

    private TaskHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private EditText listEditText;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_list_of_lists_view);

        mHelper = new TaskHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);


    }

    public void goToItems(View v) {
        Intent intent = new Intent(this, ListOfItemsView.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_task:
                createDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createDialog() {
        listEditText = new EditText(this);
        listEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Task")
                .setMessage("Add a new task")
                .setView(listEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogue, int which) {
                        String listName = String.valueOf(listEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
