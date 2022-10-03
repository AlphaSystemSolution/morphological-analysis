package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.delegate;

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter;
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import scala.Option;
import scala.collection.immutable.Seq;

public class ChapterService extends Service<Seq<Chapter>> {

    private final CacheFactory cacheFactory;

    public ChapterService(final CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Override
    protected Task<Seq<Chapter>> createTask() {
        return new Task<Seq<Chapter>>() {
            @Override
            protected Seq<Chapter> call() throws Exception {
                return cacheFactory.chapters().get(Option.empty());
            }
        };
    }
}
