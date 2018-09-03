/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.kafka.clients.producer;

import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The interface for the {@link KafkaProducer}
 * @see KafkaProducer
 * @see MockProducer
 */
public interface Producer<K, V> extends Closeable {

    /**
     * See {@link KafkaProducer#initTransactions()}
     */
    void initTransactions();

    /**
     * See {@link KafkaProducer#beginTransaction()}
     */
    void beginTransaction() throws ProducerFencedException;

    /**
     * See {@link KafkaProducer#sendOffsetsToTransaction(Map, String)}
     */
    void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets,
                                  String consumerGroupId) throws ProducerFencedException;

    /**
     * See {@link KafkaProducer#commitTransaction()}
     */
    void commitTransaction() throws ProducerFencedException;

    /**
     * See {@link KafkaProducer#abortTransaction()}
     */
    void abortTransaction() throws ProducerFencedException;

    /**
     * See {@link KafkaProducer#send(ProducerRecord)}
     */
    Future<RecordMetadata> send(ProducerRecord<K, V> record);

    /**
     *
     * 发送消息，实际上是将消息放入RecordAccumulator暂存
     */
    /**
     * See {@link KafkaProducer#send(ProducerRecord, Callback)}
     */
    Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback);

    /**
     * 刷新操作；等待RecordAccumulator中所有消息发送完成，在刷新完成之前阻塞调用的线程
     *
     * See {@link KafkaProducer#flush()}
     */
    void flush();

    /**
     * Metadata的元数据会定时更新；负责从Metadata中获取指定Topic中的分区信息
     *
     *
     * See {@link KafkaProducer#partitionsFor(String)}
     */
    List<PartitionInfo> partitionsFor(String topic);

    /**
     * See {@link KafkaProducer#metrics()}
     */
    Map<MetricName, ? extends Metric> metrics();

    /**
     * 关闭Producer对象，主要操作是设置close标志，等待RecordAccumulator中的消息清空，关闭Sender线程
     *
     * See {@link KafkaProducer#close()}
     */
    void close();

    /**
     * See {@link KafkaProducer#close(long, TimeUnit)}
     */
    void close(long timeout, TimeUnit unit);

}
