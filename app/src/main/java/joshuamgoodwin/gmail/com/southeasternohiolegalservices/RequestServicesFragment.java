package joshuamgoodwin.gmail.com.southeasternohiolegalservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;

/**
 * Created by joshuagoodwin on 8/26/15.
 */
public class RequestServicesFragment extends Fragment {

    private CheckBox cbMilitary;
    private CheckBox cbCitizenship;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etOpposingParty;
    private EditText etAddress1;
    private EditText etAddress2;
    private EditText etCity;
    private EditText etState;
    private EditText etZip;
    private EditText etEmail;
    private EditText etPhone1;
    private EditText etPhone2;
    private EditText etPhone3;
    private EditText etAge;

    private Button submit;

    private Spinner countrySpinner;
    private Spinner childrenInHouseSpinner;
    private Spinner countySpinner;
    private Spinner householdSizeSpinner;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_services_fragment, container, false);
        findViews(rootView);
        return rootView;
    }

    private void findViews(View rootView) {

        // TODO remove this line, debugging only
        mWebView = (WebView) rootView.findViewById(R.id.webview);

        cbMilitary = (CheckBox) rootView.findViewById(R.id.military_check);
        cbCitizenship = (CheckBox) rootView.findViewById(R.id.citizenship);

        etFirstName = (EditText) rootView.findViewById(R.id.first_name);
        etLastName = (EditText) rootView.findViewById(R.id.last_name);
        etOpposingParty = (EditText) rootView.findViewById(R.id.opposing_parties);
        etAddress1 = (EditText) rootView.findViewById(R.id.address_1);
        etAddress2 = (EditText) rootView.findViewById(R.id.address_2);
        etCity = (EditText) rootView.findViewById(R.id.city);
        etState = (EditText) rootView.findViewById(R.id.state);
        etZip = (EditText) rootView.findViewById(R.id.zip);
        etEmail = (EditText) rootView.findViewById(R.id.email);
        etPhone1 = (EditText) rootView.findViewById(R.id.phone1);
        etPhone2 = (EditText) rootView.findViewById(R.id.phone2);
        etPhone3 = (EditText) rootView.findViewById(R.id.phone3);
        etAge = (EditText) rootView.findViewById(R.id.age);

        submit = (Button) rootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPressed();
            }
        });
        householdSizeSpinner = (Spinner) rootView.findViewById(R.id.household_size_spinner);
        householdSizeSpinner.setAdapter(arrayAdapter(getResources().getStringArray(R.array.household_size)));

        childrenInHouseSpinner = (Spinner) rootView.findViewById(R.id.number_children_spinner);
        childrenInHouseSpinner.setAdapter(arrayAdapter(getResources().getStringArray(R.array.number_of_kids)));

        countySpinner = (Spinner) rootView.findViewById(R.id.problem_county_spinner);
        countySpinner.setAdapter(arrayAdapter(getResources().getStringArray(R.array.countyNames)));

        countrySpinner = (Spinner) rootView.findViewById(R.id.countries);
        countrySpinner.setAdapter(arrayAdapter(getResources().getStringArray(R.array.countries)));

    }

    private ArrayAdapter<String> arrayAdapter(String[] array) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }


    private void submitPressed() {
        mWebView.loadUrl(getResources().getString(R.string.request_services_url));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                String javaScript = "javascript:";
                javaScript += addJS("2", etFirstName.getText().toString());
                javaScript += addJS("3", etLastName.getText().toString());
                javaScript += addJS("4", etAddress1.getText().toString());
                javaScript += addJS("5", etAddress2.getText().toString());
                javaScript += addJS("6", etCity.getText().toString());
                javaScript += addJS("7", etState.getText().toString());
                javaScript += addJS("8", etZip.getText().toString());
                javaScript += addJS("9", countrySpinner.getSelectedItem().toString());
                javaScript += addJS("12", etEmail.getText().toString());
                javaScript += addJS("13", etPhone1.getText().toString());
                javaScript += addJS("13-1", etPhone2.getText().toString());
                javaScript += addJS("13-2", etPhone3.getText().toString());
                javaScript += addJS("17", etOpposingParty.getText().toString());
                javaScript += addJS("19", householdSizeSpinner.getSelectedItem().toString());
                javaScript += addJS("21", childrenInHouseSpinner.getSelectedItem().toString());
                javaScript += "var x = document.getElementById('Field24_" + citizenshipValue() + "').checked = true;";
                javaScript += addJS("28", countySpinner.getSelectedItem().toString());
                javaScript += addJS("38", etAge.getText().toString());
                javaScript += "var x = document.getElementById('Field40_" + militaryValue() + "').checked = true;";
                Log.d("java:", javaScript);
                view.loadUrl(javaScript);
            }
        });
    }

    private String addJS(String fieldNumber, String textToAdd) {
        return "var x = document.getElementById('Field" + fieldNumber + "').value = '" + textToAdd + "';";
    }

    /**
     * Returns an array of strings  of either "Yes"/"No" or "No"/"yes" to fill in the
     * radio button on the WuFoo form
     *
     * @return "Yes"/"No" if the military box is checked or "No"/"Yes" if it isn't checked
     */
    private String militaryValue() {
        return cbMilitary.isChecked() ? "0" : "1";
    }

    private String citizenshipValue() {
        return cbCitizenship.isChecked() ? "0" : "1";
    }


}
