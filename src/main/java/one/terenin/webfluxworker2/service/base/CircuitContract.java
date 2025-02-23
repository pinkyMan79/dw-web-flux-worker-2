package one.terenin.webfluxworker2.service.base;

import java.util.List;

// call consumer -> work with data -> send data to consumer -> repeat
public interface CircuitContract<DATATYPE> {

    void imitateOneByOneSending(int iterationCount);
    List<DATATYPE> imitateOneByOneWithAnalyseSending(int iterationCount);

    void imitateManySending(int iterationCount);
    List<DATATYPE> imitateManySendingWithAnalyse(int iterationCount);

}
