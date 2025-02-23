package one.terenin.webfluxworker2.controller;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.service.CircuitService;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class Http1Worker2Api implements one.terenin.webfluxworker2.api.Http1Worker2Api {

    private final CircuitService circuitService;

    @Override
    public Flux<String> streamJsonData() {
        return null;
    }

    @Override
    public Flux<byte[]> streamParquetData() {
        return null;
    }

    @Override
    public Flux<String> streamOctetJsonData() {
        return null;
    }

    @Override
    public Flux<byte[]> streamOctetParquetData() {
        return null;
    }

    @Override
    public Flux<String> sizedJsonData(int count) {
        return null;
    }

    @Override
    public Flux<byte[]> sizedParquetData(int count) {
        return null;
    }

    @Override
    public Flux<String> duplexJsonData(int count) {
        return null;
    }

    @Override
    public Flux<byte[]> duplexParquetData(int count) {
        return null;
    }
}
