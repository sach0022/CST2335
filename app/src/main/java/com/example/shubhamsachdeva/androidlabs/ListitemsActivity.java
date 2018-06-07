package com.example.shubhamsachdeva.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListitemsActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final String ACTIVITY_NAME = "StartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME,"IN onCreate(): ");
        ImageButton myButton = (ImageButton) findViewById(R.id.imageButton);
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    CharSequence text = "Switch is On";// "Switch is Off"
                    int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off

                    Toast toast = Toast.makeText(ListitemsActivity.this , text, duration); //this is the ListActivity
                    toast.show();
                }


                else if(!isChecked){
                    CharSequence text = "Switch is off";// "Switch is Off"
                    int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off

                    Toast toast = Toast.makeText(ListitemsActivity.this , text, duration); //this is the ListActivity
                    toast.show();

                }
            }
        });
        final CheckBox checkBox=(CheckBox) findViewById(R.id.checkBox) ;

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if(check){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListitemsActivity.this);
// 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Are you want to exit ??") //Add a dialog message to strings.xml

                            .setTitle("Confirm to exit ")
                            .setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent resultIntent = new Intent(  );
                                    resultIntent.putExtra("Response", "Here is my response");
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();

// User clicked OK button
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent resultIntent = new Intent(  );
                                    resultIntent.putExtra("Response", "Here is my response");
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    checkBox.setChecked(false);
// User cancelled the dialog
                                }
                            })
                            .show();


                }

            }
        });
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 60);
                }
           }
           } );
           }


    public void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 60 && resultCode==RESULT_OK)
        {

            Bundle ex=data.getExtras();
            Bitmap img =(Bitmap) ex.get("data");
            ImageButton myButn = (ImageButton) findViewById(R.id.imageButton);
             myButn.setImageBitmap(img);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(ACTIVITY_NAME, "in onResume() : ");

    }
    @Override
    public void onStart()
    {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart() : ");


    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause() : ");


    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.i(ACTIVITY_NAME, " onStop() : ");


    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, " onDestroy() : ");


    }
}
