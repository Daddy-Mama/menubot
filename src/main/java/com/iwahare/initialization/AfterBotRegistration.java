package com.iwahare.initialization;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicated that the Method of a Class extending {@link LongPollingBot} will be called after the com.iwahare.bot was registered
 * If the Method has a single Parameter of type {@link BotSession}, the method get passed the com.iwahare.bot session the com.iwahare.bot was registered with
 * <br><br>
 * <p>The com.iwahare.bot session passed is the ones returned by {@link TelegramBotsApi#registerBot(LongPollingBot)}</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterBotRegistration {}
