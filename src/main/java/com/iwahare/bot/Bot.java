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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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


    @Override
    public void onUpdateReceived(Update update) {
        logger.info("===============================================================================================");

        MessageTransportDto messageTransportDto = new MessageTransportDto();
        if (update.hasCallbackQuery()) {
            messageTransportDto = operateCallbackQuery(update);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().hasSuccessfulPayment()) {
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else {
                messageTransportDto = operateMessage(update);
            }
        }


//        if (update.hasPreCheckoutQuery()) {
//            messageTransportDto = operatePreChecoutQuery(update);
//        }


        try {
            buildAnswer(messageTransportDto, update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    private MessageTransportDto operateCallbackQuery(Update update) {
        return requestService.operateCallbackQuery(update);
    }

//    private MessageTransportDto operatePreChecoutQuery(Update update) {
//        MessageTransportDto messageTransportDto = new MessageTransportDto();
//        AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
//        answerPreCheckoutQuery.setOk(true);
//        answerPreCheckoutQuery.setPreCheckoutQueryId(update.getPreCheckoutQuery().getId());
//        messageTransportDto.setAnswerPreCheckoutQuery(answerPreCheckoutQuery);
//
//        return messageTransportDto;
//    }

    private MessageTransportDto operateMessage(Update update) {
        return requestService.operateMessage(update);
    }

//    private MessageTransportDto operatePhoto(Update update) {
//        return translatorService.operatePhoto(update);
//    }

    public synchronized void buildAnswer(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {

        if (messageTransportDto == null) {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Сообщение больше не доступно");
            executeEditOrDeleteMessageText(editMessageText, update);

        } else {
            if (update.hasMessage()) {
                executePhoto(messageTransportDto,update);
                executeMessage(messageTransportDto, update);
            }

        }
    }

//    private final void executeSendInvoice(MessageTransportDto messageTransportDto, Update update) throws TelegramApiException {
//        SendInvoice sendInvoice = messageTransportDto.getSendInvoice();
//        sendInvoice.setChatId(Math.toIntExact(update.getCallbackQuery().getMessage().getChatId()));
//        execute(sendInvoice);
//    }

    private final void executeMessage(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {
        if (!messageTransportDto.getDesripion().isEmpty()
                && messageTransportDto.getInlineKeyboard() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(messageTransportDto.getText());
            sendMessage.setReplyMarkup(messageTransportDto.getInlineKeyboard());
            sendMessage.setChatId(getChatId(update));
            execute(sendMessage);
        }

    }

    private final void executePhoto(MessageTransportDto messageTransportDto, Update update)
            throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(messageTransportDto.getPhotoId());
        sendPhoto.setChatId(getChatId(update));

        execute(sendPhoto);
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
