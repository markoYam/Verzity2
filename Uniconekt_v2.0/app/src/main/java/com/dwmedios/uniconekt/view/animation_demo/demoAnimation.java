package com.dwmedios.uniconekt.view.animation_demo;

public class demoAnimation implements android.view.animation.Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;

    public demoAnimation(double mAmplitude, double mFrequency) {
        this.mAmplitude = mAmplitude;
        this.mFrequency = mFrequency;
    }

    @Override
    public float getInterpolation(float v) {
        return (float) (-1 * Math.pow(Math.E, -v/ mAmplitude) *
                Math.cos(mFrequency * v) + 1);
    }
}
