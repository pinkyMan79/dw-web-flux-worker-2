package one.terenin.webfluxworker2.service.json;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.dto.DataBundle;
import one.terenin.webfluxworker2.service.ConsumerCallerService;
import one.terenin.webfluxworker2.service.base.ImitatorContract;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
// service for imitate "useful" job (data mapping, filtering and so on)
public class Imitator implements ImitatorContract<DataBundle> {

    private final ConsumerCallerService callerService;

    @Override
    public Optional<DataBundle> getDataType() {
        return Optional.empty();
    }

    @Override
    public Consumer<DataBundle> iteratorConsumer() {
        return System.out::println;
    }

    @Override
    public Function<DataBundle, ?> dataTypeMapper() {
        return filtered -> {
            //reload data
            return DataBundle.builder()
                    .price(filtered.getPrice())
                    .name(filtered.getName())
                    .type(filtered.getType())
                    .uuid(filtered.getUuid() + "mapped")
                    .characteristics(filtered.getCharacteristics().entrySet().stream()
                            .peek(it -> it.setValue(it.getValue() + "mapped"))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                    .build();
        };
    }

    @Override
    public Predicate<DataBundle> dataTypeFilteringPredicate() {
        return it -> it.getCharacteristics().size() > 3
                && it.getName().length() > 2
                && it.getType().length() > 2;
    }

    @Override
    public Mono<DataBundle> processMono(Mono<DataBundle> mono) {
        return mono.map(filtered -> {
            //reload data
            return DataBundle.builder()
                    .price(filtered.getPrice())
                    .name(filtered.getName())
                    .type(filtered.getType())
                    .uuid(filtered.getUuid() + "rebuild")
                    .characteristics(filtered.getCharacteristics().entrySet().stream().map(it -> {
                        it.setValue(it.getValue() + "rebuild");
                        return it;
                    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                    .build();
        });
    }

    @Override
    public Flux<DataBundle> processFlux(Flux<DataBundle> flux) {
        return flux
                .filter(it -> it.getName().length() > 2)
                .map(filtered -> {
                    //reload data
                    return DataBundle.builder()
                            .price(filtered.getPrice())
                            .name(filtered.getName())
                            .type(filtered.getType())
                            .uuid(filtered.getUuid() + "rebuild")
                            .characteristics(filtered.getCharacteristics().entrySet().stream().map(it -> {
                                it.setValue(it.getValue() + "rebuild");
                                return it;
                            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                            .build();
                }).subscribeOn(Schedulers.parallel());
    }
}
