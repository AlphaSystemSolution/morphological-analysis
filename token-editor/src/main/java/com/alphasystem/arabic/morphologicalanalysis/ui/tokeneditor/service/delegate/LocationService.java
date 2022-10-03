package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.delegate;

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location;
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory;
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.LocationRequest;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import scala.collection.immutable.Seq;

public class LocationService extends Service<Seq<Location>> {

    private final CacheFactory cacheFactory;
    private final LocationRequest request;

    public LocationService(final CacheFactory cacheFactory,
                           final LocationRequest request) {
        this.cacheFactory = cacheFactory;
        this.request = request;
    }

    @Override
    protected Task<Seq<Location>> createTask() {
        return new Task<Seq<Location>>() {

            @Override
            protected Seq<Location> call() throws Exception {
                return cacheFactory.locations().get(request);
            }
        };
    }
}
