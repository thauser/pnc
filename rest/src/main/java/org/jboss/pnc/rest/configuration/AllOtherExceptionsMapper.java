/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.pnc.rest.configuration;

import org.jboss.pnc.rest.restmodel.response.error.ErrorResponseRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.lang.invoke.MethodHandles;

//TODO: This is temporarily disabled due to it being too general and catching all
// exceptions.  For example, this prevents invalid rest URL requests from returning
// the proper 404 error code.  Before this is re-enabled by uncommenting the @Provider line
// the exception type which is caught needs to be made more specific than just "Exception"
//@Provider
public class AllOtherExceptionsMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(Exception e) {
        logger.error("An exception occurred when processing REST response", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponseRest(e)).type(MediaType.APPLICATION_JSON).build();
    }
}
