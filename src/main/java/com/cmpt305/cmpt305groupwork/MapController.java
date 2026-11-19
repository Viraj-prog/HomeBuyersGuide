package com.cmpt305.cmpt305groupwork;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MapController {
    @FXML
    private StackPane mapContainer;
    private MapView mapView;

    @FXML
    public void initialize() {
        ArcGISRuntimeEnvironment.setApiKey("AAPTapEH2Gu-C-xBKvwRx6eMV5g..bXg_gMpe1sK7HlQsOL9VVIXC3WKlicJotIpx77s8HcunpVFh4L8bsmsgpz5-8KygH6hAAo8KV69QidrlrGTBGf9ZBWnTqKi7v9MVxX3R979bdjTLCURXIzDM9NS_64RG0_axzIAGawPZCxoMLonsmBGpOcfW0-Q4siAhnH0g-inp4P5I0wkLWlefBv97KMfWE5tkEAwDy7prIBDrUhxQ7FqgN3OxkrEmZc2WeeVuYFVvlob51vTO1w..AT1_mQo2Vkbr");
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_STREETS);
        map.setInitialViewpoint(new Viewpoint(53.5461, -113.4938, 200000));
        mapView = new MapView();
        mapView.setMap(map);
        mapContainer.getChildren().add(mapView);
    }
}
