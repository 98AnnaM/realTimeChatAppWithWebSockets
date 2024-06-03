package com.devexperts.chatapp.service;

import com.devexperts.chatapp.model.dto.SymbolDto;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Value("${aaplsymbolservice.base.url}")
    private String addressBaseUrl;

    private static final Logger logger = Logger.getLogger(SubscriptionService.class.getName());
    private final Map<String, Disposable> subscriptions = new ConcurrentHashMap<>();
    private final WebSocketService webSocketService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Disposable disposable;

    private final Map<String, Set<String>> symbolsByUser = new HashMap<>();

    public SubscriptionService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;

        Observable<Map<String, List<SymbolDto>>> observable = Observable.interval(0, 3, TimeUnit.SECONDS)
                .map(tick -> getFetchedDataByUser())
                .retry();

        this.disposable = observable.subscribe(
                data -> {
                    for (Map.Entry<String, List<SymbolDto>> stringListEntry : data.entrySet()) {
                        logger.info("Fetched data: " + data);
                        String user = stringListEntry.getKey();
                        List<SymbolDto> symbols = stringListEntry.getValue();
                        symbols.forEach(symbol -> webSocketService.sendMessage(user, symbol));
                    }
                },
                throwable -> logger.severe("Error in observable: " + throwable.getMessage())
        );
    }

    private Map<String, List<SymbolDto>> getFetchedDataByUser() {
        Map<String, List<SymbolDto>> symbolsByUsr = new HashMap<>();
        for (Map.Entry<String, Set<String>> stringSetEntry : symbolsByUser.entrySet()) {
            symbolsByUsr.put(stringSetEntry.getKey(), stringSetEntry.getValue().stream().map(this::fetchMockData).collect(Collectors.toList()));
        }
        return symbolsByUsr;
    }

    public void subscribe(String user, String symbol) {
        Set<String> symbols = symbolsByUser.getOrDefault(user, new HashSet<>());
        symbols.add(symbol);
        symbolsByUser.put(user, symbols);

    }

    public void unsubscribe(String user, String symbol) {
        String key = user + symbol;
        if (subscriptions.containsKey(key)) {
            subscriptions.get(key).dispose();
            subscriptions.remove(key);
        }
        symbolsByUser.get(user).remove(symbol);
    }

    private SymbolDto fetchMockData(String symbol) {
        logger.info("Fetching: " + addressBaseUrl + "/" + symbol);
        ResponseEntity<SymbolDto> response = restTemplate.getForEntity(addressBaseUrl + "/" + symbol, SymbolDto.class);
        SymbolDto body = response.getBody();
        logger.info("Received response: " + body);
        return body;
    }

    public boolean isSubscribed(String user, String symbol) {
        String key = user + symbol;
        return subscriptions.containsKey(key);
    }
}