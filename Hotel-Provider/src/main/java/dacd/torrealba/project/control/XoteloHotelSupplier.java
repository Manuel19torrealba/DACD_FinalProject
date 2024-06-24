package dacd.torrealba.project.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.torrealba.project.model.Hotel;
import dacd.torrealba.project.model.HotelInformation;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XoteloHotelSupplier implements HotelSupplier {
    private final String apiURL;

    public XoteloHotelSupplier() {
        this.apiURL = "https://data.xotelo.com/api/rates";
    }

    @Override
    public List<Hotel> getHotel(HotelInformation hotelInformation) {

        List<Hotel> hotelList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            LocalDate checkInDate = LocalDate.now().plusDays(i);
            LocalDate checkOutDate = checkInDate.plusDays(1);

            String apiUrl = buildApiUrl(hotelInformation, checkInDate, checkOutDate);

            try {
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(apiUrl);

                HttpResponse response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String jsonResponse = EntityUtils.toString(response.getEntity());
                    hotelList.addAll(parseJsonResponse(jsonResponse, hotelInformation, checkInDate, checkOutDate));
                }

            } catch (IOException e) {
                throw new RuntimeException("Error al obtener hoteles desde la API de Xoleto", e);
            }
        }
        return hotelList;
    }

    private List<Hotel> parseJsonResponse(String jsonResponse, HotelInformation hotelInformation, LocalDate checkInDate, LocalDate checkOutDate) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        JsonObject resultObject = jsonObject.getAsJsonObject("result");
        JsonArray rates = resultObject.getAsJsonArray("rates");

        List<Hotel> hotelList = new ArrayList<>();

        for (JsonElement element : rates) {
            JsonObject rateObject = element.getAsJsonObject();
            String name = rateObject.get("name").getAsString();
            Double rate = rateObject.get("rate").getAsDouble();
            Double tax = rateObject.get("tax").getAsDouble();

            HotelInformation hotelInfo = new HotelInformation(
                    hotelInformation.getName(),
                    hotelInformation.getIsland(),
                    hotelInformation.getLocation(),
                    hotelInformation.getKey()
            );

            hotelList.add(new Hotel(hotelInfo, checkInDate.toString(), checkOutDate.toString(), name, rate + tax));
        }

        return hotelList;
    }


    private String buildApiUrl (HotelInformation hotelInformation, LocalDate chk_In, LocalDate chk_Out) {
        String hotelKey = "?hotel_key=" + hotelInformation.getKey();
        String check_In = "&chk_in=" + chk_In;
        String check_Out = "&chk_out=" + chk_Out;
        return apiURL + hotelKey + check_In + check_Out;
    }
}
