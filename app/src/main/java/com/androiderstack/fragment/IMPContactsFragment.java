package com.androiderstack.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androiderstack.adapter.IMPContactRecyclerViewAdapter;
import com.androiderstack.custom_view.StyledEditText;
import com.androiderstack.custom_view.StyledTextView;
import com.androiderstack.item.AllContacts;
import com.androiderstack.item.IMPContacts;
import com.androiderstack.prefs.AppSharedPrefs;
import com.androiderstack.smartcontacts.AppController;
import com.androiderstack.smartcontacts.R;
import com.androiderstack.smartcontacts.SettingActivity;
import com.androiderstack.utility.CheckForDuplicate;
import com.androiderstack.utility.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gst-10064 on 6/6/16.
 */

public class IMPContactsFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = "IMPContactsFragment";

    private static final int REQUEST_CONTACT_NUMBER = 10;

    IMPContactRecyclerViewAdapter contactListViewAdapter;
    public List<IMPContacts> myContactList;
    private List<AllContacts> allContactList = new ArrayList<>();
    FloatingActionButton addButton;

    StyledTextView noDataTextView;
    RecyclerView contactListView;

    StyledEditText contactField;

    String globalName = "", globalNumber  ="";
    private InputMethodManager imm;
    private InterstitialAd interstitial;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_two, container, false);

        initializeViews(rootView);

        loadInertialAd(false);

        getFromDB();

        AppController.getInstance().getFirebaseAnalytics().setCurrentScreen(getActivity(), "IMP Contacts", "IMPContactsFragment");

        return rootView;
    }

    private void initializeViews(View rootView) {

        contactListView = (RecyclerView) rootView.findViewById(R.id.activityMain_listView);
        myContactList = new ArrayList<>();
        noDataTextView = (StyledTextView) rootView.findViewById(R.id.activityMain_noDataText);
        addButton = (FloatingActionButton) rootView.findViewById(R.id.addNewContact);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        interstitial = new InterstitialAd(getActivity());

        interstitial.setAdUnitId(getActivity().getResources().getString(R.string.Interstitial_ad_id));

        contactListViewAdapter = new IMPContactRecyclerViewAdapter(getActivity(), myContactList);
        contactListView.getItemAnimator().setChangeDuration(0);
        contactListView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contactListView.setLayoutManager(layoutManager);
        contactListView.setItemAnimator(new DefaultItemAnimator());

        contactListView.setAdapter(contactListViewAdapter);

        addButton.setOnClickListener(this);
        registerForContextMenu(contactListView);
    }

    private void showDialogue()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update

        View dialogView = View.inflate(getActivity(), R.layout.alert_layout, null);

        contactField = (StyledEditText) dialogView.findViewById(R.id.alertLayout_phoneNumberEditText);
        ImageView pickFromContact = (ImageView) dialogView.findViewById(R.id.alertLayout_pickFromContacts);
        StyledTextView saveButton = (StyledTextView) dialogView.findViewById(R.id.alertLayout_saveButton);
        StyledTextView cancelButton = (StyledTextView) dialogView.findViewById(R.id.alertLayout_cancelButton);

        RelativeLayout relativeLayout = (RelativeLayout)dialogView.findViewById(R.id.rl);
        pickFromContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickFromContact();
            }
        });

        alertDialog.setView(relativeLayout);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                globalName = globalNumber = "";
                contactListView.setFocusable(true);
                imm.hideSoftInputFromWindow(contactListView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if(validationSuccess())
                    {
                        String number = contactField.getText().toString().trim();
                        number = Utils.removeInvalidSymbolsFromNumber2(getActivity(), number);
                        getNameOfNumber(number);
                        saveToDB(globalName, number);
                        alertDialog.dismiss();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                contactField.setFocusable(true);
                imm.showSoftInput(contactField, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

//        menu.setHeaderTitle("Select options");
        menu.add(0, 100, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
       switch (item.getItemId())
       {
            case 100:
                showDeleteAlert(contactListViewAdapter.getPosition());
       }
        return true;
    }

    private boolean validationSuccess()
    {
        String number = contactField.getText().toString().trim();

        if(number.equalsIgnoreCase(""))
        {
            contactField.setError("Should not empty");
            return false;
        }
        if(number.length() < 10)
        {
            contactField.setError("Incorrect number");
            return false;
        }

        return true;
    }

    private void saveToDB(String name, String number)
    {
        if(number.length() >= 10)
        {
            if(CheckForDuplicate.isIMPNumberUnique(number))
            {
                List<IMPContacts> list = AppController.getInstance().getDaoSession().getIMPContactsDao().loadAll();
                long id = 0;
                if (list.size() > 0)
                {
                    id = list.get(list.size()-1).getId();
                    id++;
                }

                IMPContacts impContacts = new IMPContacts();
                if(name.equalsIgnoreCase(""))// if name is empty
                {
                    impContacts.setIsDeleteShown(false);
                    impContacts.setId(id);
                    impContacts.setName(number);
                    impContacts.setNumber(number);
                    AppController.getInstance().getDaoSession().getIMPContactsDao().insert(impContacts);
                }
                else
                {
                    impContacts.setIsDeleteShown(false);
                    impContacts.setId(id);
                    impContacts.setName(name);
                    impContacts.setNumber(number);
                    AppController.getInstance().getDaoSession().getIMPContactsDao().insert(impContacts);
                }
                sendDataToFirebase(impContacts);
                getFromDB();
                loadInertialAd(true);
            }
            else
            {
                Toast.makeText(getActivity(), "Duplicate Contact Not Allowed..!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Invalid Number..!", Toast.LENGTH_LONG).show();
        }
    }

    private void sendDataToFirebase(IMPContacts impContacts) {
        try
        {
            Bundle bundle = new Bundle();
            bundle.putString("ContactName", impContacts.getName());
            bundle.putString("ContactNumber", impContacts.getNumber());
            Utils.sendEventToFirebase("IMP_CONTACT_ADDED", bundle);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getFromDB()
    {
        try
        {
            myContactList.clear();
            myContactList.addAll(AppController.getInstance().getDaoSession().getIMPContactsDao().loadAll());
            if(myContactList.size() > 0)
            {
                noDataTextView.setVisibility(View.GONE);
            }
            else
            {
                noDataTextView.setText(getResources().getString(R.string.no_record_found, getResources().getString(R.string.favorite_contacts)));
                noDataTextView.setVisibility(View.VISIBLE);
            }
            contactListViewAdapter.notifyDataSetChanged();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void showNumberAlert(final int position)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()); //Read Update
        alertDialog.setTitle(myContactList.get(position).getName());
        alertDialog.setMessage(myContactList.get(position).getNumber());
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    private void showDeleteAlert(final int position)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()); //Read Update
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do You Want to Delete This Contact\n" + myContactList.get(position).getName());
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppController.getInstance().getDaoSession().getIMPContactsDao().delete(myContactList.get(position));
                getFromDB();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

    private void showDeleteAllAlert()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()); //Read Update
        alertDialog.setTitle("Delete?");
        if(myContactList.size()==1)
            alertDialog.setMessage("Do you want to delete this contact?");
        else
            alertDialog.setMessage("Do you want to delete all "+myContactList.size()+" contacts?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearAllPeople();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {}
        });

        alertDialog.show();

    }
    private void pickFromContact()
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, REQUEST_CONTACT_NUMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        globalName = globalNumber = "";
        try
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if(requestCode==REQUEST_CONTACT_NUMBER)
                {
                    Uri uri = data.getData();
                    String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                    Cursor cursor = getActivity().getContentResolver().query(uri, projection,null, null, null);
                    if(cursor!=null && cursor.getCount()>0)
                    {
                        cursor.moveToFirst();
                        int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        globalNumber = cursor.getString(numberColumnIndex);

                        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        globalName = cursor.getString(nameColumnIndex);
                        cursor.close();
                    }

                    globalNumber = Utils.removeInvalidSymbolsFromNumber2(getActivity(), globalNumber);

                    contactField.setText(globalNumber);
                    contactField.setEnabled(false);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void openSetting()
    {
        Intent actionSettingActivity = new Intent(getActivity(), SettingActivity.class);
        startActivity(actionSettingActivity);
    }

    private void clearAllPeople()
    {
        try
        {
            AppController.getInstance().getDaoSession().getIMPContactsDao().deleteAll();
            getFromDB();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getNameOfNumber(String number)
    {
        try
        {
            allContactList = AppController.getInstance().getDaoSession().getAllContactsDao().loadAll();
            for (int i = 0; i < allContactList.size(); i++)
            {
                if(allContactList.get(i).getNumber().endsWith(number) || allContactList.get(i).getNumber().equals(number))
                {
                    globalName = allContactList.get(i).getName();
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void loadInertialAd(final boolean showAd)
    {
        try
        {
            if(showAd && interstitial.isLoaded())
            {
                interstitial.show();
            }
            else
            {
                AdRequest adRequest = new AdRequest.Builder()

                        // Add a test device to show Test Ads
                        .build();

                // Load ads into Interstitial Ads
                interstitial.loadAd(adRequest);

                // Prepare an Interstitial Ad Listener
                interstitial.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        // Call displayInterstitial() function
                        if(showAd)
                        {
                            interstitial.show();
                            loadInertialAd(false);
                        }
                    }
                });
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void clearList()
    {
        super.clearList();
        if(myContactList.size()!=0)
            showDeleteAllAlert();
        else
            Toast.makeText(getActivity(), "Nothing To Delete", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setMenuEnable(PopupMenu popupMenu) {
        super.setMenuEnable(popupMenu);

        try
        {
            if(AppSharedPrefs.getInstance().isAppEnable())
            {
                popupMenu.getMenu().getItem(0).setTitle("Enabled");
                popupMenu.getMenu().getItem(0).setTitleCondensed("Enabled");
                popupMenu.getMenu().getItem(0).setChecked(true);
            }
            else
            {
                popupMenu.getMenu().getItem(0).setTitle("Disabled");
                popupMenu.getMenu().getItem(0).setTitleCondensed("Disabled");
                popupMenu.getMenu().getItem(0).setChecked(false);
            }

            if(myContactList.size() == 0)
                popupMenu.getMenu().getItem(1).setEnabled(false);
            else
                popupMenu.getMenu().getItem(1).setEnabled(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.addNewContact:
                showDialogue();
                break;
        }
    }
}