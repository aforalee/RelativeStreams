package net.chaiapp.mware2_alerts;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class StockInfo {
    private static Cluster cluster;
    private static Session session;
    public final ResultSet value;

    private static Cluster connect(String node) {
        return Cluster.builder().addContactPoint(node).build();
    }

    public StockInfo(ResultSet theValue) {
        value = theValue;
    }

    public static StockInfo fetch(String keyspace) {
        cluster = connect("127.0.0.1");
        session = cluster.connect();
        session.execute("USE alert_KS;");
//        session.execute("INSERT INTO alert_KS.user2 (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");
        ResultSet results = session.execute("select * from alert_ks.user2 where executed = false ALLOW FILTERING");
        session.close();
        cluster.close();
        return new StockInfo(results);
    }
}
