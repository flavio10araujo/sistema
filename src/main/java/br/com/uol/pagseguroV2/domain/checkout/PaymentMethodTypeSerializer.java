package br.com.uol.pagseguroV2.domain.checkout;

import br.com.uol.pagseguroV2.enums.PaymentMethodType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class PaymentMethodTypeSerializer extends JsonSerializer<List<PaymentMethodType>> {

    @Override
    public void serialize(List<PaymentMethodType> paymentMethods, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (PaymentMethodType method : paymentMethods) {
            gen.writeStartObject();
            gen.writeStringField("type", method.name());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
