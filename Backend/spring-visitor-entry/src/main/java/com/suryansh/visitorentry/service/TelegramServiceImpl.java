package com.suryansh.visitorentry.service;

import com.suryansh.visitorentry.bot.MyTelegramBot;
import com.suryansh.visitorentry.dto.TelegramMessageDto;
import com.suryansh.visitorentry.service.interfaces.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * This class is used to perform a telegram operation like send a message.
 *
 * @author suryansh
 */
@Service
public class TelegramServiceImpl implements TelegramService {
    @Value("${folder.images}")
    private String FOLDER_PATH;

    @Value("${telegram.chatIdSuryansh}")
    private String chatId;

    private final MyTelegramBot telegramBot;
    private static final Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);
    public TelegramServiceImpl(MyTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    @Async
    public void sendNewVisitMessage(TelegramMessageDto dto) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);

        // Load the image from the file path
        String imagePath = FOLDER_PATH+"/"+dto.visitorImage();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            InputFile inputFile = new InputFile(imageFile);
            sendPhoto.setPhoto(inputFile);

            // Compose the message with visitor details
            String messageBuilder = "New visit added:\n" +
                    "Visitor Name: " + dto.visitorName() + "\n" +
                    "Contact Number: " + dto.visitorContact() + "\n" +
                    "Reason: " + dto.reason() + "\n" +
                    "Address: \n" +
                    "City: " + dto.city() +"\n" +
                    "line1: " + dto.line1() + "\n";

            // Set the caption for the photo
            sendPhoto.setCaption(messageBuilder);
            telegramBot.execute(sendPhoto);
            logger.info("Message sent to user {} ",dto.visitorContact());
        }else {
            logger.error("Unable to find image {} for sending to Telegram of user {} ",dto.visitorImage(),dto.visitorName());
        }


    }
}
