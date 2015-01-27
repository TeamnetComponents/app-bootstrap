/**
 * BaseController.java
 *
 * Copyright (c) 2014 Teamnet. All Rights Reserved.
 *
 * This source file may not be copied, modified or redistributed,
 * in whole or in part, in any form or for any reason, without the express
 * written consent of Teamnet.
 **/

package ro.teamnet.bootstrap.web.rest;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import ro.teamnet.bootstrap.extend.AppPageableHandlerMethodArgumentResolver;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseController {
    @Inject
    private RequestMappingHandlerAdapter adapter;

    @Inject
    private AppPageableHandlerMethodArgumentResolver appPageableHandlerMethodArgumentResolver;

    @PostConstruct
    public void prioritizeCustomArgumentMethodHandlers () {
        List<HandlerMethodArgumentResolver> argumentResolvers =
                new ArrayList<>(adapter.getArgumentResolvers ());
        List<HandlerMethodArgumentResolver> customResolvers =
                adapter.getCustomArgumentResolvers ();
        customResolvers.add(0, appPageableHandlerMethodArgumentResolver);
        argumentResolvers.removeAll (customResolvers);
        argumentResolvers.addAll (0, customResolvers);
        adapter.setArgumentResolvers (argumentResolvers);
    }
}
