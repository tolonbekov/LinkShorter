package com.danil.resources;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface LinkService {

    @LambdaFunction(functionName="linksAdder")
    String addLink(String link);

    @LambdaFunction(functionName="getLink")
    String getLink(String id);


}

