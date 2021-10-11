package com.epam.task;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    private Button datePickerButton;
    private Button actionButton;
    private TextView textViewOutput;
    private SharedPreferences prefs;
    private int year;
    private String output;
    private boolean dataSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        textViewOutput = findViewById(R.id.textViewOutput);
        datePickerButton = findViewById(R.id.datePickerButton);
        actionButton = findViewById(R.id.actionButton);
        loadOutput();
        initDatePicker();
        ActionButton();
    }

    private void initDatePicker() {
        datePickerButton.setOnClickListener(v -> {
            dataSelected = true;
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog
                    (MainActivity.this, android.R.style.Theme_Material_Dialog,
                            (view, yearSelected, monthSelected, dayOfMonth) -> setYear(yearSelected), year, month, day);
            datePickerDialog.show();
        });
    }

    private void setYear(int year) {
        this.year = year;
    }

    private void ActionButton() {
        actionButton.setOnClickListener(v -> {
            if (checkIsPassed()) showGeneration();
        });
    }

    private boolean checkIsPassed() {
        if (!dataSelected) {
            StyleableToast.makeText(MainActivity.this, "Необходимо выбрать дату рождения", Toast.LENGTH_LONG, R.style.blacktoast).show();
            return false;
        }
        if (year < 1946 || year > 2012) {
            StyleableToast.makeText(MainActivity.this, "Укажите год рождения в этом временном промежутке 1946 - 2012 г", Toast.LENGTH_LONG, R.style.blacktoast).show();
            return false;
        }
        return true;
    }

    private void showGeneration() {
        if (year >= 1946 && year <= 1964) output = "бэби-бумеры";

        if (year >= 1965 && year <= 1980) output = "поколение Х";

        if (year >= 1981 && year <= 1996) output = "поколение Y";

        if (year >= 1997 && year <= 2012) output = "поколение Z";

        textViewOutput.setText(output);
        textViewOutput.setVisibility(View.VISIBLE);
        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("output", output);
        editor.apply();
    }

    private void loadOutput() {
        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        output = prefs.getString("output", "nodata");
        if (output.equals("nodata")) {
            textViewOutput.setVisibility(View.INVISIBLE);
        } else {
            textViewOutput.setText(output);
            textViewOutput.setVisibility(View.VISIBLE);
        }
    }
}