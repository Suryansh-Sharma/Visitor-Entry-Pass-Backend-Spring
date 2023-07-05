package com.suryansh.visitorentry.service.interfaces;

import com.suryansh.visitorentry.dto.TelegramMessageDto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramService {
    void sendNewVisitMessage(TelegramMessageDto telegramMessageDto) throws TelegramApiException;
}
