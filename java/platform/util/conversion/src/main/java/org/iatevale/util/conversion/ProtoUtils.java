package org.iatevale.util.conversion;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public class ProtoUtils {

    static public String messageToJson(Message message) {
        JsonFormat.Printer printer = JsonFormat
                .printer()
                .preservingProtoFieldNames()
                .includingDefaultValueFields();
        try {
            return printer.print(message);
        } catch (InvalidProtocolBufferException e) {
            return String.format("{%n \"error\": \"Can't convert ProtoMessage to JSON\", %n\"exception\": \"%s\"%n}", e.getMessage());
        }
    }

    static public String messageToJsonOneLine(Message message) {
        JsonFormat.Printer printer = JsonFormat
                .printer()
                .preservingProtoFieldNames()
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace();
        try {
            return printer.print(message);
        } catch (InvalidProtocolBufferException e) {
            return String.format("{ \"error\": \"Can't convert ProtoMessage to JSON\", \"exception\": \"%s\"}", e.getMessage());
        }
    }

}
