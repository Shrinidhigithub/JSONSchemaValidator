import java.util.*;

public class JsonSchemaValidator {

    @SuppressWarnings("unchecked")
    public static String validate(Map<String, Object> schema, Object data, String path) {
        String type = (String) schema.get("type");

        switch (type) {
            case "object":
                if (!(data instanceof Map)) {
                    return error(path, "should be an object");
                }
                Map<String, Object> dataMap = (Map<String, Object>) data;
                Map<String, Object> properties = (Map<String, Object>) schema.get("properties");
                List<String> required = (List<String>) schema.get("required");

                if (required != null) {
                    for (String key : required) {
                        if (!dataMap.containsKey(key)) {
                            return "Error: Missing required field '" + key + "'";
                        }
                    }
                }

                if (properties != null) {
                    for (String key : properties.keySet()) {
                        if (dataMap.containsKey(key)) {
                            String result = validate((Map<String, Object>) properties.get(key), dataMap.get(key), key);
                            if (!result.equals("Valid JSON")) {
                                return result;
                            }
                        }
                    }
                }
                break;

            case "string":
                if (!(data instanceof String)) {
                    return error(path, "should be a string");
                }
                break;

            case "integer":
                if (!(data instanceof Integer)) {
                    return error(path, "should be an integer");
                }
                int val = (Integer) data;
                if (schema.containsKey("minimum")) {
                    int min = (Integer) schema.get("minimum");
                    if (val < min) {
                        return error(path, "should be >= " + min);
                    }
                }
                if (schema.containsKey("maximum")) {
                    int max = (Integer) schema.get("maximum");
                    if (val > max) {
                        return error(path, "should be <= " + max);
                    }
                }
                break;

            case "boolean":
                if (!(data instanceof Boolean)) {
                    return error(path, "should be a boolean");
                }
                break;

            case "array":
                if (!(data instanceof List)) {
                    return error(path, "should be an array");
                }
                List<Object> dataList = (List<Object>) data;
                Map<String, Object> itemSchema = (Map<String, Object>) schema.get("items");
                for (int i = 0; i < dataList.size(); i++) {
                    String result = validate(itemSchema, dataList.get(i), path + "[" + i + "]");
                    if (!result.equals("Valid JSON")) {
                        return result;
                    }
                }
                break;

            default:
                return error(path, "Unsupported type '" + type + "'");
        }

        return "Valid JSON";
    }

    public static String error(String path, String message) {
        return "Error: '" + path + "' " + message;
    }

    public static void main(String[] args) {
        // Schema
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> nameSchema = new HashMap<>();
        nameSchema.put("type", "string");

        Map<String, Object> ageSchema = new HashMap<>();
        ageSchema.put("type", "integer");
        ageSchema.put("minimum", 18);

        properties.put("name", nameSchema);
        properties.put("age", ageSchema);

        schema.put("properties", properties);
        schema.put("required", Arrays.asList("name", "age"));

        // Valid data
        Map<String, Object> validData = new HashMap<>();
        validData.put("name", "Alice");
        validData.put("age", 25);

        // Invalid data
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("name", "Bob");
        invalidData.put("age", "twenty");

        // Output with prefix
        System.out.println("Valid Case: \"" + validate(schema, validData, "") + "\"");
        System.out.println("Invalid Case: \"" + validate(schema, invalidData, "") + "\"");
    }
}
