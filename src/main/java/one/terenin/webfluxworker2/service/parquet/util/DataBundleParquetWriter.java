package one.terenin.webfluxworker2.service.parquet.util;

import lombok.SneakyThrows;
import one.terenin.webfluxworker2.dto.DataBundle;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.io.PositionOutputStream;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DataBundleParquetWriter {

    public static byte[] writeToParquet(List<DataBundle> dataBundles, Schema schema) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputFile outputFile = new ByteArrayOutputFile(byteArrayOutputStream);

        try (ParquetWriter<GenericRecord> writer = AvroParquetWriter
                .<GenericRecord>builder(outputFile)
                .withSchema(schema)
                .withDataModel(GenericData.get())
                .withCompressionCodec(CompressionCodecName.GZIP)
                .build()) {

            for (DataBundle data : dataBundles) {
                writer.write(convertDataBundleToRecord(data, schema));
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static GenericRecord convertDataBundleToRecord(DataBundle data, Schema schema) {
        GenericRecord record = new GenericData.Record(schema);
        record.put("uuid", data.getUuid());
        record.put("name", data.getName());
        record.put("description", data.getDescription());
        record.put("type", data.getType());
        record.put("mainCategory", data.getMainCategory());
        record.put("price", data.getPrice());
        record.put("productOwner", data.getProductOwner());
        record.put("slaveCategories", data.getSlaveCategories());
        record.put("options", data.getOptions());
        record.put("characteristics", data.getCharacteristics());
        return record;
    }

    static class ByteArrayOutputFile implements OutputFile {
        private final ByteArrayOutputStream outputStream;

        public ByteArrayOutputFile(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public PositionOutputStream create(long blockSizeHint) {
            return new SeekableByteArrayOutputStream(outputStream);
        }

        @Override
        public PositionOutputStream createOrOverwrite(long blockSizeHint) {
            return new SeekableByteArrayOutputStream(outputStream);
        }

        @Override
        public boolean supportsBlockSize() {
            return false;
        }

        @Override
        public long defaultBlockSize() {
            return 0;
        }
    }

    static class SeekableByteArrayOutputStream extends PositionOutputStream {
        private final ByteArrayOutputStream outputStream;
        private long position = 0;

        public SeekableByteArrayOutputStream(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) {
            outputStream.write(b);
            position++;
        }

        @Override
        public void write(byte[] b, int off, int len) {
            outputStream.write(b, off, len);
            position += len;
        }

        @Override
        public long getPos() {
            return position;
        }

        @Override
        public void flush() {
            // no needed
        }

        @SneakyThrows
        @Override
        public void close() {
            outputStream.close();
        }
    }
}