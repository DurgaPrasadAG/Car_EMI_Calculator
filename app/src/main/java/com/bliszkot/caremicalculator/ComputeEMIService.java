package com.bliszkot.caremicalculator;

import static com.bliszkot.caremicalculator.ComputeEMIInterface.Stub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class ComputeEMIService extends Service {

    Stub myBinder = new Stub() {
        @Override
        public int computeEMI(int principalAmount, int downPayment, double rate, int loanTermInMonths) throws RemoteException {
            if (rate == 0) {
                principalAmount = principalAmount - downPayment;
                return principalAmount * loanTermInMonths;
            } else {
                principalAmount = principalAmount - downPayment;
                rate = (rate / 12) / 100;
                double emi = principalAmount * rate *
                        (Math.pow(1 + rate, loanTermInMonths)) /
                        (Math.pow(1 + rate, loanTermInMonths) - 1);
                emi = Math.round(emi);
                int result = (int) emi;
                return result;
            }
        }
    };

    public ComputeEMIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
}