package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by joshuagoodwin on 8/26/15.
 */
public class RequestServicesFragment extends Fragment {

    private EditText etFirstName;
    private EditText etLastName;
    private Button submit;
    private Spinner householdSizeSpinner;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_services_fragment, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.mWebView);
        etFirstName = (EditText) rootView.findViewById(R.id.first_name);
        etLastName = (EditText) rootView.findViewById(R.id.last_name);
        submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPressed();
            }
        });
        householdSizeSpinner = (Spinner) rootView.findViewById(R.id.household_size_spinner);
        householdSizeSpinner.setAdapter(householdSizeAdapter());
    }

    private ArrayAdapter<String> householdSizeAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.household_size));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    private void submitPressed() {
        mWebView.loadUrl(getResources().getString(R.string.request_services_url));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                String javaScript = "javascript:";
                javaScript += "var firstName = document.getElementById('Field2').value = '"+ etFirstName.getText().toString() + "';";
                javaScript += "var firstName = document.getElementById('Field3').value = '"+ etLastName.getText().toString() + "';";
                javaScript += "var householdSize = document.getElementById('Field19').value = '" + householdSizeSpinner.getSelectedItem() + "';";
                view.loadUrl(javaScript);
            }
        });
    }


}
