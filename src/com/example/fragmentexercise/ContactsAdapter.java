/**
 * * @(#)ContactsAdapter.java, 2015年8月16日. * * Copyright 2015 Yodao, Inc. All
 * rights reserved. * YODAO PROPRIETARY/CONFIDENTIAL. Use is subject to license
 * terms.
 */
package com.example.fragmentexercise;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

/** * * @author ibm * * */
public class ContactsAdapter extends ResourceCursorAdapter {

    private Context mContext;

    private int mContactIdIndex = -1;

    private int mLookupKeyIndex = -1;

    private int mPhotoThumbailUriIndex = -1;

    private int mDisplayNameIndex = -1;

    private List<Long> contactIdList = new ArrayList<Long>();

    final static class ContactListItemCache {
        public TextView nameView;

        public QuickContactBadge photoView;

        public CharArrayBuffer nameBuffer = new CharArrayBuffer(128);
    }

    /**
     * * @param context /** * @param layout /** * @param c
     */
    @SuppressWarnings("deprecation")
    public ContactsAdapter(Context context, int layout, Cursor c) {

        super(context, layout, c); // TODO Auto-generated constructor stub
        contactIdList.clear();
        this.mContext = context;
        mContactIdIndex = c.getColumnIndex(Contacts._ID);
        mLookupKeyIndex = c.getColumnIndex(Contacts.LOOKUP_KEY);
        mDisplayNameIndex = c.getColumnIndex(Contacts.DISPLAY_NAME);
        mPhotoThumbailUriIndex = c.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI);
    }

    /*
     * (non-Javadoc) * @see
     * android.support.v4.widget.ResourceCursorAdapter#newView
     * (android.content.Context, android.database.Cursor,
     * android.view.ViewGroup)
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // TODO Auto-generated method stub return super.newView(context, cursor,
        // parent);
        View view = super.newView(context, cursor, parent);

        ContactListItemCache cache = new ContactListItemCache();
        cache.nameView = (TextView) view.findViewById(R.id.name);
        cache.photoView = (QuickContactBadge) view
                .findViewById(R.id.quickcontact);

        view.setTag(cache);
        return view;
    }

    /*
     * (non-Javadoc) * @see
     * android.support.v4.widget.CursorAdapter#bindView(android.view.View,
     * android.content.Context, android.database.Cursor)
     */
    @Override
    public void bindView(View arg0, Context arg1, Cursor arg2) {

        // TODO Auto-generated method stub
        final ContactListItemCache cache = (ContactListItemCache) arg0.getTag();

        arg2.copyStringToBuffer(mDisplayNameIndex, cache.nameBuffer);
        int size = cache.nameBuffer.sizeCopied;
        cache.nameView.setText(cache.nameBuffer.data, 0, size);

        final long contactId = arg2.getLong(mContactIdIndex);
        contactIdList.add(contactId);

        final String lookupKey = arg2.getString(mLookupKeyIndex);

        cache.photoView.assignContactUri(Contacts.getLookupUri(contactId,
                lookupKey));

        android.util.Log.e("PhotoData",
                "photoData=" + arg2.getString(mPhotoThumbailUriIndex));

        //判断是否有头像
         if (arg2.getLong(arg2.getColumnIndex(Contacts.PHOTO_ID))>0) {
            String photoData = arg2.getString(mPhotoThumbailUriIndex);
            Bitmap thumbnailBitmap = loadContactPhotoThumbnail(photoData);
            cache.photoView.setImageBitmap(thumbnailBitmap);
        }
         
    }

    /**
     * Load a contact photo thumbnail and return it as a Bitmap, resizing the
     * image to the provided image dimensions as needed.
     * 
     * @param photoData
     *            photo ID Prior to Honeycomb, the contact's _ID value. For
     *            Honeycomb and later, the value of PHOTO_THUMBNAIL_URI.
     * @return A thumbnail Bitmap, sized to the provided width and height.
     *         Returns null if the thumbnail is not found.
     */
    private Bitmap loadContactPhotoThumbnail(String photoData) {
        // Creates an asset file descriptor for the thumbnail file.
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri;
            // If Android 3.0 or later
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Sets the URI from the incoming PHOTO_THUMBNAIL_URI
                thumbUri = Uri.parse(photoData);
            } else {
                // Prior to Android 3.0, constructs a photo Uri using _ID
                /*
                 * Creates a contact URI from the Contacts content URI incoming
                 * photoData (_ID)
                 */
                final Uri contactUri = Uri.withAppendedPath(
                        Contacts.CONTENT_URI, photoData);
                /*
                 * Creates a photo URI by appending the content URI of
                 * Contacts.Photo.
                 */
                thumbUri = Uri.withAppendedPath(contactUri,
                        Photo.CONTENT_DIRECTORY);
            }

            /*
             * Retrieves an AssetFileDescriptor object for the thumbnail URI
             * using ContentResolver.openAssetFileDescriptor
             */
            afd = mContext.getContentResolver().openAssetFileDescriptor(
                    thumbUri, "r");
            /*
             * Gets a file descriptor from the asset file descriptor. This
             * object can be used across processes.
             */
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            // Decode the photo file and return the result as a Bitmap
            // If the file descriptor is valid
            if (fileDescriptor != null) {
                // Decodes the bitmap
                return BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
                        null);
            }
            // If the file isn't found
        } catch (FileNotFoundException e) {
            /*
             * Handle file not found errors
             */
        }
        // In all cases, close the asset file descriptor
        finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<Long> getContactIdList() {
        return this.contactIdList;
    }

}
