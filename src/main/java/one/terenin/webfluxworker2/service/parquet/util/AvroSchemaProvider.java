package one.terenin.webfluxworker2.service.parquet.util;

import org.apache.avro.Schema;

public class AvroSchemaProvider {
    private static final String AS_JSON = """
            {
              "type": "record",
              "name": "DataBundle",
              "namespace": "one.terenin.datagenerator",
              "fields": [
                {"name": "uuid", "type": "string"},
                {"name": "name", "type": "string"},
                {"name": "description", "type": "string"},
                {"name": "type", "type": "string"},
                {"name": "mainCategory", "type": "string"},
                {"name": "price", "type": "string"},
                {"name": "productOwner", "type": "string"},
                {"name": "slaveCategories", "type": {"type": "array", "items": "string"}},
                {"name": "options", "type": {"type": "map", "values": "string"}},
                {"name": "characteristics", "type": {"type": "map", "values": "string"}}
              ]
            }""";

    public static final Schema AS_AVRO_SCHEMA = new Schema.Parser().parse(AS_JSON);
}