/*******************************************************************************
 * Copyright (C) 2012 The Android Money Manager Ex Project
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.android.money.manager.ex;

import java.util.Formatter;
import java.util.List;

import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.android.money.manager.ex.database.TableCurrencyFormats;
import com.android.money.manager.ex.dropbox.DropboxActivity;
/**
 * 
 * @author Alessandro Lazzari (lazzari.ale@gmail.com)
 * @version 1.0.0
 */
public class MoneyManagerPrefsActivity extends PreferenceActivity {
	// prendo la referenza all'applicazione
	private MoneyManagerApplication application;
	// ID delle preferenze
	private Preference pUserName;
	private Preference pDatabasePath;
	private Preference pDropboxFile;
	private ListPreference lstBaseCurrency;
	private ListPreference lstDropboxMode;
	private CheckBoxPreference chkAccountOpen;
	private CheckBoxPreference chkAccountFav;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// imposto layout
		addPreferencesFromResource(R.xml.prefrences);
		// prendo la referenza all'applicazione
		application = (MoneyManagerApplication)getApplication();
		PreferenceManager.getDefaultSharedPreferences(this);
		// prendo la preferenza della username e imposto la summery
		pUserName = findPreference(MoneyManagerApplication.PREF_USER_NAME);
		if (pUserName != null) {
			pUserName.setSummary(application.getUserName());
			// imposto il listener per la modifica al database
			pUserName.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					application.setUserName((String) newValue, true);
					pUserName.setSummary(application.getUserName());
					return true;
				}
			});
		}
		// prendo la ListPreference della BaseCurrency
		lstBaseCurrency = (ListPreference) findPreference(MoneyManagerApplication.PREF_BASE_CURRENCY);
		if (lstBaseCurrency != null) {
			List<TableCurrencyFormats> currencies = application.getAllCurrencyFormats();
			String[] entries = new String[currencies.size()];
			String[] entryValues = new String[currencies.size()];
			// composizione dei due vettori entry
			for(int i = 0; i < currencies.size(); i ++) {
				entries[i] = currencies.get(i).getCurrencyName();
				entryValues[i] = ((Integer)currencies.get(i).getCurrencyId()).toString();
			}
			// imposto i valori
			lstBaseCurrency.setEntries(entries);
			lstBaseCurrency.setEntryValues(entryValues);
			// imposto il summary
			TableCurrencyFormats tableCurrency = application.getCurrencyFormats(application.getBaseCurrencyId());
			if (tableCurrency != null) {
				lstBaseCurrency.setSummary(tableCurrency.getCurrencyName());
			}
			// imposto un listener sulla modifica
			lstBaseCurrency.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					if (application.setBaseCurrencyId(Integer.parseInt((String)newValue), true)) {
						TableCurrencyFormats tableCurrency = application.getCurrencyFormats(application.getBaseCurrencyId());
						if (tableCurrency != null) {
							lstBaseCurrency.setSummary(tableCurrency.getCurrencyName());
						}
					}
					return true;
				}
			});
		}
		// prendo il check open per la gestione se devo riavviare l'interfaccia
		chkAccountOpen = (CheckBoxPreference)findPreference(MoneyManagerApplication.PREF_ACCOUNT_OPEN_VISIBLE);
		chkAccountFav = (CheckBoxPreference)findPreference(MoneyManagerApplication.PREF_ACCOUNT_FAV_VISIBLE);
		// definizione del listener di mod
		OnPreferenceChangeListener listener = new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// imposto che la MainActivity va riavviata
				MainActivity.setRestartActivity(true);
				return true;
			}
		};
		// linster sul cambiamento
		chkAccountOpen.setOnPreferenceChangeListener(listener);
		chkAccountFav.setOnPreferenceChangeListener(listener);
		// sezione dropbox account e file
		pDropboxFile = findPreference("dropboxlinkedfile");
		SharedPreferences prefs = getSharedPreferences(DropboxActivity.ACCOUNT_PREFS_NAME, 0);
		pDropboxFile.setSummary(prefs.getString(DropboxActivity.REMOTE_FILE, null));
		// set che non sono selezionabili
		pDropboxFile.setSelectable(false);
		// gestione della listview sulle modalit� di sincronizzazione
		lstDropboxMode = (ListPreference) findPreference(MoneyManagerApplication.PREF_DROPBOX_MODE);
		if (lstDropboxMode != null) {
			lstDropboxMode.setSummary(application.getDropboxSyncMode());
			lstDropboxMode.setDefaultValue(getResources().getStringArray(R.array.dropbox_sync_item)[0]);
			// imposto un listener sulla modifica
			lstDropboxMode.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					lstDropboxMode.setSummary((CharSequence) newValue);
					return true;
				}
			});
		pDatabasePath = findPreference(MoneyManagerApplication.PREF_DATABASE_PATH);
		pDatabasePath.setSummary(MoneyManagerApplication.getDatabasePath(getApplicationContext()));
		}
	}
}
