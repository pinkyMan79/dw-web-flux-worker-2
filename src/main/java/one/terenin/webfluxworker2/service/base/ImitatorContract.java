package one.terenin.webfluxworker2.service.base;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

// DATATYPE - json/parquet
// contract for service that imitate "useful" job
public interface ImitatorContract<DATATYPE> {

    Optional<DATATYPE> getDataType();

    Consumer<DATATYPE> iteratorConsumer();
    Function<DATATYPE, ?> dataTypeMapper();
    Predicate<DATATYPE> dataTypeFilteringPredicate();

    Mono<DATATYPE> processMono(Mono<DATATYPE> mono);
    Flux<DATATYPE> processFlux(Flux<DATATYPE> mono);


}
