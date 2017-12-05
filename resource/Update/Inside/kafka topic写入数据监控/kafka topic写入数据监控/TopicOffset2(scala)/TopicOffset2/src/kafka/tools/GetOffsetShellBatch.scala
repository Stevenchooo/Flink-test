package kafka.tools



import kafka.consumer._
import joptsimple._
import kafka.api.{OffsetRequest, PartitionOffsetRequestInfo}
import kafka.common.TopicAndPartition
import kafka.client.ClientUtils
import kafka.utils.{CommandLineUtils, ToolsUtils}
import kafka.cluster.BrokerEndPoint
import java.nio.ByteBuffer

import kafka.api.ApiUtils._
import kafka.utils.Logging
import kafka.common._

import scala.collection.Seq
import scala.io.Source

object GetOffsetShellBatch {

  def main(args: Array[String]): Unit = {
    val parser = new OptionParser
    val brokerListOpt = parser.accepts("broker-list", "REQUIRED: The list of hostname and port of the server to connect to.")
      .withRequiredArg
      .describedAs("hostname:port,...,hostname:port")
      .ofType(classOf[String])
    val topicOpt = parser.accepts("topic-path", "REQUIRED: The topic file to get offset from.")
      .withRequiredArg
      .describedAs("topic-path")
      .ofType(classOf[String])

    val timeOpt = parser.accepts("time", "timestamp of the offsets before that")
      .withRequiredArg
      .describedAs("timestamp/-1(latest)/-2(earliest)")
      .ofType(classOf[java.lang.Long])

    val nBatchCountOpt = parser.accepts("batch-count", "max topics in  each query ")
      .withRequiredArg
      .describedAs("default 50")
      .ofType(classOf[java.lang.Integer])
      .defaultsTo(50)




    if(args.length == 0)
      CommandLineUtils.printUsageAndDie(parser, "An interactive shell for getting consumer offsets.")

    val options = parser.parse(args : _*)

    CommandLineUtils.checkRequiredArgs(parser, options, brokerListOpt, topicOpt, timeOpt,nBatchCountOpt)

    val batchCount = options.valueOf(nBatchCountOpt).intValue()
    val brokerList = options.valueOf(brokerListOpt)
    ToolsUtils.validatePortOrDie(parser, brokerList)
    val metadataTargetBrokers = ClientUtils.parseBrokerList(brokerList)
    val topicPath = options.valueOf(topicOpt)

    var time = options.valueOf(timeOpt).intValue()


    val file = Source.fromFile(topicPath)
    //file.foreach(print)
    var i=0;

    var topics =  Set("");
    for(line <- file.getLines())
    {
      if(line.length> 0) {
        topics += line
        i += 1;
        if (i > batchCount) {
          printOffset(topics, metadataTargetBrokers,time)
          //清空topics
          topics =Set("")
          i = 0;
        }
      }
    }
    printOffset(topics,metadataTargetBrokers,time)
    file.close()


  }
  def printOffset( s:Set[String],metadataTargetBrokers:Seq[BrokerEndPoint], time:Long  ): Unit =
  {
    val clientId = "GetOffsetShellBatch"
    val topicsMetadata = ClientUtils.fetchTopicMetadata(s, metadataTargetBrokers, clientId, 1000).topicsMetadata
    /*
    if(topicsMetadata.size != 1 || !topicsMetadata(0).topic.equals(topic)) {
      System.err.println(("Error: no valid topic metadata for topic: %s, " + " probably the topic does not exist, run ").format(topic) +
        "kafka-list-topic.sh to verify")
      System.exit(1)
    }
    */
    topicsMetadata.head.partitionsMetadata.map(_.partitionId)


    //topicsMetadata
    //println("-------" + topicsMetadata.toString())
    //循环 metadata
    topicsMetadata.foreach(m1 =>
    {
      val t1 =m1.topic
      //m1.t
      //println("topicMetadata toString = ".format(m1.toString()) );

      m1.errorCode match {
        case ErrorMapping.NoError =>
          m1.partitionsMetadata.foreach { partitionMetadata1 =>

            partitionMetadata1.leader match {
              case Some(leader) =>
                val consumer = new SimpleConsumer(leader.host, leader.port, 10000, 100000, clientId)
                val topicAndPartition = TopicAndPartition(t1, partitionMetadata1.partitionId)
                val request = OffsetRequest(Map(topicAndPartition -> PartitionOffsetRequestInfo(time, 1)))
                val offsets = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets


                //println(offsets)
                //println("sizeinBytes %d".format(partitionMetadata1.));
                //topicAndPartition.
                println("%s:%d:%s".format(t1, partitionMetadata1.partitionId, offsets.mkString(",")))
              case None => System.err.println("")
            }
            //case None => System.err.println("Error: partition %d does not exist".format(partitionId))
          }
        case _ =>
          val j=1

      }

    })
  }



}
