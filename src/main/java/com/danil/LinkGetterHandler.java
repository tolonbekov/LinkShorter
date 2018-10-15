package com.danil;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;

public class LinkGetterHandler {

    private static final Table LINKS_TABLE;

    static {
        final AmazonDynamoDB client = AmazonDynamoDBAsyncClientBuilder.standard().build();
        final DynamoDB dynamoDB = new DynamoDB(client);

        LINKS_TABLE = dynamoDB.getTable("Links");
    }

    public String getLink(String id, Context context) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        final Item item = LINKS_TABLE.getItem("id", id);
        final String url = item.getString("url");
        if (url == null || url == "") {
            return null;
        }
        return url;
    }
}
