package com.project.megaconvertor;

import java.util.HashMap;

/**
 * Created by Andrew on 4/11/2018.
 */

public class UnitConvertor {

    private HashMap<String,HashMap<String, Double>> units;

    public UnitConvertor() {
        units = new HashMap<>();

        HashMap<String, Double> lengthMap = new HashMap<>();
        lengthMap.put("Millimeter", 1.0);
        lengthMap.put("Centimeter", 10.0);
        lengthMap.put("Meter", 1000.0);
        lengthMap.put("Kilometer", 1000000.0);
        units.put("Length", lengthMap);

        HashMap<String, Double> timeMap = new HashMap<>();
        timeMap.put("Second", 1.0);
        timeMap.put("Minute", 60.0);
        timeMap.put("Hour", 3600.0);
        timeMap.put("Day", 86400.0);
        units.put("Time", timeMap);

        HashMap<String, Double> weightMap = new HashMap<>();
        weightMap.put("Milligram", 1.0);
        weightMap.put("Gram", 1000.0);
        weightMap.put("Kilogram", 1000000.0);
        weightMap.put("Tonne", 1000000000.0);
        units.put("Weight", weightMap);

        HashMap<String, Double> temperatureMap = new HashMap<>();
        temperatureMap.put("Celsius", 1.0);
        temperatureMap.put("Kelvin", 33.8);
        temperatureMap.put("Fahrenheit", 274.15);
        units.put("Temperature", temperatureMap);

    }

    public double convert(String unit, String from, String to, double value) {
        return value * units.get(unit).get(from) / units.get(unit).get(to);
    }

    public double convertTemperature(String from, String to, double value) {
        switch (from) {
            case "Celsius":
                switch (to) {
                    case "Fahrenheit":
                        return value * 9.0 / 5.0 + 32;
                    case "Kelvin":
                        return value + 273.15;
                }
                break;
            case "Fahrenheit":
                switch (to) {
                    case "Celsius":
                        return (value - 32) * 5.0 / 9.0;
                    case "Kelvin":
                        // (T(°F) + 459.67)× 5/9
                        return (value + 459.67) * 5.0 / 9.0;
                }
                break;
            case "Kelvin":
                switch (to) {
                    case "Celsius":
                        return value - 273.15;
                    case "Fahrenheit":
                        // T(°F) = T(K) × 9/5 - 459.67
                        return value * 9.0 / 5.0 - 459.67;
                }
        }
    return value;
    }

}
