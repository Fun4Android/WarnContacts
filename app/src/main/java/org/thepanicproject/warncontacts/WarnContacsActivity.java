package org.thepanicproject.warncontacts;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.thepanicproject.warncontacts.constants.WarnConstants;
import org.thepanicproject.warncontacts.fragments.ContactSettings;
import org.thepanicproject.warncontacts.fragments.ContactsFragment;
import org.thepanicproject.warncontacts.fragments.WarnContacsSettingsFragment;

public class WarnContacsActivity extends AppCompatActivity implements
        ContactsFragment.OnContactListener, ContactSettings.OnContacSettingsListener {

    private FragmentManager mFragmentManager;
    private FloatingActionButton mFab;
    private String newContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_contacs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getFragmentManager();

        // Do not overlapping fragments.
        if (savedInstanceState != null) return;

        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ContactsFragment())
                .commit();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, WarnConstants.CONTACT_PICKER_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_warn_contacs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mFab.hide();

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new WarnContacsSettingsFragment())
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case WarnConstants.CONTACT_PICKER_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    newContact = data.getData().getLastPathSegment();
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (newContact != null) {
            ContactSettings contactSettings = new ContactSettings();
            Bundle args = new Bundle();
            args.putString(WarnConstants.CONTACT_ID, newContact);
            contactSettings.setArguments(args);

            newContact = null;
            mFab.hide();

            mFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, contactSettings)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            mFragmentManager.popBackStack();
            mFab.show();
        }
    }

    @Override
    public void onContactSettingsCallback() {
        mFab.show();
    }

    @Override
    public void onContactListenerCallback(int id) {

    }
}
