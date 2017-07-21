package com.demo.ml;
/**
 * Created by Steven on 2017/7/20.
 */
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * Skeleton for incremental machine learning algorithm consisting of a
 * pre-computed model, which gets updated for the new inputs and new input data
 * for which the job provides predictions.
 *
 * 增量机器学习算法的骨架, 包括
 *
 * - 一个从老数据计算出来的batch-model(或者从数据流计算出来的不断增量更新的模型)
 * - 一个不断由时间窗口内的新数据计算出来的partial model, 用来更新batch-model
 * - 对查询请求进行预测.
 *
 * <p>This may serve as a base of a number of algorithms, e.g. updating an
 * incremental Alternating Least Squares model while also providing the
 * predictions.
 *
 * <p>This example shows how to use:
 * <ul>
 *   <li>Connected streams
 *   <li>CoFunctions
 *   <li>Tuple data types
 * </ul>
 *
 */
public class IncrementalLearningSkeleton {


	// *************************************************************************
	// PROGRAM
	// *************************************************************************

	public static void main(String[] args) throws Exception {

		//检查输入参数
		final ParameterTool params = ParameterTool.fromArgs(args);

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 设定流中元素的时间特性, 在依赖时间的操作(例如window操作)中会用到
        // EventTime是按照event发生的时间决定处理顺序, 这个时间是在外部的source中生成的, 与事件到达Flink的时间无关.
        // 也就是后面到来的事件可能由于延迟, 实际上带有较小的时间戳, Flink能够处理这种乱序到达的情况.
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		// 训练数据, 8500条, 无延迟发送
		DataStream<Integer> trainingData = env.addSource(new FiniteTrainingDataSource());
		// 新到来的数据, 50条, 用750ms发送完成
		DataStream<Integer> newData = env.addSource(new FiniteNewDataSource());

		// 在新数据的每一秒建立新的模型
        // 以0.5s为单位, 进行增量部分模型的构建.
		DataStream<Double[]> model = trainingData
                // 对于要进行window操作的数据, 要包含时间戳和Watermark. 由于我们是简单的从文件中读取的数据,
                // 并没有时间戳, 这里我们自己生成. 如果是从kafka等数据源生成的数据, 可以由数据源提供时间戳.
				.assignTimestampsAndWatermarks(new LinearTimestamp())
                // 注意: 如果没有按key进行partition, 那么timeWindowAll()操作实际上是串行的, 这会极大影响效率
				.timeWindowAll(Time.of(5000, TimeUnit.MILLISECONDS))
                // apply操作是作用于一个时间窗口的所有事件的,
				.apply(new PartialModelBuilder());

        // 注意: 这里使用了connect操作, connect操作会保留被connect的两个流的类型信息, 这里进行connect的目的是, 让newData
        // 能够使用model进行预测.
        // 以推荐为例, 这里的两个数据流可以这样理解:
        // 1. newData是用户的查询请求流, 可以传来用户的userID, 每到来一个userID, 就可以利用模型进行推荐, 返回推荐结果.
        // 2. model是partial model流, 也就是从原始的trainingData按照window建模后构建的增量模型流. 每一个window interval的间隔,
        // 到来一个新的partial model, 对原模型进行更新.
		DataStream<Integer> prediction = newData.connect(model).map(new Predictor());

		// emit result
		if (params.has("output")) {
			prediction.writeAsText(params.get("output"));
		} else {
			System.out.println("Printing result to stdout. Use --output to specify output path.");
			// 输出的前缀, 例如"1>", "2>"这样的, 是在不同的slot上并行执行的.
			// 本示例程序中, 如果输出的是0, 那么就是预测, 如果是1, 那么就是模型更新操作.
			prediction.print();
		}

		// execute program
		env.execute("Streaming Incremental Learning");
	}

	// *************************************************************************
	// USER FUNCTIONS
	// *************************************************************************

	/**
	 * Feeds new data for newData. By default it is implemented as constantly
	 * emitting the Integer 1 in a loop.
     *
     * 数据源: 一共发送50个数据, 每20ms发送一次, 发送时间一共是1s
	 */
	public static class FiniteNewDataSource implements SourceFunction<Integer> {
		private static final long serialVersionUID = 1L;
		private int counter;

		@Override
		public void run(SourceContext<Integer> ctx) throws Exception {
			Thread.sleep(15);
			while (counter < 50) {
				ctx.collect(getNewData());
			}
		}

		@Override
		public void cancel() {
			// No cleanup needed
		}

		private Integer getNewData() throws InterruptedException {
			Thread.sleep(5);
			counter++;
			return 1;
		}
	}

	/**
	 * Feeds new training data for the partial model builder. By default it is
	 * implemented as constantly emitting the Integer 1 in a loop.
     *
     * 数据源: 原始数据, 一共发送8200个数据, 立即发送.
	 */
	public static class FiniteTrainingDataSource implements SourceFunction<Integer> {
		private static final long serialVersionUID = 1L;
		private int counter = 0;

		@Override
		public void run(SourceContext<Integer> collector) throws Exception {
			while (counter < 8200) {
				collector.collect(getTrainingData());
			}
		}

		@Override
		public void cancel() {
			// No cleanup needed
		}

		private Integer getTrainingData() throws InterruptedException {
			// debug
			// added by congyihao
			Thread.sleep(1);
			counter++;
			return 1;
		}
	}

	private static class LinearTimestamp implements AssignerWithPunctuatedWatermarks<Integer> {
		private static final long serialVersionUID = 1L;

		private long counter = 0L;

		@Override
		public long extractTimestamp(Integer element, long previousElementTimestamp) {
			return counter += 10L;
		}

		@Override
		public Watermark checkAndGetNextWatermark(Integer lastElement, long extractedTimestamp) {
			return new Watermark(counter - 1);
		}
	}

	/**
	 * Builds up-to-date partial models on new training data.
     *
     * 注意: 这个partial model是用一个window中的数据创建出来的. 实际上是mini-batch的形式???
	 */
	public static class PartialModelBuilder implements AllWindowFunction<Integer, Double[], TimeWindow> {
		private static final long serialVersionUID = 1L;

		protected Double[] buildPartialModel(Iterable<Integer> values) {
			return new Double[]{1.};
		}

		@Override
		public void apply(TimeWindow window, Iterable<Integer> values, Collector<Double[]> out) throws Exception {
			out.collect(buildPartialModel(values));
		}
	}

	/**
	 * Creates newData using the model produced in batch-processing and the
	 * up-to-date partial model.
	 * <p>
	 * By defaults emits the Integer 0 for every newData and the Integer 1
	 * for every model update.
	 * </p>
     *
     * CoMapFunction泛型的:
     * - 第一个参数是第一个数据流里面的元素类型.
     * - 第二个参数是第二个数据流里面的元素类型.
     * - 第三个参数是返回结果类型.
     *
     * CoMapFunction主要是为了两个流共享数据而使用.
     *
     * 两个流中, 每当不同的流到来元素, 会触发不同的map进行transformation操作.
	 */
	public static class Predictor implements CoMapFunction<Integer, Double[], Integer> {
		private static final long serialVersionUID = 1L;

		// batchModel和partialModel的类型应该是相同的
		Double[] batchModel = null;
		Double[] partialModel = null;
		// for debug
		int count1 = 0;
		int count2 = 0;

		@Override
		public Integer map1(Integer value) {
            // TODO 用两个模型(或者说更新后的模型)来预测新数据的结果.
			count1++;
//			return predict(value);
			return count1;
		}

		@Override
		public Integer map2(Double[] value) {
			// TODO 在这里获取batch model, 以及partial model, 使用partial model进行batch model的更新.
			partialModel = value;
			batchModel = getBatchModel();
			// 实际上是返回一个标志位, 要能够与前面map1的预测结果区分开来, 表明是对第二个数据流的操作.
			// for debug
			count2++;
			return count2;
//			return 1;
		}

		// pulls model built with batch-job on the old training data
        // TODO 返回老模型(batch-model), 或者说更新后的模型
		protected Double[] getBatchModel() {
			return new Double[]{0.};
		}

		// performs newData using the two models
        // TODO 使用两个模型(或者说更新后的模型)对新到来的数据进行预测
		protected Integer predict(Integer inTuple) {
			return 0;
		}

	}

}
