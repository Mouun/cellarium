package mapfoursquare;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.Photo;
import fi.foyt.foursquare.api.entities.PhotoGroup;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

import java.io.IOException;

import static mapfoursquare.MapUtils.geolocalisationString;

public class ParserFoursquare {

    private FoursquareApi foursquareApi;
    private static String ll = "43.6863732,7.2329360000000005";



    public ParserFoursquare(Boolean geolocalized) {
        this.foursquareApi = new FoursquareApi("V5HMJJ3KRCH3T053QWHGST2PIDWEH44H2JUQCGKZ5RKHWICP", "P4QQSTEJKPFGJI3P2P2PXNQ5MINLEH5WEXKTIBELFV15030H", "");
        foursquareApi.setVersion("20170801");

        if (geolocalized) {

            try {
                ll = geolocalisationString();
            } catch (IOException | GeoIp2Exception e) {
                e.printStackTrace();
            }
        }
    }



    public Result<VenuesSearchResult> search() throws FoursquareApiException {
        return foursquareApi.venuesSearch(ll, "cave vin", 10, "checkin", null, null, null, null);
    }

    public String getPhotosVenues (String venueId) throws FoursquareApiException {
        Result<PhotoGroup> ret = foursquareApi.venuesPhotos(venueId, null, 3, 0);

        System.out.println(ret);

        String str = "";
        for (Photo photo : ret.getResult().getItems()) {
            str += "<img src=\"" + photo.getUrl() + "\n>";
        }
        return str;
    }



    /*public Result<VenuesSearchResult> searchWithLocation(String ll) throws FoursquareApiException {
     return foursquareApi.venuesSearch(ll, "cave vin", 10, "checkin", null, null, null, null);
     }*/


}
