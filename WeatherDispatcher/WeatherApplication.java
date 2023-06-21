package WeatherDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface WeatherObserver {
    void update(float temperature, float humidity, float pressure);
}

class WeatherDispatcher {
    private List<WeatherObserver> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherDispatcher() {
        this.observers = new ArrayList<>();
    }

    public void register(WeatherObserver observer) {
        observers.add(observer);
    }

    public void remove(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }
}

class CurrentConditionsDisplay implements WeatherObserver {
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    private void display() {
        System.out.println("Temperature: " + temperature + "F");
        System.out.println("Humidity: " + humidity + "%");
    }
}

class ForecastDisplay implements WeatherObserver {
    private float previousPressure;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        weatherDispatcher.register(this);
        previousPressure = 0.0f;
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        displayForecast(pressure);
        previousPressure = pressure;
    }

    private void displayForecast(float currentPressure) {
        System.out.print("Forecast: ");
        if (currentPressure > previousPressure) {
            System.out.println("Improving");
            System.out.printf("\n");
        } else if (currentPressure == previousPressure) {
            System.out.println("Same");
            System.out.printf("\n");
        } else {
            System.out.println("Cooler");
            System.out.printf("\n");
        }
    }
}

public class WeatherApplication {
    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }
            }
        }
    }
}

