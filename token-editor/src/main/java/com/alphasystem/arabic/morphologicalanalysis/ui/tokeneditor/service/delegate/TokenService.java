package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.delegate;

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location;
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token;
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.CacheFactory;
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.TokenRequest;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import scala.collection.immutable.Seq;

public class TokenService extends Service<Seq<Token>> {

    private final CacheFactory cacheFactory;
    private final TokenRequest request;

    public TokenService(final CacheFactory cacheFactory,
                           final TokenRequest request) {
        this.cacheFactory = cacheFactory;
        this.request = request;
    }

    @Override
    protected Task<Seq<Token>> createTask() {
        return new Task<Seq<Token>>() {

            @Override
            protected Seq<Token> call() throws Exception {
                return cacheFactory.tokens().get(request);
            }
        };
    }
}
