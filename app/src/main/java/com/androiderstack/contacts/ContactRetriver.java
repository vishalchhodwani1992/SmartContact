package com.androiderstack.contacts;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;

@SuppressWarnings("deprecation")
public class ContactRetriver {
	
	Context context;
	
	public ContactRetriver(Context context)
	{
		this.context = context;
	}
	
	
	public String getContactName(final String phoneNumber) 
	 {  
	        Uri uri, mBaseUri;
	       String[] projection;
	       mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
	       projection = new String[] { Contacts.People.NAME };
	        try
	        {
		        Class<?> c =Class.forName("android.provider.ContactsContract$PhoneLookup");
		        mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
		        projection = new String[] { "display_name" };
	        } 
	        catch (Exception e) {
	    }
	        
	    uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber)); 
	    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null); 

	    String contactName = "";

	    if (cursor.moveToFirst()) 
	    { 
	        contactName = cursor.getString(0);
	    } 

	    cursor.close();
	    cursor = null;

	    return contactName; 
	}

}
