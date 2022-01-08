package p5.bank;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * serializes {@link CustomSerializer#serialize(Transaction, Type, JsonSerializationContext) transaction}
     * @param transaction Transaction to be serialized
     * @param type Type of serialized object (not set)
     * @param jsonSerializationContext not set
     * @return serialized JsonElement
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject result = new JsonObject();
        JsonObject instance = new JsonObject(); //transaction information

        String classname = transaction.getClass().toString();
        String[] classParts = classname.split("\\."); // split to remove "class bank."
        classname = classParts[classParts.length-1]; // only select name of class
        result.addProperty("CLASSNAME", classname);

        if (transaction instanceof Transfer) {
            Transfer t = (Transfer) transaction;
            instance.addProperty("sender", t.getSender());
            instance.addProperty("recipient", t.getRecipient());
        }
        else if (transaction instanceof Payment){
            Payment p = (Payment) transaction;
            instance.addProperty("incomingInterest", p.getIncomingInterest());
            instance.addProperty("outgoingInterest", p.getOutgoingInterest());
        }

        instance.addProperty("date", transaction.getDate());
        instance.addProperty("amount", transaction.getAmount());
        instance.addProperty("description", transaction.getDescription());

        result.add("INSTANCE", instance);

        return result;
    }

    /**
     * deserializes {@link CustomSerializer#deserialize(JsonElement, Type, JsonDeserializationContext) jsonElement}
     * @param jsonElement JsonElement to be deserialized
     * @param type Type of deserialized Object
     * @param jsonDeserializationContext not set
     * @return {@link Transaction} contained in {@link CustomSerializer#deserialize(JsonElement, Type, JsonDeserializationContext) jsonElement}
     * @throws JsonParseException
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jobj = jsonElement.getAsJsonObject();
        String classname = jobj.get("CLASSNAME").getAsString();

        JsonElement jelem = jobj.get("INSTANCE");

        return switch(classname){
            case "Payment" -> new Gson().fromJson(jelem, Payment.class);
            case "Transfer" -> new Gson().fromJson(jelem, Transfer.class);
            case "IncomingTransfer" -> new Gson().fromJson(jelem, IncomingTransfer.class);
            case "OutgoingTransfer" -> new Gson().fromJson(jelem, OutgoingTransfer.class);
            default -> throw new JsonParseException("Unbekannter Typ!");
        };
    }
}