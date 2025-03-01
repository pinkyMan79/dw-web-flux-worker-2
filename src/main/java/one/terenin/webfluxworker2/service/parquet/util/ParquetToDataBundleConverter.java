package one.terenin.webfluxworker2.service.parquet.util;

import one.terenin.webfluxworker2.dto.DataBundle;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.InputFile;
import org.apache.parquet.io.SeekableInputStream;
import org.apache.parquet.io.api.Binary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class ParquetToDataBundleConverter {

    public static List<DataBundle> convert(byte[] parquetBytes) throws IOException {
        InputFile inputFile = new ByteArrayInputFile(parquetBytes);

        try (ParquetReader<GenericRecord> reader = AvroParquetReader
                .<GenericRecord>builder(inputFile)
                .withDataModel(org.apache.avro.generic.GenericData.get())
                .build()) {

            List<DataBundle> dataBundles = new ArrayList<>();
            GenericRecord record;

            while ((record = reader.read()) != null) {
                dataBundles.add(convertRecordToDataBundle(record));
            }

            return dataBundles;
        }
    }

    private static DataBundle convertRecordToDataBundle(GenericRecord record) {
        return DataBundle.builder()
                .uuid(getString(record.get("uuid")))
                .name(getString(record.get("name")))
                .description(getString(record.get("description")))
                .type(getString(record.get("type")))
                .mainCategory(getString(record.get("mainCategory")))
                .price(getString(record.get("price")))
                .productOwner(getString(record.get("productOwner")))
                .slaveCategories(getStringList(record.get("slaveCategories")))
                .options(getStringMap(record.get("options")))
                .characteristics(getStringMap(record.get("characteristics")))
                .build();
    }

    private static String getString(Object value) {
        return Optional.ofNullable(value).map(it -> (String) value).orElse(null);
    }

    private static List<String> getStringList(Object avroArray) {
        if (avroArray == null) return Collections.emptyList();
        return ((List<?>) avroArray).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private static Map<String, String> getStringMap(Object avroMap) {
        if (avroMap == null) return Collections.emptyMap();
        Map<?, ?> map = (Map<?, ?>) avroMap;
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
    }

    static class ByteArrayInputFile implements InputFile {
        private final byte[] data;

        public ByteArrayInputFile(byte[] data) {
            this.data = data;
        }

        @Override
        public long getLength() {
            return data.length;
        }

        @Override
        public SeekableInputStream newStream() {
            return new SeekableByteArrayInputStream(data);
        }
    }

    static class SeekableByteArrayInputStream extends SeekableInputStream {
        private final ByteArrayInputStream inputStream;
        private long position = 0;

        public SeekableByteArrayInputStream(byte[] data) {
            this.inputStream = new ByteArrayInputStream(data);
        }

        @Override
        public int read() {
            int result = inputStream.read();
            if (result != -1) position++;
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            int bytesRead = inputStream.read(b, off, len);
            if (bytesRead != -1) position += bytesRead;
            return bytesRead;
        }

        @Override
        public void seek(long newPos) {
            inputStream.reset();
            inputStream.skip(newPos);
            position = newPos;
        }

        @Override
        public void readFully(byte[] bytes) throws IOException {
            int i = 0;
            int data = inputStream.read();
            while (data != -1 || i < bytes.length) {
                data = inputStream.read();
                bytes[i++] = (byte) data;
            }
        }

        @Override
        public void readFully(byte[] bytes, int i, int i1) throws IOException {

        }

        @Override
        public int read(ByteBuffer byteBuffer) throws IOException {
            return inputStream.read();
        }

        @Override
        public void readFully(ByteBuffer byteBuffer) throws IOException {
            byteBuffer.put(inputStream.readAllBytes());
        }

        @Override
        public long getPos() {
            return position;
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }
}