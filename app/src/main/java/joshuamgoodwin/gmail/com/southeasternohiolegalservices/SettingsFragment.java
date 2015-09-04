package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuagoodwin on 8/25/15.
 */
public class SettingsFragment extends Fragment {

    private Switch pushSwitch;
    private TextView selectedCounties;
    private ToggleButton pushToggle;
    private Spinner homeCounty;
    private String[] counties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_layout, container, false);
        findViews(rootView);
        counties = getActivity().getResources().getStringArray(R.array.countyNames);
        return rootView;
    }

    private void findViews(View rootView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            pushSwitch = (Switch) rootView.findViewById(R.id.push_switch);
            pushSwitch.setChecked(arePushesEnabled());
            pushSwitch.setOnCheckedChangeListener(pushSelector);

        } else {
            pushToggle = (ToggleButton) rootView.findViewById(R.id.push_switch);
            pushToggle.setChecked(arePushesEnabled());
            pushToggle.setOnCheckedChangeListener(pushSelector);
        }

        selectedCounties = (TextView) rootView.findViewById(R.id.selectedCounties);
        adjustCountySelectionColor();

        selectedCounties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arePushesEnabled()) showCountyDialog();
            }
        });

        homeCounty = (Spinner) rootView.findViewById(R.id.home_county_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.countyNames, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        homeCounty.setAdapter(adapter);
    }



    private void pushEnabled() {
        ParsePush.subscribeInBackground(getString(R.string.pushEnabled));
        ParsePush.unsubscribeInBackground(getString(R.string.pushDisabled));
        adjustCountySelectionColor();

    }

    private void pushDisabled() {
        ParsePush.unsubscribeInBackground(getString(R.string.pushEnabled));
        ParsePush.subscribeInBackground(getString(R.string.pushDisabled));
        adjustCountySelectionColor();
    }

    private void showCountyFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CountySelectionFragment())
                .commit();
    }

    /**
     * Method to determine whether the Parse channel contains R.string.pushEnabled,
     * which would indicate that the user has enable push notifications.
     *
     * @return whether the Parse channel contains R.string.pushEnabled
     */
    private boolean arePushesEnabled() {
        List<String> currentChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        return currentChannels.contains(getString(R.string.pushEnabled));
    }

    private Dialog countySelectionDialog() {
        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        // add currently selected counties to mSelectedItems
        for (int i = 0; i < getBooleanListOfCounties().length; i++) {
            if (getBooleanListOfCounties()[i]) mSelectedItems.add(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.countySelection)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.countyNames, getBooleanListOfCounties(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        unsubscribeFromCounties();
                        subscribeToSelectedCounties(mSelectedItems);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    private CompoundButton.OnCheckedChangeListener pushSelector = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isChecked()) {
                pushEnabled();
                //showCountyFragment();
                showCountyDialog();
            } else {
                pushDisabled();
            }
        }
    };

    private void showCountyDialog() {
        Dialog showCountyDialog = countySelectionDialog();
        showCountyDialog.show();
    }

    /**
     * Unsubscribes the user from all counties in the channels
     * on Parse push notifications.
     *
     */
    private void unsubscribeFromCounties() {
        ArrayList<String> countiesToRemove = getSubscribedCounties();
        for (String county: countiesToRemove) {
            ParsePush.unsubscribeInBackground(county);
        }
    }

    /**
     * Returns an ArrayList containing all of the counties that are currently
     * subsribed to in Parse
     *
     * @return ArrayList\<String> that contains all counties the user is currently subscribed to in Parse
     */
    private ArrayList<String> getSubscribedCounties() {
        // get a list of current counties already subscribed to
        List<String> currentChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        ArrayList<String> countiesSubscribedTo = new ArrayList<>();
        for (int i = 0; i < counties.length; i++) {
            if (currentChannels.contains(counties[i])) {
                countiesSubscribedTo.add(counties[i]);
            }
        }
        return countiesSubscribedTo;
    }

    private void adjustCountySelectionColor() {
        if (arePushesEnabled()) {
            selectedCounties.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
        } else {
            selectedCounties.setTextColor(getResources().getColor(R.color.primary_text_disabled_material_light));
        }
    }

    private boolean[] getBooleanListOfCounties() {
        // get currently subscribed counties
        ArrayList subscribedCounties = getSubscribedCounties();
        ArrayList<Boolean> results = new ArrayList<>();
        for (String county: counties) {
            if (subscribedCounties.contains(county)) {
                results.add(true);
            } else {
                results.add(false);
            }
        }
        boolean[] test = new boolean[results.size()];
        for (int i = 0; i < test.length; i++) {
            test[i] = results.get(i);
        }
        return test;
    }

    /**
     * Method to subscribe the user to all counties selected in the listView
     *
     */
    private void subscribeToSelectedCounties(ArrayList<Integer> newCountyPosition) {
        ArrayList<String> newCountyNames = new ArrayList<>();
        for (int pos: newCountyPosition) {
            newCountyNames.add(counties[pos]);
        };
        for (String county: newCountyNames) {
            ParsePush.subscribeInBackground(county);
        }
    }

}
