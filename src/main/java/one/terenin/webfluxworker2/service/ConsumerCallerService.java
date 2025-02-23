package one.terenin.webfluxworker2.service;

import lombok.RequiredArgsConstructor;
import one.terenin.webfluxworker2.dto.DataBundle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
// call the first consumer service
public class ConsumerCallerService {

    private final WebClient webClientJson;
    private final WebClient webClientParquet;

    public Optional<DataBundle> getOneDataBundleJson() {
        return Optional.empty();
    }

    public Optional<List<DataBundle>> getManyDataBundleJson() {
        return Optional.empty();
    }

    public Optional<List<DataBundle>> getSizedDataBundleJson(int packetSize) {
        return Optional.empty();
    }

    public Optional<byte[]> getOneDataBundleParquet() {
        return Optional.empty();
    }

    public Optional<List<byte[]>> getManyDataBundleParquet() {
        return Optional.empty();
    }

    public Optional<List<byte[]>> getSizedDataBundleParquet(int packetSize) {
        return Optional.empty();
    }

}
