package one.terenin.webfluxworker2.service.parquet;

import one.terenin.webfluxworker2.dto.DataBundle;
import one.terenin.webfluxworker2.service.base.ImitatorContract;
import one.terenin.webfluxworker2.service.parquet.util.AvroSchemaProvider;
import one.terenin.webfluxworker2.service.parquet.util.DataBundleParquetWriter;
import one.terenin.webfluxworker2.service.parquet.util.ParquetToDataBundleConverter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
// service for imitate "useful" job (data mapping, filtering and so on)
public class Imitator implements ImitatorContract<byte[]> {

    @Override
    public Optional<byte[]> getDataType() {
        return Optional.empty();
    }

    @Override
    public Consumer<byte[]> iteratorConsumer() {
        return null;
    }

    @Override
    public Function<byte[], ?> dataTypeMapper() {
        return null;
    }

    @Override
    public Predicate<byte[]> dataTypeFilteringPredicate() {
        return null;
    }

    @Override
    public Mono<byte[]> processMono(Mono<byte[]> mono) {
        return mono.handle((it, sink) -> {
            try {
                List<DataBundle> convert = ParquetToDataBundleConverter.convert(it);
                DataBundle dataBundle = convert.get(0);
                DataBundle newDB = DataBundle.builder()
                        .price(dataBundle.getPrice())
                        .name(dataBundle.getName())
                        .type(dataBundle.getType())
                        .uuid(dataBundle.getUuid() + "rebuildPQ")
                        .characteristics(dataBundle.getCharacteristics().entrySet().stream()
                                .peek(i -> i.setValue(i.getValue() + "rebuildPQ"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                        .build();
                sink.next(DataBundleParquetWriter.writeToParquet(Collections.singletonList(newDB), AvroSchemaProvider.AS_AVRO_SCHEMA));
            } catch (IOException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }

    @Override
    public Flux<byte[]> processFlux(Flux<byte[]> flux) {
        return flux.map(it -> {
            try {
                List<DataBundle> convert = ParquetToDataBundleConverter.convert(it);
                List<DataBundle> mappedData = convert.stream().map(dataBundle -> DataBundle.builder()
                        .price(dataBundle.getPrice())
                        .name(dataBundle.getName())
                        .type(dataBundle.getType())
                        .uuid(dataBundle.getUuid() + "rebuildPQ")
                        .characteristics(dataBundle.getCharacteristics().entrySet().stream().map(i -> {
                            i.setValue(i.getValue() + "rebuildPQ");
                            return i;
                        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                        .build()).toList();
                return DataBundleParquetWriter.writeToParquet(mappedData, AvroSchemaProvider.AS_AVRO_SCHEMA);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).subscribeOn(Schedulers.parallel());
    }
}
