package com.project.megaconvertor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew on 4/9/2018.
 */

public class ConvertorActivity extends AppCompatActivity {

    private String selectedItem;
    private Spinner measuresSpinner;
    private Spinner fromMeasureSpinner;
    private Spinner toMeasureSpinner;
    private double convertedValue = 0;

    private UnitConvertor unitConvertor;

    private String SELECTED_ITEM_KEY = "selectedItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertor);

        unitConvertor = new UnitConvertor();
        populateSpinner();

        final Button button = findViewById(R.id.convert_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editValue = (EditText)findViewById(R.id.from);
                if (isInteger(editValue.getText().toString())){
                    String unit = measuresSpinner.getSelectedItem().toString();
                    final String from = fromMeasureSpinner.getSelectedItem().toString();
                    String to = toMeasureSpinner.getSelectedItem().toString();
                    final int valueToConvert = Integer.parseInt(editValue.getText().toString());

                    if (unit.equals("Currency")) {
                        String accessKey = "eab74e4cf8bacb65a9dd0347cc0122ec";
                        String url = "http://data.fixer.io/api/latest?access_key=" + accessKey + "&symbols=" + to;
                        final String toFinal = to;

                        // Request a string response from the provided URL.
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, (JSONObject) null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String responseText =  response.getJSONObject("rates").getString(toFinal);
                                            convertedValue = valueToConvert * Double.parseDouble(responseText);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error

                                    }
                                });

                        // Access the RequestQueue through your singleton class.
                        MyRequestQueue queue = MyRequestQueue.getInstance(ConvertorActivity.this);
                        queue.getQueue().add(jsonObjectRequest);
                    }
                    else if (unit.equals("Temperature")) {
                        convertedValue = unitConvertor.convertTemperature(from, to, valueToConvert);
                    }
                    else {
                        convertedValue = unitConvertor.convert(unit, from, to, valueToConvert);
                    }

                    TextView convertedValueTextView = (TextView)findViewById(R.id.to);
                    convertedValueTextView.setText(Double.toString(convertedValue));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(selectedItem != null) {
            populateSubSpinners();
        }
    }

    private void populateSpinner() {
        measuresSpinner = (Spinner) findViewById(R.id.physic_measure_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.measures_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        measuresSpinner.setAdapter(adapter);
        selectedItem = measuresSpinner.getSelectedItem().toString();

    }

    private void populateSubSpinners() {
        fromMeasureSpinner = (Spinner) findViewById(R.id.measure_from);
        toMeasureSpinner = (Spinner) findViewById(R.id.measure_to);
        measuresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter<CharSequence> adapter;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerValue = measuresSpinner.getSelectedItem().toString();
                if (spinnerValue.equals("Temperature")) {
                     adapter = ArrayAdapter.createFromResource(ConvertorActivity.this,
                            R.array.temperature_measures_array, android.R.layout.simple_spinner_item);

                    fromMeasureSpinner.setAdapter(adapter);
                    toMeasureSpinner.setAdapter(adapter);

                } else if (spinnerValue.equals("Weight")) {
                    adapter = ArrayAdapter.createFromResource(ConvertorActivity.this,
                            R.array.weight_measures_array, android.R.layout.simple_spinner_item);

                    fromMeasureSpinner.setAdapter(adapter);
                    toMeasureSpinner.setAdapter(adapter);

                } else if (spinnerValue.equals("Length")) {
                    adapter = ArrayAdapter.createFromResource(ConvertorActivity.this,
                            R.array.length_measures_array, android.R.layout.simple_spinner_item);

                    fromMeasureSpinner.setAdapter(adapter);
                    toMeasureSpinner.setAdapter(adapter);

                } else if (spinnerValue.equals("Time")) {
                    adapter = ArrayAdapter.createFromResource(ConvertorActivity.this,
                            R.array.time_measures_array, android.R.layout.simple_spinner_item);

                    fromMeasureSpinner.setAdapter(adapter);
                    toMeasureSpinner.setAdapter(adapter);
                } else if (spinnerValue.equals("Currency")) {
                    adapter = ArrayAdapter.createFromResource(ConvertorActivity.this,
                            R.array.currency_measures_array, android.R.layout.simple_spinner_item);

                    fromMeasureSpinner.setAdapter(adapter);
                    toMeasureSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's selected element
        savedInstanceState.putString(SELECTED_ITEM_KEY, selectedItem);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        selectedItem = savedInstanceState.getString(SELECTED_ITEM_KEY);

        measuresSpinner.setSelection(((ArrayAdapter)measuresSpinner.getAdapter()).getPosition(selectedItem));
    }

    private int convertFromMeter() {
        return 5;
    }
}
