package com.iwahare.bot;


import com.iwahare.impl.IRequestService;
import com.iwahare.message.MessageTransportDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.iwahare.enums.CommandsEnum.COMMAND_NOT_RECOGNIZED_ERROR;

@Component
@PropertySource("classpath:application.properties")
public class Bot extends TelegramLongPollingBot {
    private final static Logger logger = LogManager.getLogger();

    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.username}")
    private String name;
    @Autowired
    private IRequestService requestService;

    @Autowired
    public Bot() {
    }


    /**
     * Метод для приема сообщений.
     *
     * @param update Содержит сообщение от пользователя.
     */
    private final MessageTransportDto operateSuccessfullpayment(Update update) {
        return requestService.operatePayment(update);
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("===============================================================================================");

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        if (update.hasCallbackQuery()) {
            messageTransportDto = operateCallbackQuery(update);
        }

        if (update.hasMessage()) {
            if (update.getMessage().hasSuccessfulPayment()) {
                messageTransportDto = operateSuccessfullpayment(update);
            }
            if (update.getMessage().hasText()) {
                messageTransportDto = operateMessage(update);
            }

        }

        if (update.hasPreCheckoutQuery()) {
            messageTransportDto = operatePreChecoutQuery(update);
        }

        try {
            buildAnswer(messageTransportDto, update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    private MessageTransportDto operateCallbackQuery(Update update) {
        return requestService.operateCallbackQuery(update);
    }

    private MessageTransportDto operatePreChecoutQuery(Update update) {
        MessageTransportDto messageTransportDto = new MessageTransportDto();
        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
        answerPreCheckoutQuery.setOk(true);
        answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
        messageTransportDto.setAnswerPreCheckoutQuery(answerPreCheckoutQuery);

        return messageTransportDto;
    }

    private MessageTransportDto operateMessage(Update update) {
        return requestService.operateMessage(update);
    }


    public synchronized void buildAnswer(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {

        if (messageTransportDto == null) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Сообщение больше не доступно");
            executeEditOrDeleteMessageText(editMessageText, update);

        } else {
            if (update.hasMessage()) {
                if (update.getMessage().hasSuccessfulPayment()) {
                    executeSuccessfullPayment(messageTransportDto, update);
                    return;
                }
                executePhoto(messageTransportDto, update);
                executeMessage(messageTransportDto, update);
            }
            if (update.hasCallbackQuery()) {
                executePhoto(messageTransportDto, update);
                executeCallbackQuery(messageTransportDto, update);
                executeSendInvoice(messageTransportDto, update);
            }
            if (update.hasPreCheckoutQuery()) {
                executePrecheckout(messageTransportDto);
            }
        }
    }

    private final void executeSuccessfullPayment(MessageTransportDto messageTransportDto, Update update) throws TelegramApiException {
        if (messageTransportDto.getDesripion() != null) {
            for (Long chatId:messageTransportDto.getChat_id()){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(messageTransportDto.getText());
                execute(sendMessage);
            }
        }
    }

    private final void executePrecheckout(MessageTransportDto messageTransportDto) throws TelegramApiException {
        if (messageTransportDto.getAnswerPreCheckoutQuery() != null) {
            execute(messageTransportDto.getAnswerPreCheckoutQuery());
        }
    }

    private final void executeCallbackQuery(MessageTransportDto messageTransportDto, Update update) throws TelegramApiException {
        if (messageTransportDto.getDesripion() != null && messageTransportDto.getSendInvoice() == null) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(messageTransportDto.getText());
            if (messageTransportDto.getInlineKeyboardMarkup() != null) {
                editMessageText.setReplyMarkup(messageTransportDto.getInlineKeyboardMarkup());
            }
            editMessageText.setChatId(getChatId(update));
            editMessageText.setMessageId(getMessageId(update));
            execute(editMessageText);
        }
    }

    private final void executeSendInvoice(MessageTransportDto messageTransportDto, Update update) throws TelegramApiException {
        if (messageTransportDto.getSendInvoice() != null) {
            SendInvoice sendInvoice = messageTransportDto.getSendInvoice();
            sendInvoice.setChatId(Math.toIntExact(getChatId(update)));
            execute(sendInvoice);
        }
    }

    private final void executeMessage(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {
        if (messageTransportDto.getDesripion() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(messageTransportDto.getText());
            if (messageTransportDto.getInlineKeyboardMarkup() != null) {
                sendMessage.setReplyMarkup(messageTransportDto.getInlineKeyboardMarkup());
            }
            if (messageTransportDto.getKeyboardMarkup() != null) {
                sendMessage.setReplyMarkup(messageTransportDto.getKeyboardMarkup());
            }
            sendMessage.setChatId(getChatId(update));
            execute(sendMessage);
        }

    }

    private final void executePhoto(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {
        if (messageTransportDto.getPhotoId() != null) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(messageTransportDto.getPhotoId());
            sendPhoto.setChatId(getChatId(update));

            execute(sendPhoto);
        }
    }


    private final void executeEditOrDeleteMessageText(EditMessageText editMessageText, Update update)
            throws TelegramApiException {

        int message_id = getMessageId(update);
        long chat_id = getChatId(update);

        if (editMessageText != null) {
            editMessageText.setChatId(chat_id);
            editMessageText.setMessageId(message_id);
            execute(editMessageText);
        }

    }

    private final int getMessageId(Update update) {
        Integer message_id;
        if (update.hasCallbackQuery()) {
            message_id = update.getCallbackQuery().getMessage().getMessageId();
        } else {
            message_id = update.getMessage().getMessageId();
        }
        return message_id;
    }

    private final long getChatId(Update update) {
        Long chat_id;
        if (update.hasCallbackQuery()) {
            chat_id = update.getCallbackQuery().getMessage().getChatId();
        } else {
            chat_id = update.getMessage().getChatId();
        }
        return chat_id;
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return name;
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return token;
    }
}
