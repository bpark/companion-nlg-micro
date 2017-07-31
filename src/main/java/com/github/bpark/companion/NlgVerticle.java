/*
 * Copyright 2017 Kurt Sparber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bpark.companion;

import com.github.bpark.companion.model.ConversationResponse;
import com.github.bpark.companion.model.PhraseResponse;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Single;

import java.util.List;
import java.util.stream.Collectors;

public class NlgVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(NlgVerticle.class);

    private static final String ADDRESS = "nlg.generate";

    private static final String DIALOGMANAGER_KEY = "dialogmanager";

    private static final String NLG_KEY = "nlg";

    private static final String DELIMITER = " ";

    @Override
    public void start() throws Exception {
        super.start();

        PhraseGenerator phraseGenerator = new PhraseGenerator();

        EventBus eventBus = vertx.eventBus();

        MessageConsumer<String> consumer = eventBus.consumer(ADDRESS);
        Observable<Message<String>> observable = consumer.toObservable();
        observable.subscribe(message -> {
            String id = message.body();
            logger.info("received message id: {}", id);

            readMessage(id).flatMap(conversationResponse -> {

                List<PhraseResponse> phraseResponses = conversationResponse.getResponses();

                List<String> answers = phraseResponses.stream().map(phraseGenerator::generate).collect(Collectors.toList());
                String answer = String.join(DELIMITER, answers);

                return Single.just(answer).toObservable();

            }).flatMap(answer -> saveMessage(id, answer)).subscribe(a -> message.reply(id));

        });

    }

    private Observable<ConversationResponse> readMessage(String id) {
        return vertx.sharedData().<String, String>rxGetClusterWideMap(id)
                .flatMap(map -> map.rxGet(DIALOGMANAGER_KEY))
                .flatMap(content -> Single.just(Json.decodeValue(content, ConversationResponse.class)))
                .toObservable();
    }

    private Observable<Void> saveMessage(String id, String answer) {
        return vertx.sharedData().<String, String>rxGetClusterWideMap(id)
                .flatMap(map -> map.rxPut(NLG_KEY, Json.encode(answer)))
                .toObservable();
    }

}
