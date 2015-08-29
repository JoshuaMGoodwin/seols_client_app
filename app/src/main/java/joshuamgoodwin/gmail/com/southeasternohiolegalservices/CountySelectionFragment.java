package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.ArrayList;
import java.util.List;

    /**
     * CountySelectionFragment.java
     * Purpose: a fragment that allows users to decide which counties/channels
     * to subsrcribe to when receiving Parse push notifications.
     *
     * @author Joshua Goodwin
     * @version 1.0 last updated
     */
public class CountySelectionFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private Button addButton;
    private ListView listView;
    private String[] counties;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.county_selection_layout, container, false);
        findViews(rootView);
        createList();
        selectSubscribedCounties();
        return rootView;
    }

        /**
         * Method that initializes the various UI elements on the fragmnt
         * and sets the button's onClickListener.
         *
         * @param rv The rootView created in the onCreateMethod
         */
    private void findViews(View rv) {
        counties = getActivity().getResources().getStringArray(R.array.countyNames);
        listView = (ListView)rv.findViewById(R.id.county_selection_list);
        addButton = (Button)rv.findViewById(R.id.select_county_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCountiesToChannel();
            }
        });
    }

        /**
         * Method that generates the content of the listView
         */
    private void createList() {
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, counties);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
    }

        /**
         * Method called by the button's onClickListener. This method
         * unsubscribes from all Parse county channels and then subscribes
         * to all counties selected by the user.
         */
    private void addCountiesToChannel() {
        unsubscribeFromCounties();
        subscribeToSelectedCounties();
        Toast.makeText(getActivity(), getString(R.string.confirmation_toast), Toast.LENGTH_LONG).show();
    }

    /**
     * Returns an ArrayList\<String> of all of the counties selected by the user
     *
     * @return ArrayList\<String> of all counties selected by the user
     */
    private ArrayList<String> getCountiesFromLV() {

        // returns the list of selected counties by user as an arraylist
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }

        return selectedItems;

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

        /**
         * Method to subscribe the user to all counties selected in the listView
         *
         */
    private void subscribeToSelectedCounties() {
        for (String county: getCountiesFromLV()) {
            ParsePush.subscribeInBackground(county);
        }
    }

        /**
         * Method that selects in the listView all counties the user
         * has already subscribed to so that the user can then unsubscribe from them.
         */
    private void selectSubscribedCounties() {

        ArrayList<String> subscribedCounties = getSubscribedCounties();
        for (int i = 0; i < counties.length; i++) {
            if (subscribedCounties.contains(counties[i])) {
                listView.setItemChecked(i, true);
            }
        }
    }


}
