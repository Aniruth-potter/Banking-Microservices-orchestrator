//package com.bank.orchestrator_service.config;
//
//import com.bank.orchestrator_service.dto.TransactionDTO;
//
//import java.util.concurrent.CompletableFuture;
//
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.SettableListenableFuture;
//
//@Configuration
//@Profile("dev") // Only active in 'dev' profile
//public class KafkaMockConfig {
//
//	@Bean
//	public KafkaTemplate<String, TransactionDTO> kafkaTemplate() {
//	    return new KafkaTemplate<>(null) {
//	        @Override
//	        public ListenableFuture<SendResult<String, TransactionDTO>> send(String topic, TransactionDTO data) {
//	            System.out.println("✅ Mock Kafka send: " + data);
//
//	            // Create a dummy SendResult
//	            ProducerRecord<String, TransactionDTO> record = new ProducerRecord<>(topic, data);
//	            RecordMetadata metadata = new RecordMetadata(null, 0, 0, 0L, 0L, 0, 0);
//	            SendResult<String, TransactionDTO> result = new SendResult<>(record, metadata);
//
//	            // Wrap it in a SettableListenableFuture
//	            SettableListenableFuture<SendResult<String, TransactionDTO>> future = new SettableListenableFuture<>();
//	            future.set(result);
//	            return future;
//	        }
//	    };
//	}
//
//}
