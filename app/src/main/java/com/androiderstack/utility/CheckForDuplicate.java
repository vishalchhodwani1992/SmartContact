package com.androiderstack.utility;

import com.androiderstack.item.BlockContacts;
import com.androiderstack.item.IMPContacts;
import com.androiderstack.smartcontacts.AppController;

import java.util.List;


public class CheckForDuplicate {

	public static boolean isIMPNumberUnique(String number)
	{
		List<IMPContacts> listOfAllNames = AppController.getInstance().getDaoSession().getIMPContactsDao().loadAll();

		for (int i = 0; i < listOfAllNames.size(); i++)
		{
			if(listOfAllNames.get(i).getNumber().equalsIgnoreCase(number))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean isBlockedNumberUnique(String number)
	{
        List<BlockContacts> contactsList = AppController.getInstance().getDaoSession().getBlockContactsDao().loadAll();

        for (int i = 0; i < contactsList.size(); i++)
        {
            if(contactsList.get(i).getNumber().equalsIgnoreCase(number))
            {
                return false;
            }
        }
        return true;
	}

}
