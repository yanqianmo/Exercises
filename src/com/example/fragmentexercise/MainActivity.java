package com.example.fragmentexercise;

import com.example.fragmentexercise.ContactListFragment.ContactListOnItemClickCallBack;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements ContactListOnItemClickCallBack{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ContactListFragment clf = new ContactListFragment();
        clf.setContactListOnItemClickBack(this);
        
        getSupportFragmentManager().beginTransaction().add(R.id.container, clf).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc) * @see com.example.fragmentexercise.ContactListFragment.ContactListOnItemClickCallBack#onContactListItemClick(long) */
    @Override
    public void onContactListItemClick(String contactId) {
        
        // TODO Auto-generated method stub 
        DetailFragment df = new DetailFragment(contactId);
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, df);
        ft.addToBackStack(null);
        ft.commit();
    }
}
