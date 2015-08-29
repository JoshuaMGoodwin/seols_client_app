package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by joshuagoodwin on 8/26/15.
 */
public class MainActivityFragment extends Fragment {

    private Button requestAssistance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {
        requestAssistance = (Button) rootView.findViewById(R.id.request_services);
        requestAssistance.setOnClickListener(buttonClicked);
    }

    private void changeFragment(Fragment fragmentName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new CountySelectionFragment())
                .commit();
    }



    private View.OnClickListener buttonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.request_services:
                    changeFragment(new RequestServicesFragment());
                    break;
            }
        }
    };

}
