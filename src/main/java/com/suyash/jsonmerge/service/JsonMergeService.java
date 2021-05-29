package com.suyash.jsonmerge.service;

import com.google.gson.Gson;
import com.jsontypedef.jtd.Schema;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class JsonMergeService {

    public Schema makeSchema() {
        String schemaJson1 = String.join("\n",
                "{",
                "  \"properties\": {",
                "    \"phones\": {",
                "       \"properties\": {",
                "         \"gender\": {",
                "           \"values\": { ",
                "             \"definitions\": {",
                "               \"someKey\": {",
                "                  \"enum\": [\"A\", \"C\", \"B\"]",
                "                }",
                "              }",
                "            }",
                "          }",
                "        }",
                "    },",
                "    \"elements\": { \"type\": \"uint32\" }",
                "  },",
                "  \"mapping\": {",
                "    \"abc\": { \"type\": \"string\" }",
                "  },",
                "  \"enum\": [\"abc\",\"def\",\"ghi\"],",
                "  \"discriminator\": \"dis\"",
                "}");
        System.out.println(schemaJson1);
        Gson gson = new Gson();
        Schema schema1 = gson.fromJson(schemaJson1, Schema.class);

        String schemaJson2 = String.join("\n",
                "{",
                "  \"properties\": {",
                "    \"phones\": {",
                "       \"properties\": {",
                "         \"gender\": {",
                "           \"values\": { ",
                "             \"definitions\": {",
                "               \"someKey\": {",
                "                  \"enum\": [\"A\", \"B\", \"C\"] ",
                "               }",
                "              }",
                "           }",
                "         }",
                "       }",
                "    },",
                "    \"elements\": { \"type\": \"uint32\" }",
                "  },",
                "  \"mapping\": {",
                "    \"abc\": { \"type\": \"string\" }",
                "  },",
                "  \"enum\": [\"ghi\",\"abc\",\"def\"],",
                "  \"discriminator\": \"dis\"",
                "}");
        Schema schema2 = gson.fromJson(schemaJson2, Schema.class);

        Schema mergedSchema = new Schema();
        mergedSchema = merge(schema1, schema2);
        System.out.println("final MergedSchema: " + schema1);
        System.out.println("final MergedSchema: " + mergedSchema);
        return mergedSchema;
    }

    public Schema merge(Schema schema1, Schema schema2) {
//        System.out.println(schema1.getAdditionalProperties());
//        System.out.println(schema1.getDefinitions());
//        System.out.println(schema1.getDiscriminator());
//        System.out.println(schema1.getElements());
//        System.out.println(schema1.getEnum());
//        System.out.println(schema1.getForm());
//        System.out.println(schema1.getMapping());
//        System.out.println(schema1.getMetadata());
//        System.out.println(schema1.getOptionalProperties());
//        System.out.println(schema1.getProperties());
//        System.out.println(schema1.getRef());
//        System.out.println(schema1.getType());
//        System.out.println(schema1.getValues());

        Schema mergedSchema = null;

        if(schema1.getDefinitions() != null && schema2.getDefinitions() != null) {
            Map<String, Schema> def1 = schema1.getDefinitions();
            Map<String, Schema> def2 = schema2.getDefinitions();

            for(Map.Entry<String, Schema> entry : def1.entrySet()) {
                String key = entry.getKey();
                if(def2.containsKey(key)) {
                    if(merge(def1.get(key), def2.get(key)) != null) {
                        if(mergedSchema == null) {
                            mergedSchema = new Schema();
                        }
                        if(mergedSchema.getDefinitions() == null) {
                            Map<String, Schema> map = new HashMap<>();
                            map.put(key, entry.getValue());
                            mergedSchema.setDefinitions(map);
                        } else {
                            Map<String, Schema> map = mergedSchema.getDefinitions();
                            map.put(key, entry.getValue());
                            mergedSchema.setDefinitions(map);
                        }
                    }
                }
            }
        }

        if(schema1.getRef() != null && schema2.getRef() != null && schema1.getRef().equals(schema2.getRef())) {
            if(mergedSchema == null) {
                mergedSchema = new Schema();
            }
            mergedSchema.setRef(schema1.getRef());
        }

        if(schema1.getType() != null && schema2.getType() != null) {
            System.out.println("schema1 type" + schema1.getType());
            System.out.println("schema2 type" + schema2.getType());

            if(schema1.getType().equals(schema2.getType())) {
                if(mergedSchema == null) {
                    mergedSchema = new Schema();
                }
                mergedSchema.setType(schema1.getType());
            }
        }

        if(schema1.getEnum() != null && schema2.getEnum() != null) {
            Set<String> enum1 = schema1.getEnum();
            Set<String> enum2 = schema2.getEnum();

            if(enum1.equals(enum2)) {
                if(mergedSchema == null) {
                    mergedSchema = new Schema();
                }
                mergedSchema.setEnum(enum1);
            }
        }

        if(schema1.getElements() != null && schema2.getElements() != null) {
            Schema elements1 = schema1.getElements();
            Schema elements2 = schema2.getElements();

            if(merge(elements1, elements2) != null) {
                if(mergedSchema == null) {
                    mergedSchema = new Schema();
                }
                mergedSchema.setElements(elements1);
            }
        }

        if(schema1.getProperties() != null && schema2.getProperties() != null) {
            Map<String, Schema> prop1 = schema1.getProperties();
            Map<String, Schema> prop2 = schema2.getProperties();

            System.out.println("prop1:" + prop1);
            System.out.println("prop2:" + prop2);

            for(Map.Entry<String, Schema> entry : prop1.entrySet()) {
                String key = entry.getKey();
                if(prop2.containsKey(key)) {
                    if(merge(prop1.get(key), prop2.get(key)) != null) {
                        if(mergedSchema == null) {
                            mergedSchema = new Schema();
                        }
                        if(mergedSchema.getProperties() == null) {
                            Map<String, Schema> map = new HashMap<>();
                            map.put(key, entry.getValue());
                            mergedSchema.setProperties(map);
                        } else {
                            Map<String, Schema> map = mergedSchema.getProperties();
                            map.put(key, entry.getValue());
                            mergedSchema.setProperties(map);
                        }
                    }
                }
            }
        }

        if(schema1.getOptionalProperties() != null && schema2.getOptionalProperties() != null) {
            Map<String, Schema> prop1 = schema1.getOptionalProperties();
            Map<String, Schema> prop2 = schema2.getOptionalProperties();

            System.out.println("prop1:" + prop1);
            System.out.println("prop2:" + prop2);

            for(Map.Entry<String, Schema> entry : prop1.entrySet()) {
                String key = entry.getKey();
                if(prop2.containsKey(key)) {
                    if(merge(prop1.get(key), prop2.get(key)) != null) {
                        if(mergedSchema == null) {
                            mergedSchema = new Schema();
                        }
                        if(mergedSchema.getOptionalProperties() == null) {
                            Map<String, Schema> map = new HashMap<>();
                            map.put(key, entry.getValue());
                            mergedSchema.setOptionalProperties(map);
                        } else {
                            Map<String, Schema> map = mergedSchema.getOptionalProperties();
                            map.put(key, entry.getValue());
                            mergedSchema.setOptionalProperties(map);
                        }
                    }
                }
            }
        }

        if(schema1.getValues() != null && schema2.getValues() != null) {
            Schema values1 = schema1.getValues();
            Schema values2 = schema2.getValues();

            if(merge(values1, values2) != null) {
                if(mergedSchema == null) {
                    mergedSchema = new Schema();
                }
                mergedSchema.setValues(values1);
            }
        }

        if(schema1.getDiscriminator() != null && schema2.getDiscriminator() != null && schema1.getDiscriminator().equals(schema2.getDiscriminator())) {
            if(mergedSchema == null) {
                mergedSchema = new Schema();
            }
            mergedSchema.setDiscriminator(schema1.getDiscriminator());
        }

        if(schema1.getMapping() != null && schema2.getMapping() != null) {
            Map<String, Schema> prop1 = schema1.getMapping();
            Map<String, Schema> prop2 = schema2.getMapping();

            System.out.println("map1:" + prop1);
            System.out.println("map2:" + prop2);

            for(Map.Entry<String, Schema> entry : prop1.entrySet()) {
                String key = entry.getKey();
                if(prop2.containsKey(key)) {
                    if(merge(prop1.get(key), prop2.get(key)) != null) {
                        if(mergedSchema == null) {
                            mergedSchema = new Schema();
                        }
                        if(mergedSchema.getMapping() == null) {
                            Map<String, Schema> map = new HashMap<>();
                            map.put(key, entry.getValue());
                            mergedSchema.setMapping(map);
                        } else {
                            Map<String, Schema> map = mergedSchema.getMapping();
                            map.put(key, entry.getValue());
                            mergedSchema.setMapping(map);
                        }
                    }
                }
            }
        }

        System.out.println("mergedSchema:" + mergedSchema);
        return mergedSchema;
    }

}
