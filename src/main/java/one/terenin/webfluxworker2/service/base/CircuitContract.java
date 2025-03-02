package one.terenin.webfluxworker2.service.base;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

// call consumer -> work with data -> send data to consumer -> repeat
public interface CircuitContract<DATATYPE> {

    Mono<DATATYPE> imitateOneByOneSending(int iterationCount);
    Mono<DATATYPE> imitateOneByOneWithAnalyseSending(int iterationCount);

    Flux<DATATYPE> imitateManySending(int iterationCount);
    Flux<DATATYPE> imitateManySendingWithAnalyse(int iterationCount);

}
