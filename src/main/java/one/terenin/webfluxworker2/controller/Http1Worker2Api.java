package one.terenin.webfluxworker2.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.dto.DataBundle;
import one.terenin.webfluxworker2.service.json.CircuitService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Http1Worker2Api implements one.terenin.webfluxworker2.api.Http1Worker2Api {

    private final CircuitService circuitService;

    @Override
    public Flux<DataBundle> streamJsonData() {
        return circuitService.imitateManySending(20);
    }

    @Override
    public Flux<byte[]> streamParquetData() {
        return null;
    }

    @Override
    public Flux<DataBundle> streamOctetJsonData() {
        return circuitService.imitateManySending(20);
    }

    @Override
    public Flux<byte[]> streamOctetParquetData() {
        return null;
    }

    @Override
    public Flux<DataBundle> sizedJsonData(int count) {
        return circuitService.imitateManySendingWithAnalyse(20);
    }

    @Override
    public Flux<byte[]> sizedParquetData(int count) {
        return null;
    }

    @Override
    public Flux<DataBundle> duplexJsonData(int count) {
        return circuitService.imitateManySendingWithAnalyse(20);
    }

    @Override
    public Flux<byte[]> duplexParquetData(int count) {
        return null;
    }

    @Override
    public Flux<DataBundle> consumeStreamJsonData(DataBundle dataBundle) {
        return null;
    }

    @Override
    public Flux<byte[]> consumeStreamParquetData(String utf8EncodedByteArray) {
        return null;
    }

    @Override
    public Flux<DataBundle> consumeStreamOctetJsonData(DataBundle dataBundle) {
        return null;
    }

    @Override
    public Flux<byte[]> consumeStreamOctetParquetData(String utf8EncodedByteArray) {
        return null;
    }

    @Override
    public Flux<DataBundle> consumeSizedJsonData(List<DataBundle> dataBundle) {
        return null;
    }

    @Override
    public Flux<byte[]> consumeSizedParquetData(List<String> utf8EncodedByteArray) {
        return null;
    }

    @Override
    public Flux<DataBundle> consumeDuplexJsonData(List<DataBundle> dataBundle) {
        return null;
    }

    @Override
    public Flux<byte[]> consumeDuplexParquetData(List<String> utf8EncodedByteArray) {
        return null;
    }
}
