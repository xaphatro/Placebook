package com.example.paragjain.firebaseauthentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import java.util.ArrayList;

import com.example.paragjain.firebaseauthentication.ListController;

/**
 * Created by paragjain on 11/10/17.
 */

public class ListOfListsView extends Activity {

    private TaskHelper mHelper;
    private ListView listOfListsObject;
    private ArrayAdapter<List> listOfListsAdapter;
    private EditText listEditText;
    private StaticDatabaseHelper db;

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_list_of_lists_view);
        db= new StaticDatabaseHelper(this);
        mHelper = new TaskHelper(this);
        listOfListsObject = (ListView) findViewById(R.id.list_list);


    }

    public void goToItems(View v) {
        Intent intent = new Intent(this, ListOfItemsView.class);
        startActivity(intent);
    }

    public void logOut(View v) {
        db.deleteEmail();
        Intent intent = new Intent(this, LoginView.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
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
                .setTitle("New List")
                .setMessage("Add a new list")
                .setView(listEditText)
                .setPositiveButton("Add List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogue, int which) {
                        String listName = String.valueOf(listEditText.getText());
                        /*SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Task.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(Task.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();*/
                        List newList = ListController.createList(listName, "nishantb21@gmail.com");
                        updateUI();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void updateUI() {
        ArrayList<List> listHolder = new ArrayList<>();
        /*SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(Task.TaskEntry.TABLE,
                new String[] {Task.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(Task.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }
        */
        if (listOfListsAdapter == null) {
            listOfListsAdapter = new ArrayAdapter<List>(this, R.layout.item_todo, R.id.task_title, listHolder);
            listOfListsObject.setAdapter(listOfListsAdapter);

        } else {
            listOfListsAdapter.clear();
            listOfListsAdapter.addAll(listHolder);
            listOfListsAdapter.notifyDataSetChanged();
        }

        //cursor.close();
        //db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Task.TaskEntry.TABLE, Task.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {task});
        db.close();
        updateUI();

    }
}
