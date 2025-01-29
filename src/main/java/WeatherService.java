import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class WeatherService {
    private static final String API_KEY = "..."; // ВСТАВИТЬ КЛЮЧ
    private static final String URL = "https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62&limit=7";

    public static void main(String[] args) {
        try {
            String jsonResponse = getWeatherData();
            System.out.println("Ответ от сервера: " + jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            int temperature = jsonObject.getJSONObject("fact").getInt("temp");
            System.out.println("Текущая температура: " + temperature + "°C");

            double averageTemp = calculateAverageTemperature(jsonObject);
            System.out.println("Средняя температура за период: " + averageTemp + "°C");

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String getWeatherData() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("X-Yandex-Weather-Key", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static double calculateAverageTemperature(JSONObject jsonObject) {
        int sumTemp = 0;
        int count = 0;

        for (Object day : jsonObject.getJSONArray("forecasts")) {
            JSONObject forecast = (JSONObject) day;
            int dayTemp = forecast.getJSONObject("parts").getJSONObject("day").getInt("temp_avg");
            sumTemp += dayTemp;
            count++;
        }

        return count > 0 ? (double) sumTemp / count : 0;
    }
}
