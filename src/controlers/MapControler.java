package controlers;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import mapfoursquare.MapUtils;
import mapfoursquare.ParserFoursquare;
import netscape.javascript.JSObject;


public class MapControler implements Initializable, MapComponentInitializedListener {

    @FXML
    private Button button;

    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;
    private Double lat = 43.6863732;
    private Double lng = 7.2329360000000005;
    private ParserFoursquare parserFoursquare;

    private Boolean demo = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }



    @Override
    public void mapInitialized() {

        parserFoursquare = new ParserFoursquare(demo);
        Result <VenuesSearchResult> result = null;
        ArrayList<LatLong> latLongList = new ArrayList<>();
        ArrayList<String> info = new ArrayList<>();

        try {
            result = parserFoursquare.search();
        } catch (FoursquareApiException e) {
            e.printStackTrace();
        }



        if (result.getMeta().getCode() == 200) {
            // if query was ok we can finally we do something with the data


            for (CompactVenue venue : result.getResult().getVenues()) {

                latLongList.add(new LatLong(venue.getLocation().getLat(), venue.getLocation().getLng()));
                try {
                    info.add(constructPOPUP(venue));
                } catch (FoursquareApiException e) {
                    e.printStackTrace();
                }
            }

// Nom, location, contact, Site web, price, rating, photos

            //Set the initial properties of the map.
            MapOptions mapOptions = new MapOptions();

            if (demo) {
                try {
                    Double [] temp = MapUtils.geolocalisation();
                    lat = temp[0];
                    lng = temp[1];
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GeoIp2Exception e) {
                    e.printStackTrace();
                }
            }

            mapOptions.center(new LatLong(lat, lng))
                    .overviewMapControl(false)
                    .panControl(false)
                    .rotateControl(false)
                    .scaleControl(false)
                    .streetViewControl(false)
                    .zoomControl(true)
                    .zoom(12);

            map = mapView.createMap(mapOptions);

            //Add markers to the map

            for (int i = 0; i < latLongList.size(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLongList.get(i));
                markerOptions.animation(Animation.DROP);
                Marker marker = new Marker(markerOptions);
                map.addMarker(marker);

                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content(info.get(i));
                InfoWindow testWindow = new InfoWindow(infoWindowOptions);
                testWindow.setOptions(infoWindowOptions);

                map.addUIEventHandler(marker, UIEventType.click, (JSObject obj) -> {
                    testWindow.open(map, marker);
                });
            }




        } else {
            System.out.println("Error occured: ");
            System.out.println("  code: " + result.getMeta().getCode());
            System.out.println("  type: " + result.getMeta().getErrorType());
            System.out.println("  detail: " + result.getMeta().getErrorDetail());
        }

    }

    private String constructPOPUP(CompactVenue venue) throws FoursquareApiException {

        String adresse = "Informations indisponibles (magasins probablement fermé définitivement)";
        String url = "Informations indisponibles";
        String prix = "Informations indisponibles";
        String note = "Informations indisponibles";

        if ( venue.getLocation().getAddress() != null && venue.getLocation().getPostalCode() != null && venue.getLocation().getCity() != null) {
            adresse = venue.getLocation().getAddress()+" "+ venue.getLocation().getPostalCode()+" "+venue.getLocation().getCity();
        }

        if (venue.getUrl() != null) {
            url = venue.getUrl();
        }

        if (venue.getPrice() != null) {
            prix = venue.getUrl();
        }

        if (venue.getRating() != null) {
            note = venue.getUrl();
        }



        String  ret = "<h2>"+ venue.getName() + "</h2>"
                + "Adresse : " + adresse + "<br>"
                + "Site web du vendeur : " + url + "<br>"
                + "Prix (1 = pas couteux, 4 = très couteux) : " + prix + "<br>"
                + "Note (sur 10) : " + note + "<br>"
                + parserFoursquare.getPhotosVenues(venue.getId());


        return ret;


    }
}
