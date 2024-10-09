package io.github.mauricio_mds.products.service;

import io.github.mauricio_mds.core.ProductCreatedEvent;
import io.github.mauricio_mds.products.rest.CreateProductRestModel;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {
        String productId = UUID.randomUUID().toString();
        // TODO: Persist Product Details into database table before publishing and Event
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );

        ProducerRecord<String, ProductCreatedEvent> record =
                new ProducerRecord<>("product-created-events-topic", productId, productCreatedEvent);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(record).get();
        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("Offset: " + result.getRecordMetadata().offset());

        return productId;
    }
}
