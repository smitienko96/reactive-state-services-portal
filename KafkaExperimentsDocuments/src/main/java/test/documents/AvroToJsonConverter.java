package test.documents;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.*;

import java.io.*;

/**
 * @author s.smitienko
 */
public class AvroToJsonConverter {


    public static void main(String[] args) throws Exception {
        Schema avroSchema = loadSchema("documenttypecreated.avsc");

    }
    private static Schema loadSchema(String fileName) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("C" +
                ":\\Projects\\Kite\\KafkaExperimentsDocuments\\src\\main" +
                "\\avro\\" + fileName));
        Schema.Parser parser = new Schema.Parser();
        return parser.parse(inputStream);
    }

    static void convertAvroToJson(InputStream inputStream, OutputStream outputStream, Schema schema)
            throws IOException {
        DatumReader<Object> reader = new GenericDatumReader<>(schema);
        DatumWriter<Object> writer = new GenericDatumWriter<>(schema);

        BinaryDecoder binaryDecoder = DecoderFactory.get().binaryDecoder(inputStream, null);

        JsonEncoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, outputStream, true);
        Object datum = null;
        while (!binaryDecoder.isEnd()) {
            datum = reader.read(datum, binaryDecoder);
            writer.write(datum, jsonEncoder);
            jsonEncoder.flush();
        }
        outputStream.flush();
    }
}
