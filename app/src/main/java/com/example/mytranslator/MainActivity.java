package com.example.mytranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.text.StringPrepParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mytranslator.Adapters.LanguageSpinnerAdapter;
import com.example.mytranslator.Util.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private EditText translatedEditText;
    private Spinner spinner;
    private MyRequestQueue queue;
    private String selectedLanguage = "en";
    private List<Pair<String, String>> dirs = new ArrayList<>();
    private /*List<Map<String, String>>*/Map<String, String> langs = new HashMap<>();
    private String enteredText;
    private String translatedText;
    private List<String> supportedLanguagesList = new ArrayList<>();
    private LanguageSpinnerAdapter spinnerAdapter;
    private MakeRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        queue = MyRequestQueue.getInstance(context);

        spinner = findViewById(R.id.spinnerId);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = parent.getItemAtPosition(position).toString();
                setLanguageABBR(language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLanguage = "en";
            }
        });
        translatedEditText = findViewById(R.id.translatedTextId);
        EditText enterText = findViewById(R.id.textId);
        enterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enteredText = s.toString();
            }
        });

        ImageButton translateButton = findViewById(R.id.translateButtonId);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = new MakeRequest(context);
                request.execute(enteredText);
            }
        });

        if (dirs.size() == 0 && langs.size() == 0) {
            request = new MakeRequest(context);
            request.execute(enteredText);
        }
    }

    private void doSpinnerStuff() {
        if (supportedLanguagesList.size() > 0) {
            spinnerAdapter = new LanguageSpinnerAdapter(context, R.layout.spinner_row, R.id.spinnerRowId, supportedLanguagesList);
            spinner.setAdapter(spinnerAdapter);
        }
        spinner.setSelection(supportedLanguagesList.indexOf("English"));
        setLanguageABBR("English");
    }

    private void setLanguageABBR(String language) {
        for (Map.Entry<String, String> entry : langs.entrySet()) {
            if (entry.getValue().equals(language)) {
                selectedLanguage = entry.getKey();
            }
        }
    }


    public class MakeRequest extends AsyncTask<String, Void, String> {
        Context context;
        Parser parser = new Parser();

        MakeRequest(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String text = strings[0];
            StringRequest request = new StringRequest(Request.Method.GET, queue.getLangsURL(selectedLanguage),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (dirs.size() == 0 && langs.size() == 0) {
                                dirs = parser.parseDirs(response);
                                langs = parser.parseLangs(response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TranslatorLog", "onErrorResponse: " + error);
                }
            });
            StringRequest translationRequest = new StringRequest(Request.Method.GET, queue.getTranslationURL(text, selectedLanguage),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("translation", "onResponse: I'm here");
                        translatedText = parser.parseText(response);
                        supportedLanguagesList = parser.getSupportedLanguageList();
                        doSpinnerStuff();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TranslatorLog", "onErrorResponse: " + error);
                    }
                });
            queue.addRequest(request);
            queue.addRequest(translationRequest);
            return translatedText;
        }

        @Override
        protected void onPostExecute(String string) {
            translatedEditText.setText(string);
        }
    }
}
