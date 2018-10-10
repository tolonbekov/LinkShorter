package com.danil.resources;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.print.Doc;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("links")
public class LinkResource {

    private static final MongoCollection<Document> LINKS_COLLECTION;

    static {
        final MongoClient mongo = new MongoClient("localhost", 27017);
        final MongoDatabase db = mongo.getDatabase("hexlet");
        LINKS_COLLECTION = db.getCollection("links");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public Response getUrlById(final @PathParam("id") String id) {
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final FindIterable<Document> resultsIterable = LINKS_COLLECTION.find(new Document("id", id));
        final Iterator<Document> resultIterator = resultsIterable.iterator();
        if (!resultIterator.hasNext()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final String url = resultIterator.next().getString("url");
        if (url == null || url == "") {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(url).build();


    }


    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response shortUrl(final String url) {
        int attempt = 0;
        while (attempt < 5) {
            final String id = getRandomId();
            final Document newShortDoc = new Document("id", id);
            newShortDoc.put("url", url);
            try {
                LINKS_COLLECTION.insertOne(newShortDoc);
                return Response.ok(id).build();
            } catch (MongoWriteException e) {
                System.out.println(e.getMessage());
            }
            attempt++;
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    private static String getRandomId() {
        String possibleCharacters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder idBuilder = new StringBuilder();
        Random rnd = new Random();
        while (idBuilder.length() < 5) {
            int index = (int) (rnd.nextFloat() * possibleCharacters.length());
            idBuilder.append(possibleCharacters.charAt(index));
        }
        return idBuilder.toString();
    }


}

/*@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("")
    public Response getAllUrl() {
        if (links.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            for (String key : links.keySet()) {
                //System.out.println(key + "=" + links.get(key));
                result.append(key + "=" + links.get(key));
            }
            return Response.ok(result).build();
        }


    }*/