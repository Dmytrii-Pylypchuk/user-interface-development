package com.example.texteditor;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    EditText editor;
    TextView charCount;
    MaterialToolbar toolbar;

    boolean isChanged = false;

    Uri currentFileUri = null;

    final int OPEN_FILE = 100;
    final int CREATE_FILE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        charCount = findViewById(R.id.charCount);
        toolbar = findViewById(R.id.topAppBar);
        FloatingActionButton saveButton = findViewById(R.id.saveButton);

        // Toolbar menu
        toolbar.setOnMenuItemClickListener(this::onMenuClick);

        // Save button
        saveButton.setOnClickListener(v -> saveFile());

        // Text change listener
        editor.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
                charCount.setText("Characters: " + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                checkUnsaved(() -> finish());
            }
        });
    }

    private boolean onMenuClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new) {
            checkUnsaved(() -> {
                editor.setText("");
                toolbar.setTitle("Text Editor");
                currentFileUri = null;
            });
            return true;
        } else if (id == R.id.action_open) {
            checkUnsaved(this::openFilePicker);
            return true;
        } else if (id == R.id.action_exit) {
            checkUnsaved(this::finish);
            return true;
        }

        return false;
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, OPEN_FILE);
    }

    private void saveFile() {
        if (currentFileUri == null) {
            // Новий файл – виклик Create Document
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt");
            startActivityForResult(intent, CREATE_FILE);
            return;
        }

        writeToFile(currentFileUri);
    }

    private void writeToFile(Uri uri) {
        try {
            OutputStream output = getContentResolver().openOutputStream(uri);
            output.write(editor.getText().toString().getBytes());
            output.close();
            isChanged = false;
            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) return;

        if (requestCode == OPEN_FILE) {
            currentFileUri = data.getData();
            readFile(currentFileUri);
        }

        if (requestCode == CREATE_FILE) {
            currentFileUri = data.getData();
            writeToFile(currentFileUri);
        }
    }

    private void readFile(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
            editor.setText(text.toString());
            toolbar.setTitle("Editing file");
            isChanged = false;
            Toast.makeText(this, "File opened", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error opening file", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUnsaved(Runnable action) {
        if (!isChanged) {
            action.run();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Save changes before continuing?")
                .setPositiveButton("Save", (dialog, which) -> {
                    saveFile();
                    action.run();
                })
                .setNegativeButton("Discard", (dialog, which) -> {
                    isChanged = false;
                    action.run();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}