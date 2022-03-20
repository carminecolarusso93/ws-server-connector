package it.unisannio.websocket.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColarussoTester {
    private static final Pattern pattern = Pattern.compile("(areaName=)([0-9a-zA-Z-_]+)");
    private static final Pattern pattern2 = Pattern.compile("(linkid=)(\\d+)");
    private static final int REGEX_OFFSET = "areaname\":".length();
    private static String message = "SinkRecord{kafkaOffset=233565, timestampType=CreateTime} ConnectRecord{topic='Lyon_2e_Arrondissement-Northbound', kafkaPartition=0, key=null, keySchema=null, value={sdTravelTime=0.0, aggPeriod=179000, linkid=12500009458457, domainAggTimestamp=1536188938000, areaName=Lyon_2e_Arrondissement, avgTravelTime=19.50983963012695, numVehicles=1, aggTimestamp=1647430623994}, valueSchema=null, timestamp=1647430626590, headers=ConnectHeaders(headers=)}";
    private static String m2 = "SinkRecord{kafkaOffset=233565, timestampType=CreateTime} ConnectRecord{topic='Lyon_2e_Arrondissement-Northbound', kafkaPartition=0, key=null, keySchema=null, value={sdTravelTime=0.0, aggPeriod=179000, linkid=12500009458457, domainAggTimestamp=1536188938000, areaName=Lyon_2e_Arrondissement, avgTravelTime=19.50983963012695, numVehicles=1, aggTimestamp=1647430623994}, valueSchema=null, timestamp=1647430626590, headers=ConnectHeaders(headers=)}ciaolinkid=12500009458j";

    public static void test1() {
        System.out.println("BroadcastWorker.run");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String area = matcher.group(2);
            System.out.println("area: " + area);
        }
    }

    public static void test2() {
        Matcher matcher = pattern2.matcher(m2);
        matcher.find();
        String area = matcher.group(2);
        matcher.find();
        String area2 = matcher.group(2);
        System.out.println("area = " + area);
    }

    public static void main(String[] args) {
        test2();

//        area = area.substring(REGEX_OFFSET, area.length()-1);
//        System.out.println("area2: " + area );
    }
}
