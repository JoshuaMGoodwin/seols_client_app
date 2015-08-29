package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by joshuagoodwin on 8/26/15.
 */
public class RequestServicesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_layout, container, false);
        //findViews(rootView);
        return rootView;
    }

}
