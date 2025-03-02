package one.terenin.webfluxworker2.service.json;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.dto.DataBundle;
import one.terenin.webfluxworker2.service.ConsumerCallerService;
import one.terenin.webfluxworker2.service.base.CircuitContract;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
// service that use caller-service and imitator to generate circuit of http calls
public class CircuitService implements CircuitContract<DataBundle> {

    private final Imitator imitator;
    private final ConsumerCallerService callerService;
    private final WebClient webClientJson;


    @Override
    public Mono<DataBundle> imitateOneByOneSending(int iterationCount) {
        // loop logging
        WebClient.RequestHeadersSpec<?> body = webClientJson.post().body(imitator.processMono(callerService.getOneDataBundleJson()), DataBundle.class);
        return body.exchangeToMono(it -> it.bodyToMono(DataBundle.class));
    }

    @Override
    public Mono<DataBundle> imitateOneByOneWithAnalyseSending(int iterationCount) {
        // loop logging
        WebClient.RequestHeadersSpec<?> body = webClientJson.post().body(imitator.processMono(callerService.getOneDataBundleJson()), DataBundle.class);
        return body.exchangeToMono(it -> it.bodyToMono(DataBundle.class));
    }

    @Override
    public Flux<DataBundle> imitateManySending(int iterationCount) {
        // loop
        Flux<DataBundle> sizedDataBundleJson = callerService.getSizedDataBundleJson(iterationCount);
        return imitator.processFlux(sizedDataBundleJson);
    }

    @Override
    public Flux<DataBundle> imitateManySendingWithAnalyse(int iterationCount) {
        // append statistics
        Flux<DataBundle> sizedDataBundleJson = callerService.getSizedDataBundleJson(iterationCount);
        return imitator.processFlux(sizedDataBundleJson);
    }
}
