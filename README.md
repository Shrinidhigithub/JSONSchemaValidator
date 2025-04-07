Problem Statement
You need to implement a JSON Schema Validator from scratch, ensuring that a given JSON object adheres to a defined schema.

Example:

JSON Schema:
{
  "type": "object",
  "properties": {
    "name": { "type": "string" },
    "age": { "type": "integer", "minimum": 18 }
  },
  "required": ["name", "age"]
}

JSON Object Valid:
{
  "name": "Alice",
  "age": 25
}

JSON Object InValid:
{
  "name": "Bob",
  "age": "twenty"
}

Valid Case: "Valid JSON"
Invalid Case: "Error: 'age' should be an integer"


