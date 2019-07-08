package com.iwahare;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwahare.dto.Menu;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class MbotApplication {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(MbotApplication.class, args);
        logger.info("=======================APPLICATION STARTED =====================");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static Menu buildMenuSingleton() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
//        File json = new File("/src/main/resources/menu.json");
        String json = "{\n" +
                "  \"description\": \"\\ud83d\\udc47Меню\\ud83d\\udc47\",\n" +
                "  \"categories\": [\n" +
                "    {\n" +
                "      \"name\": \"Лате\",\n" +
                "      \"preNameEmoji\": \"\\u2615\",\n" +
                "      \"postNameEmoji\": \"\",\n" +
                "      \"description\": \"\\ud83d\\udd25\\ud83c\\udfaf Выберите размер \\ud83c\\udfaf\\ud83d\\udd25\",\n" +
                "      \"products\": [\n" +
                "        {\n" +
                "          \"name\": \"Мини\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"35\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Средний\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"50\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Большой\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"100\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"categories\": []\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Капучино\",\n" +
                "      \"preNameEmoji\": \"\\u2615\",\n" +
                "      \"postNameEmoji\": \"\",\n" +
                "      \"description\": \"\\uD83D\\uDD25\\uD83C\\uDFAF Выберите размер\\uD83C\\uDFAF\\uD83D\\uDD25\",\n" +
                "      \"products\": [\n" +
                "        {\n" +
                "          \"name\": \"Мини\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"35\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Средний\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"50\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Большой\",\n" +
                "          \"preNameEmoji\": \"\",\n" +
                "          \"postNameEmoji\": \"\\uD83D\\uDCB0\",\n" +
                "          \"price\": \"100\",\n" +
                "          \"extras\": [\n" +
                "            {\n" +
                "              \"name\": \" Вишневый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf52\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Банановый\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf4c\",\n" +
                "              \"price\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"name\": \" Клубничный\",\n" +
                "              \"preNameEmoji\": \"\\ud83c\\udf53\",\n" +
                "              \"price\": \"50\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"extraDescription\": \"\\ud83e\\uddc2\\uD83C\\uDF74 Добавить сироп:\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"categories\": []\n" +
                "    }\n" +
                "  ],\n" +
                "  \"products\": []\n" +
                "}";
        Menu menu = mapper.readValue(json, Menu.class);
        return menu;
    }
}
