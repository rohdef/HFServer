package dk.rohdef.hfserver.jersey;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by rohdef on 7/29/15.
 */
public class ObjectIdDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        TreeNode treeNode = jp.readValueAsTree();
        JsonNode oid = ((JsonNode) treeNode).get("$oid");
        if (oid != null)
            return oid.asText();
        else
            return ((JsonNode) treeNode).asText();
    }
}
