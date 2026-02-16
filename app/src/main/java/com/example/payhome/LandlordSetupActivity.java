package com.example.payhome;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myhome.R;

public class LandlordSetupActivity extends AppCompatActivity {

    private EditText editTextNumberOfHouses;
    private EditText editTextHouseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_setup);

        editTextNumberOfHouses = findViewById(R.id.edit_text_number_of_houses);
        editTextHouseName = findViewById(R.id.edit_text_house_name);
        Button buttonSaveHouse = findViewById(R.id.button_save_house);

        buttonSaveHouse.setOnClickListener(v -> {
            String numberOfHouses = editTextNumberOfHouses.getText().toString();
            String houseName = editTextHouseName.getText().toString();
            // Handle saving the house information here
        });
    }
}
