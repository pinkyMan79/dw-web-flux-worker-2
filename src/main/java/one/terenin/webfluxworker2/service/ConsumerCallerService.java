package one.terenin.webfluxworker2.service;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.dto.DataBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// call the first consumer service
public class ConsumerCallerService {

    private final WebClient webClientJson;
    private final WebClient webClientParquet;

    public Mono<DataBundle> getOneDataBundleJson() {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/sized/json/1");
        return requestHeadersUriSpec.exchangeToMono(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToMono(DataBundle.class);
            } else {
                return Mono.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

    public Flux<DataBundle> getManyDataBundleJson() {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/stream/string");
        return requestHeadersUriSpec.exchangeToFlux(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToFlux(DataBundle.class);
            } else {
                return Flux.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

    public Flux<DataBundle> getSizedDataBundleJson(int packetSize) {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/sized/json/" + packetSize);
        return requestHeadersUriSpec.exchangeToFlux(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToFlux(DataBundle.class);
            } else {
                return Flux.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

    public Mono<byte[]> getOneDataBundleParquet() {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/sized/json/1");
        return requestHeadersUriSpec.exchangeToMono(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToMono(byte[].class);
            } else {
                return Mono.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

    public Flux<byte[]> getManyDataBundleParquet() {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/stream/string");
        return requestHeadersUriSpec.exchangeToFlux(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToFlux(byte[].class);
            } else {
                return Flux.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

    public Flux<byte[]> getSizedDataBundleParquet(int packetSize) {
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = webClientJson.get();
        requestHeadersUriSpec.uri("/sized/json/" + packetSize);
        return requestHeadersUriSpec.exchangeToFlux(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
                return resp.bodyToFlux(byte[].class);
            } else {
                return Flux.error(new RuntimeException("Non 2xx response: " + resp.statusCode()));
            }
        });
    }

}
