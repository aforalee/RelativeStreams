package net.chaiapp.mware2_alerts;

import rx.Observable;

public class StockServer {

    static public Observable<StockInfo> getFeed(final String keyspace) {
        return Observable.create(subcriber -> {
            while(true){subcriber.onNext(StockInfo.fetch(keyspace));}
        });
    }

}
