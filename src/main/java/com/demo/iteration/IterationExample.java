/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.iteration;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Example illustrating iterations in Flink streaming.
 * <p> The program sums up random numbers and counts additions
 * it performs to reach a specific threshold in an iterative streaming fashion. </p>
 *
 * 这个程序将随机数相加, 并计算加法操作执行的次数.
 *
 * <p>
 * This example shows how to use:
 * <ul>
 *   <li>streaming iterations,
 *   <li>buffer timeout to enhance latency,
 *   <li>directed outputs.
 * </ul>
 * </p>
 *
 * 程序主要经过了以下步骤:
 *
 * 1. 从文件中生成数据源, 或者直接生成数据源.
 * 2. 将数据源map成迭代需要的格式, 生成IterativeStream
 * 3. 对IterativeStream进行一系列的transformation操作, 然后利用Selector, 根据结果生成SpitStream.
 * 4. 根据SplitStream的不同Channel, 决定IterativeStream的迭代流和输出流.
 * 5. 对输出流进行一系列的transformation,
 */
public class IterationExample {

    private static final int BOUND = 100;

    // *************************************************************************
    // PROGRAM
    // *************************************************************************

    public static void main(String[] args) throws Exception {

        // Checking input parameters
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up input for the stream of integer pairs

        // obtain execution environment and set setBufferTimeout to 1 to enable
        // continuous flushing of the output buffers (lowest latency)
        // 注意: 这里的延迟单位是ms.
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment()
                .setBufferTimeout(1);

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // create input stream of integer pairs
        // 这里是数据源格式的声明
        DataStream<Tuple2<Integer, Integer>> inputStream;
        if (params.has("input")) {
            // 从文件中读取数据, 进行tuple2的构建
            inputStream = env.readTextFile(params.get("input")).map(new FibonacciInputMap());
        } else {
            System.out.println("Executing Iterate example with default input data set.");
            System.out.println("Use --input to specify file input.");
            // 这里是本程序实际使用的数据源: 直接生成数据源
            inputStream = env.addSource(new RandomFibonacciSource());
        }

        // create an iterative data stream from the input with 5 second timeout
        IterativeStream<Tuple5<Integer, Integer, Integer, Integer, Integer>> it = inputStream.map(new InputMap())
                // 这个等待时间, 是指在5000ms内没有收到新数据, 就停止迭代. 为了测试的方便.
                .iterate(5000);

        // apply the step function to get the next Fibonacci number
        // increment the counter and split the output with the output selector
        SplitStream<Tuple5<Integer, Integer, Integer, Integer, Integer>> step = it.map(new Step())
                .split(new MySelector());

        // close the iteration by selecting the tuples that were directed to the
        // 'iterate' channel in the output selector
        // 这里是反馈回路, 也就是需要继续迭代的地方
        it.closeWith(step.select("iterate"));

        // to produce the final output select the tuples directed to the
        // 'output' channel then get the input pairs that have the greatest iteration counter
        // on a 1 second sliding window
        // 这里是最终的输出
        DataStream<Tuple2<Tuple2<Integer, Integer>, Integer>> numbers = step.select("output")
                .map(new OutputMap());

        // emit results
        if (params.has("output")) {
            numbers.writeAsText(params.get("output"));
        } else {
            System.out.println("Printing result to stdout. Use --output to specify output path.");
            numbers.print();
        }

        // execute the program
        env.execute("Streaming Iteration Example");
    }

    // *************************************************************************
    // USER FUNCTIONS
    // *************************************************************************

    /**
     * Generate BOUND number of random integer pairs from the range from 0 to BOUND/2.
     *
     * 数据源: 生成取值范围受限的tuple2.
     *
     * 注意: run()方法理论上必须能够无限执行, 而且必须能够响应cancel()方法的中断.
     */
    private static class RandomFibonacciSource implements SourceFunction<Tuple2<Integer, Integer>> {
        private static final long serialVersionUID = 1L;

        private Random rnd = new Random();

        private volatile boolean isRunning = true;
        private int counter = 0;

        @Override
        public void run(SourceContext<Tuple2<Integer, Integer>> ctx) throws Exception {

            while (isRunning && counter < BOUND) {
                int first = rnd.nextInt(BOUND / 2 - 1) + 1;
                int second = rnd.nextInt(BOUND / 2 - 1) + 1;

                ctx.collect(new Tuple2<>(first, second));
                counter++;
                Thread.sleep(50L);
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    /**
     * Generate random integer pairs from the range from 0 to BOUND/2.
     *
     * 这个方法是用来从文件中读取数据作为数据源的, 因为文件中的数据是(value1, value2), 是一个String, 所以这里进行了以下操作:
     * 1. substring, 取1~n-1的长度, 也就是去掉了().
     * 2. 分离两个value.
     * 3. 生成tuple2
     */
    private static class FibonacciInputMap implements MapFunction<String, Tuple2<Integer, Integer>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Tuple2<Integer, Integer> map(String value) throws Exception {
            String record = value.substring(1, value.length() - 1);
            String[] splitted = record.split(",");
            return new Tuple2<>(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
        }
    }

    /**
     * Map the inputs so that the next Fibonacci numbers can be calculated while preserving the original input tuple.
     * A counter is attached to the tuple and incremented in every iteration step.
     */
    public static class InputMap implements MapFunction<Tuple2<Integer, Integer>, Tuple5<Integer, Integer, Integer,
            Integer, Integer>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Tuple5<Integer, Integer, Integer, Integer, Integer> map(Tuple2<Integer, Integer> value) throws
                Exception {
            // tuple5的前两位仅仅是为了保留原值, 返回的时候方便使用.
            return new Tuple5<>(value.f0, value.f1, value.f0, value.f1, 0);
        }
    }

    /**
     * Iteration step function that calculates the next Fibonacci number.
     */
    public static class Step implements
            MapFunction<Tuple5<Integer, Integer, Integer, Integer, Integer>, Tuple5<Integer, Integer, Integer,
                    Integer, Integer>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Tuple5<Integer, Integer, Integer, Integer, Integer> map(Tuple5<Integer, Integer, Integer, Integer,
                Integer> value) throws Exception {
            return new Tuple5<>(value.f0, value.f1, value.f3, value.f2 + value.f3, ++value.f4);
        }
    }

    /**
     * OutputSelector testing which tuple needs to be iterated again.
     */
    public static class MySelector implements OutputSelector<Tuple5<Integer, Integer, Integer, Integer, Integer>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Iterable<String> select(Tuple5<Integer, Integer, Integer, Integer, Integer> value) {
            // 这个output是个局部变量.
            List<String> output = new ArrayList<>();
            if (value.f2 < BOUND && value.f3 < BOUND) {
                output.add("iterate");
            } else {
                output.add("output");
            }
            // 调用select方法会返回分类结果
            return output;
        }
    }

    /**
     * Giving back the input pair and the counter.
     *
     * 返回原始的tuple2, 和加法执行次数的结果.
     */
    public static class OutputMap implements MapFunction<Tuple5<Integer, Integer, Integer, Integer, Integer>,
            Tuple2<Tuple2<Integer, Integer>, Integer>> {
        private static final long serialVersionUID = 1L;

        @Override
        public Tuple2<Tuple2<Integer, Integer>, Integer> map(Tuple5<Integer, Integer, Integer, Integer, Integer>
                                                                     value) throws
                Exception {
            return new Tuple2<>(new Tuple2<>(value.f0, value.f1), value.f4);
        }
    }

}

