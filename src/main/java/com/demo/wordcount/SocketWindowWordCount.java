package com.demo.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Created by congyihao on 2017/5/29.
 */
public class SocketWindowWordCount {

    public static void main(String[] args) throws Exception {
        // 程序要连接的端口
        final int port;

        try {
            final ParameterTool params = ParameterTool.fromArgs(args);
            port = params.getInt("port");
        } catch (Exception e) {
            System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'");
            return;
        }

        // 获取执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 连接到socket, 获取数据
        DataStream<String> text = env.socketTextStream("localhost", port, "\n");

        DataStream<WordWithCount> windowCount = text
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    public void flatMap(String s, Collector<WordWithCount> collector) throws Exception {
                        for (String word : s.split("\\s")) {
                            collector.collect(new WordWithCount(word, 1L));
                        }
                    }
                })
                // WordWithCount中的word这个field作为key
                .keyBy("word")
                // 5秒的窗口, 1秒的滑动步长
                .timeWindow(Time.seconds(5), Time.seconds(1))
                .reduce(new ReduceFunction<WordWithCount>() {
                    public WordWithCount reduce(WordWithCount wordWithCount, WordWithCount t1) throws Exception {
                        return new WordWithCount(wordWithCount.word, wordWithCount.count + t1.count);
                    }
                });

        // 使用单线程打印结果
        windowCount.print().setParallelism(1);
        env.execute("Socket Window WordCount");
    }

    /**
     * 数据流中的中间结果单元
     */
    public static class WordWithCount {
        public String word;
        public long count;

        /**
         * 注意: 一定要有这个空的构造方法, 否则会报异常
         */
        public WordWithCount() {}

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}