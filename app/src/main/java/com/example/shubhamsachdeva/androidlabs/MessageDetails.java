package com.example.shubhamsachdeva.androidlabs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Bundle b = getIntent().getExtras();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MessageFragment mf = new MessageFragment();
        mf.setArguments(b);
        ft.replace(R.id.emtFrame,mf);
        ft.commit();

}
}
