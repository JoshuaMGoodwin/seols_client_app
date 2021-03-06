package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import com.parse.ParsePush;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        // ask the user about push notifications if is the first run
        if(isFirstRun()) askAboutPush();
        changeFragments(new MainActivityFragment());
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            changeFragments(new SettingsFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

        /**
         * Method to see if this is the first time the user has run the
         * app by using shared preferences.
         *
         * @return true if this is the first time
         */
    private boolean isFirstRun() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (!sp.getBoolean("firstTime", false)) {
            sp.edit().putBoolean("firstTime", true).apply();
            return true;
        } else {
            return false;
        }
    }

        /**
         * Method that genereates an alertDialog to ask the user whether they want to
         * enable push notifications. Run if this is the user's first time using
         * the app to allow user to opt in to push notifications.
         */
    private void askAboutPush() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Enable button
                // subscribe to Parse pushEnabled channel
                ParsePush.subscribeInBackground(getString(R.string.pushEnabled));
                dialog.dismiss();
                // show county selection fragment so user can subscribe to counties
                changeFragments(new CountySelectionFragment());
            }
        });
        builder.setNegativeButton("Don't Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Don't Enable button
                ParsePush.subscribeInBackground(getString(R.string.pushDisabled));
                dialog.dismiss();
                canChangeChoiceDialog("enable");
            }
        });
        builder.setTitle("Push Notifications");
        builder.setMessage("This app would like to occasionally provide you with notifications about important changes in the law and other news. Would you like to enable these notifications?");
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

        /**
         * Method that shows an alertDialog reminding the user that they can either enable
         * or disable push notifications in the future through the settings options.
         *
         * @param decision either "enable" or "disable", the opposite of the user's current
         *                 push notification preference
         */
    private void canChangeChoiceDialog(String decision) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You can change your mind and " + decision + " push notifications at any time through the settings menu.")
                .setTitle("Changing Notification Settings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method that changes the fragment displayed in the main container.
     *
     * @param fragmentName the name of the fragment to switch the main view to
     */
    public void changeFragments(Fragment fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentName)
                .commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
