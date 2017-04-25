package de.neltaprogramming.drivemanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;


public class SettingsDriveAdministrationActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;

    private final String PATH_KEY = "drive_administration_path";
    private EditText pathEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_drive_administration);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        pathEditText = (EditText) findViewById(R.id.path_drive_administration);
        pathEditText.setText(sharedPreferences.getString(PATH_KEY, "/"));
        pathEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedPreferences.edit().putString("drive_administration_path", s.toString()).apply();
                Log.e("TEST", "afterTextChanged");
            }
        });

        ImageButton browsePathButton = (ImageButton) findViewById(R.id.browse_path_button);
        browsePathButton.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.browse_path_button:
                showChooser();
                break;
        }
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    private static final String TAG = "FileChooserExample";

    private static final int REQUEST_CODE = 6384;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.e(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            pathEditText.setText(uri.toString());
                            Toast.makeText(SettingsDriveAdministrationActivity.this,
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
