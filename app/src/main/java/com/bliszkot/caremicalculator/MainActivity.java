package com.bliszkot.caremicalculator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    int p, dp, term;
    double r;
    ComputeEMIInterface computeEMIInterface;
    ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            computeEMIInterface = ComputeEMIInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText pText = findViewById(R.id.pText);
        EditText dpText = findViewById(R.id.dpText);
        EditText iText = findViewById(R.id.interestText);
        EditText termText = findViewById(R.id.loanTermText);
        Button calculateButton = findViewById(R.id.calculateEMI);
        TextView resultText = findViewById(R.id.emiText);

        Intent computeEMIIntent = new Intent(this, ComputeEMIService.class);
        bindService(computeEMIIntent, myServiceConnection, Context.BIND_AUTO_CREATE);

        calculateButton.setOnClickListener(v -> {
            resultText.setText("");
            Boolean flag = false;
            String validP, validDp, validIn, validTerm;

            validP = pText.getText().toString();
            validDp = dpText.getText().toString();
            validIn = iText.getText().toString();
            validTerm = termText.getText().toString();

            if (isNotEmpty(validP, validDp, validIn, validTerm)) {
                p = Integer.parseInt(validP);
                dp = Integer.parseInt(validDp);
                r = Double.parseDouble(validIn);
                term = Integer.parseInt(validTerm);
                flag = true;
            }

            if (flag) {
                if (p == dp) {
                    resultText.setText("0");
                } else if (dp > p) {
                    Toast.makeText(this,
                            "Principal amount must be greater than Down Payment.",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        int result = computeEMIInterface.computeEMI(p, dp, r, term);
                        String resultString = "₨ " + result;
                        resultText.setText(resultString);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Boolean isNotEmpty(String p, String dp, String in, String term) {
        if (p.isEmpty() || dp.isEmpty() || in.isEmpty() || term.isEmpty()) {
            Toast.makeText(this,
                    "All fields must be filled.",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}