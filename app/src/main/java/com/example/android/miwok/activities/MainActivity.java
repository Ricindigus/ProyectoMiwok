/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.miwok.R;

public class MainActivity extends AppCompatActivity {

    TextView txtFamily;
    TextView txtColors;
    TextView txtPhrases;
    TextView txtNumbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        conectarVistas();

        txtNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NumbersActivity.class);
                startActivity(intent);
            }
        });

        txtFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FamilyActivity.class);
                startActivity(intent);
            }
        });

        txtPhrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PhrasesActivity.class);
                startActivity(intent);
            }
        });

        txtColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ColorsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void conectarVistas(){
        txtColors = findViewById(R.id.colors);
        txtPhrases = findViewById(R.id.phrases);
        txtNumbers= findViewById(R.id.numbers);
        txtFamily = findViewById(R.id.family);

    }
}
