package com.backend

import com.kms.katalon.core.annotation.Keyword
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.Properties
import java.util.Collections

public class KafkaHelper {

    @Keyword
    def consumeKafkaMessage(String topicName) {
        // Setup Properties for Kafka Connection
        // NOTE: Pointing to localhost for demonstration. In real env, use actual bootstrap servers.
        Properties props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "katalon-test-group-id")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

        KafkaConsumer<String, String> consumer = null
        
        try {
            // Initialize Consumer
            consumer = new KafkaConsumer<>(props)
            
            // Subscribe to Topic
            consumer.subscribe(Collections.singletonList(topicName))
            println("Subscribed to topic: " + topicName)
            
            // Poll for records (Timeout 5 seconds)
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5))
            
            if (records.isEmpty()) {
                println("No records found within timeout period.")
            } else {
                records.forEach { record ->
                    println(String.format("Record received - Key: %s, Value: %s, Partition: %d, Offset: %d", 
                        record.key(), record.value(), record.partition(), record.offset()))
                }
            }
        } catch (Exception e) {
            println("Error during Kafka consumption: " + e.getMessage())
            // Normally we would fail the test here, but for demo script we just log
        } finally {
            if (consumer != null) {
                consumer.close()
                println("Kafka Consumer closed.")
            }
        }
    }
}
