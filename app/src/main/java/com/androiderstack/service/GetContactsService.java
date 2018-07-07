package com.androiderstack.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.androiderstack.item.AllContacts;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.utility.Utils;

import java.util.ArrayList;

/**
 * Created by vishalchhodwani on 5/1/17.
 */
public class GetContactsService extends IntentService {

    private final String TAG = "GetContactsService";

    public static final String CONTACT_ACTION = "com.scantity.fragment.";

    public GetContactsService()
    {
        super("GetContactsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try
        {
            getAllContacts(getApplicationContext());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getAllContacts(Context context)
    {
        try
        {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0)
            {
                ArrayList<AllContacts> allContactList = new ArrayList<>();
                int i = 0;
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        for (int j = 0; pCur.moveToNext() ; j++)
                        {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNo = Utils.removeInvalidSymbolsFromNumber2(context, phoneNo);
                            AllContacts allContacts = new AllContacts();
                            allContacts.setId((long)i);
                            allContacts.setName(name);
                            allContacts.setNumber(phoneNo);
                            allContacts.setIsDeleteShown(false);
                            allContactList.add(allContacts);
                            i++;


                        }
                        pCur.close();
                    }
                }
                if (AppController.getInstance().getDaoSession().getAllContactsDao().loadAll().size() > 0)
                {
                    AppController.getInstance().getDaoSession().getAllContactsDao().deleteAll();
                }
                AppController.getInstance().getDaoSession().getAllContactsDao().insertInTx(allContactList);
            }

            sendAllContactsToActivity();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void sendAllContactsToActivity()
    {

        Intent intent = new Intent(CONTACT_ACTION);
        sendBroadcast(intent);
    }

}
