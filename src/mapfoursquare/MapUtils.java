package mapfoursquare;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public abstract class MapUtils {

    public static Double[] geolocalisation() throws IOException, GeoIp2Exception {

        Double [] coordinates = {0.0,0.0};

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);

        File database = new File("GeoLite2-City.mmdb");
        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

        CityResponse response = dbReader.city(InetAddress.getByName(ip));



        coordinates[0] = response.getLocation().getLatitude();
        coordinates[1] = response.getLocation().getLongitude();

        return coordinates;
    }

    // Cette fonction de flemmard mdrrr
    public static String geolocalisationString() throws IOException, GeoIp2Exception {
        Double [] temp = geolocalisation();

        return temp[0].toString() + "," + temp[1].toString();
    }
}
