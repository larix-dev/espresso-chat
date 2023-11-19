package com.larix.proto;

import lombok.Data;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.DefaultByteOrder;
import org.indunet.fastproto.annotation.StringType;

import java.util.function.Function;

import static org.indunet.fastproto.ByteOrder.BIG;

@Data
@DefaultByteOrder(BIG)
public class MessagePacket {

    @StringType(offset = 0, length = 16)
    @DecodingFormula(RemoveNull.class)
    private final String username;

    @StringType(offset = 16, length = 1008)
    @DecodingFormula(RemoveNull.class)
    private final String message;

    public static class RemoveNull implements Function<String, String> {
        @Override
        public String apply(final String string) {
            return string.replaceAll("\u0000", "");
        }
    }

}


