package com.cmpt305.cmpt305groupwork;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/*
    A map that draws one red pin for every PropertyAssessment it is given.

    Usage (from MainApplication):
        MapPane mapPane = new MapPane();
        mapPane.setProperties(listOfPropertiesShownInTheCards);

    Call setProperties again whenever the list shown in the card view changes,
    and the pins will follow.

    The ArcGIS API key is read from a local file named api.properties
    (one line:  arcgis.key=YOUR_KEY). That file is ignored by git.
 */
public class MapPane extends StackPane {

    private static final String KEY_FILE = "api.properties";
    private static final String KEY_NAME = "arcgis.key";

    // Edmonton, used when there is nothing to pin
    private static final double START_LAT = 53.5461;
    private static final double START_LON = -113.4938;
    private static final double START_SCALE = 200000;

    private MapView mapView;
    private GraphicsOverlay pinLayer;
    private SimpleMarkerSymbol pinSymbol;

    public MapPane() {
        String key = loadApiKey();
        if (key == null) {
            // Don't crash the whole app if a teammate has no key file yet.
            getChildren().add(new Label(
                    "Map unavailable: create " + KEY_FILE + " containing " + KEY_NAME + "=YOUR_KEY"));
            return;
        }

        ArcGISRuntimeEnvironment.setApiKey(key);

        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
        map.setInitialViewpoint(new Viewpoint(START_LAT, START_LON, START_SCALE));

        mapView = new MapView();
        mapView.setMap(map);

        // Red circle, size 8. Colour is ARGB: 0xFFFF0000 = opaque red.
        pinSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 8);

        pinLayer = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(pinLayer);

        getChildren().add(mapView);
    }

    // Replace all pins with one pin per property in the list.
    public void setProperties(List<PropertyAssessment> properties) {
        if (mapView == null) {
            return; // map failed to start (no key)
        }

        pinLayer.getGraphics().clear();

        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;
        int count = 0;

        for (PropertyAssessment p : properties) {
            double lat = p.getLatitude();
            double lon = p.getLongitude();
            if (lat == 0 && lon == 0) {
                continue; // missing coordinates
            }

            // ArcGIS points are (x = longitude, y = latitude)
            Point point = new Point(lon, lat, SpatialReferences.getWgs84());
            pinLayer.getGraphics().add(new Graphic(point, pinSymbol));

            minLat = Math.min(minLat, lat);
            maxLat = Math.max(maxLat, lat);
            minLon = Math.min(minLon, lon);
            maxLon = Math.max(maxLon, lon);
            count++;
        }

        zoomToPins(count, minLat, maxLat, minLon, maxLon);
    }

    private void zoomToPins(int count, double minLat, double maxLat, double minLon, double maxLon) {
        if (count == 0) {
            mapView.setViewpointAsync(new Viewpoint(START_LAT, START_LON, START_SCALE));
        } else if (count == 1) {
            mapView.setViewpointAsync(new Viewpoint(minLat, minLon, 5000));
        } else {
            Envelope box = new Envelope(minLon, minLat, maxLon, maxLat, SpatialReferences.getWgs84());
            mapView.setViewpointGeometryAsync(box, 50); // 50 px padding around the pins
        }
    }

    // Call from Application.stop() so the map releases its resources.
    public void dispose() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    private static String loadApiKey() {
        Properties props = new Properties();
        try (FileReader reader = new FileReader(KEY_FILE)) {
            props.load(reader);
        } catch (IOException e) {
            return null;
        }
        String key = props.getProperty(KEY_NAME);
        return (key == null || key.isBlank()) ? null : key.trim();
    }
}