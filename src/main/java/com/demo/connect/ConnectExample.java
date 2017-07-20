package com.demo.connect;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * Created by didi on 2017/6/11.
 */
public class ConnectExample {

    public static void main(String[] args) throws Exception {
        final ParameterTool params = ParameterTool.fromArgs(args);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple3<String, String, String>> ratingSource = env.fromElements(
                new Tuple3<>("user1", "item1", "1"),
                new Tuple3<>("user2", "item2", "2"),
                new Tuple3<>("user3", "item3", "3"),
                new Tuple3<>("user4", "item4", "4"),
                new Tuple3<>("user5", "item5", "5"),
                new Tuple3<>("user6", "item6", "6"),
                new Tuple3<>("user7", "item7", "7"),
                new Tuple3<>("user8", "item8", "8")
        );

        KeyedStream<Tuple3<String, String, String>, Tuple> userStream = ratingSource.keyBy(0);
//        KeyedStream<Tuple3<String, String, String>, Tuple> itemStream = ratingSource.keyBy(1);
        DataStream<Tuple3<String, String, String>> itemStream = ratingSource.shuffle();

        DataStream<String> connectedStream = userStream.connect(itemStream).map(new CoMapper());

        connectedStream.print();

        env.execute("Connect example");

    }

    private static class CoMapper implements CoMapFunction<
            Tuple3<String, String, String>,
            Tuple3<String, String, String>,
            String> {
        @Override
        public String map1(Tuple3<String, String, String> value) throws Exception {
            return value.f0;
        }

        @Override
        public String map2(Tuple3<String, String, String> value) throws Exception {
            return value.f1;
        }
    }
}
