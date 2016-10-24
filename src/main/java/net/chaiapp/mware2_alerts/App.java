package net.chaiapp.mware2_alerts;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import rx.Observable;
import rx.Subscriber;

public class App {
    private static Cluster cluster;
    private static Session session;

    private static Cluster connect(String node) {
        return Cluster.builder().addContactPoint(node).build();
    }

    public static void main1() {

        session.execute("CREATE KEYSPACE alert_KS WITH REPLICATION={'class':'SimpleStrategy', 'replication_factor':1};");
        session.execute("CREATE TABLE alert_KS.user2(firstname text,lastname text,email text,age int,city text,PRIMARY KEY(lastname));");

    }

    public static void insert() {
        for (int i = 1; i < 300; i++) {
//            session.execute("INSERT INTO alert_KS.user2 (lastname, age, city, email, firstname) VALUES ("+"'ab + i + '," + 35 + i + ",'Islamabad" + i + "','bob" + i + "@example.com','Ali" + i + "')");
            session.execute("INSERT INTO alert_KS.user2 (lastname, age, city, email, firstname) VALUES ('Jones', 35, 'Austin', 'bob@example.com', 'Bob')");

        }
    }

    public static void view() {
        ResultSet results = session.execute("SELECT * FROM alert_KS.user2");

        for (Row row : results) {
            System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
        }
    }

    public static void update() {
        // Update the same user with a new age
        session.execute("update alert_KS.user2 set age = 36 where lastname = 'Jones'");
        // Select and show the change
        ResultSet results = session.execute("select * from alert_KS.user2 where lastname='Jones'");
        for (Row row : results) {
            System.out.format("%s %d\n", row.getString("firstname"), row.getInt("age"));
        }
    }

    public static void delete() {

        // Delete the user from the users table
        session.execute("DELETE FROM alert_KS.user2 WHERE lastname = 'Jones'");
        // Show that the user is gone
        ResultSet results = session.execute("SELECT * FROM alert_KS.user2");
        for (Row row : results) {
            System.out.format("%s %d %s %s %s\n", row.getString("lastname"), row.getInt("age"), row.getString("city"), row.getString("email"), row.getString("firstname"));
        }
    }

    public static void main(String[] args) {
        String KEYSPACE = "alert_KS";
        Observable<StockInfo> feed = StockServer.getFeed(KEYSPACE);
        feed.subscribe(new Subscriber<StockInfo>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(StockInfo stockInfo) {
                ResultSet resultSet = stockInfo.value;
                for (Row row : resultSet) {
                    if(!row.getBool("executed")){
                        System.out.println(row.getString("lastname"));
                        session.execute("update alert_KS.user2 set executed = true where lastname = '"+row.getString("lastname")+"'");
                    }
                }
            }

        });
    }
}
