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
    private final String SHARED_PREFS = "sharedPrefs";
    private Button datePickerButton;
    private Button actionButton;
    private TextView textViewOutput;
    private SharedPreferences prefs;
    private int year, month, day;
    private String output;
    private boolean dataPicking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        textViewOutput = findViewById(R.id.textViewOutput);
        datePickerButton = findViewById(R.id.datePickerButton);
        actionButton = findViewById(R.id.actionButton);
        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        loadDates();
        loadOutput();
        initDatePicker();
        actionButton();
    }

    private void initDatePicker() {
        datePickerButton.setOnClickListener(v -> {
            dataPicking = true;
            DatePickerDialog datePickerDialog = new DatePickerDialog
                    (MainActivity.this, android.R.style.Theme_Material_Dialog,
                            (view, yearSelected, monthSelected, daySelected) -> {
                                year = yearSelected;
                                month = monthSelected;
                                day = daySelected;
                            }, year, month, day);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("year", year);
            editor.putInt("month", month);
            editor.putInt("day", day);
            editor.apply();
            datePickerDialog.show();
            datePickerDialog.updateDate(year, month, day);
        });
    }

    private void actionButton() {
        actionButton.setOnClickListener(v -> {
            if (checkIsPassed()) showGeneration();
        });
    }

    private boolean checkIsPassed() {
        if (!dataPicking) {
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
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("output", output);
        editor.apply();
    }

    private void loadDates() {
        final Calendar calendar = Calendar.getInstance();
        year = prefs.getInt("year", calendar.get(Calendar.YEAR));
        month = prefs.getInt("month", calendar.get(Calendar.MONTH));
        day = prefs.getInt("day",calendar.get(Calendar.DATE));
    }

    private void loadOutput() {
        output = prefs.getString("output", "nodata");
        if (output.equals("nodata")) {
            textViewOutput.setVisibility(View.INVISIBLE);
        } else {
            textViewOutput.setText(output);
            textViewOutput.setVisibility(View.VISIBLE);
        }
    }
}
