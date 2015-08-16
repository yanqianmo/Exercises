
/**
 * * @(#)DetailFragment.java, 2015Äê8ÔÂ16ÈÕ. * * Copyright 2015 Yodao, Inc. All
 * rights reserved. * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms.
 */
package com.example.fragmentexercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/** * * @author ibm * * */
public class DetailFragment extends Fragment {

    String mContactId = null;

    public DetailFragment(String contactId) {
        Log.i(this.toString(), "contactId = " + contactId);
        mContactId = contactId != null ? contactId : null;
    }

    /*
     * (non-Javadoc) * @see
     * android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        View view = inflater.inflate(R.layout.detail_fragment, container,false);
        
        if (mContactId != null) {
            // TODO Auto-generated method stub
            // super.onActivityCreated(savedInstanceState);
            ListView detailListView = (ListView) view
                    .findViewById(R.id.detail_list_view);
            String selectionStr = " contact_id = ?";
            Cursor cursor = getActivity().getContentResolver().query(
                    CommonDataKinds.Phone.CONTENT_URI, null, selectionStr,
                    new String[] {
                        mContactId
                    }, " contact_id asc");
            if (cursor != null && cursor.moveToFirst()) {
                List<String> listData = new ArrayList<String>();
                listData.add("contact_id:"
                        + cursor.getString(cursor
                                .getColumnIndex(Phone.CONTACT_ID)));
                listData.add("name:"
                        + cursor.getString(cursor
                                .getColumnIndex(Phone.DISPLAY_NAME)));
                listData.add("phoneNumber:"
                        + cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
                detailListView.setAdapter(new ArrayAdapter<String>(
                        getActivity(), android.R.layout.test_list_item,
                        listData));
            }
        }
        return view;
    }

}
