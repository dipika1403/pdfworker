package com.example.pdfworker.controller;

import com.example.pdfworker.service.PdfWorkerSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfWorkerSenderController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PdfWorkerSenderController.class);

    private final PdfWorkerSenderService senderService;
    @Value("${amqp.exchange.name}")
    String exchangeName;
    @Value("${amqp.verify.routing.key}")
    String verifyRoutingKey;
    @Value("${amqp.checkout.routing.key}")
    String checkoutRoutingKey;

    @Autowired
    public PdfWorkerSenderController(PdfWorkerSenderService senderService) {
        this.senderService = senderService;
    }

    public void sendPdfWorkSheet(){
        String msg = "blueprintId: '5d5f044f-ea3b-4533-9f55-2e1a45b02aab',\n" +
                "    currentProcessingPhase: 'pdf_to_image',\n" +
                "    fileLocation: 'https://s3.us-east-2.amazonaws.com/someco.com/uploads/pdfs/74efe087-7949-46db-8a8d-ee06776eb2b0.pdf',\n" +
                "    createTime: '1544404634'\n";

        try {
            senderService.sendMessage(exchangeName, checkoutRoutingKey, msg);
        } catch(RuntimeException ex){
            LOGGER.error("Error while sending message to RabbitMq." +ex.toString());
        }
    }
}
