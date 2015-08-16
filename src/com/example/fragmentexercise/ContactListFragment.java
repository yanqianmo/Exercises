/**
 * * @(#)ContactListFragment.java, 2015Äê8ÔÂ16ÈÕ. * * Copyright 2015 Yodao, Inc.
 * All rights reserved. * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to
 * license terms.
 */
package com.example.fragmentexercise;

import java.util.ResourceBundle.Control;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

/** * * @author ibm * * */
public class ContactListFragment extends Fragment {
    ListView contactListView;
    Cursor cursor = null;
//    SimpleCursorAdapter sca;
    ContactsAdapter sca;

    private ContactListOnItemClickCallBack mContactListOnItemClickCallBack;

    public void setContactListOnItemClickBack(ContactListOnItemClickCallBack cb) {
        this.mContactListOnItemClickCallBack = cb;
    }

    public interface ContactListOnItemClickCallBack {
        void onContactListItemClick(String contactId);
    }

    /*
     * (non-Javadoc) * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // TODO Auto-generated method stub return super.onCreateView(inflater,
        // container, savedInstanceState);
        View view = inflater.inflate(R.layout.contact_list_fragment, container,
                false);
        
        contactListView = (ListView) view.findViewById(
                R.id.contact_list);

       /* cursor = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {
                    Contacts._ID, Contacts.DISPLAY_NAME,
                    Contacts.PHOTO_THUMBNAIL_URI
                }, null, null, " _id asc");
        
        sca = new SimpleCursorAdapter(getActivity(), R.layout.row, cursor,
                new String[] {
                    // Contacts.PHOTO_THUMBNAIL_URI,
                    ContactsContract.Data.DISPLAY_NAME
                }, new int[] {
                    // R.id.thumbnail,
                    R.id.name
                });
        contactListView.setAdapter(sca);*/
        
        //using QuickContactBadge show the thumbnail
        
        cursor = getActivity().getContentResolver().query(
                Contacts.CONTENT_URI,
                new String[] {
                    Contacts._ID, Contacts.LOOKUP_KEY,
                    Contacts.DISPLAY_NAME, 
                    Contacts.PHOTO_THUMBNAIL_URI,Contacts.PHOTO_ID
//                    (Build.VERSION.SDK_INT >=
//                    Build.VERSION_CODES.HONEYCOMB) ?
//                           Contacts.DISPLAY_NAME_PRIMARY :
//                           Contacts.DISPLAY_NAME,
//                   (Build.VERSION.SDK_INT >=
//                    Build.VERSION_CODES.HONEYCOMB) ?
//                           Contacts.PHOTO_THUMBNAIL_URI :
//                               Contacts._ID
                           /*
                            * Although it's not necessary to include the
                            * column twice, this keeps the number of
                            * columns the same regardless of version
                            */

                }, null, null, " _id asc");
        
        sca = new ContactsAdapter(getActivity(), R.layout.row, cursor);
        contactListView.setAdapter(sca);

        contactListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                // TODO Auto-generated method stub 
                Cursor c = (Cursor) sca.getItem(position);
                String contactId = c.getString(c.getColumnIndex(Contacts._ID));
                if (mContactListOnItemClickCallBack != null) {
                    mContactListOnItemClickCallBack
                            .onContactListItemClick(contactId);
                }
                
            }});
       
        return view;
    }

}
