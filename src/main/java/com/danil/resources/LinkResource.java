package com.danil.resources;


import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("links")
public class LinkResource {

    private static final AtomicInteger currentId = new AtomicInteger();

    private static final Map<String, String> links = new ConcurrentHashMap<>();

    private static final StringBuilder result = new StringBuilder();

    @GET
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


    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{id}")
    public Response getUrlById(final @PathParam("id") String id){
        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final String url = links.get(id);
        if (url == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(url).build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response shortUrl(final String url) {
        final int id = currentId.getAndIncrement();
        links.put(String.valueOf(id), url);
        return Response.ok(id).build();
    }


}
