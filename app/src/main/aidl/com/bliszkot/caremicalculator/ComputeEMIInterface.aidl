// ComputeEMIInterface.aidl
package com.bliszkot.caremicalculator;

// Declare any non-default types here with import statements

interface ComputeEMIInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int computeEMI(int principalAmount, int downPayment, double rate, int loanTermInMonths);
}