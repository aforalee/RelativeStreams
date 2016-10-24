//package net.chaiapp.mware2_alerts;
//
//import com.datastax.driver.core.Cluster;
//import com.datastax.driver.core.Session;
//
//public class CassandraConnector {
//    private static Cluster cluster;
//    private static Session session;
//
//    private static Cluster connect(String node) {
//        return Cluster.builder().addContactPoint(node).build();
//    }
//    public static void main() {
//        cluster = connect("127.0.0.1");
//        session = cluster.connect();
//        session.execute("CREATE KEYSACE alert_KS WITH REPLICATION=" +
//                "{'class':'SimpleStrategy', 'replicationfactor':1};");
//        session.execute("USE alert_KS;");
//        session.close();
//        cluster.close();
//    }
//}
