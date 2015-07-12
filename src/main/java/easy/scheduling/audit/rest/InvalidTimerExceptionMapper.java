
package easy.scheduling.audit.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidTimerExceptionMapper implements ExceptionMapper<IllegalStateException> {

    @Override
    public Response toResponse(IllegalStateException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
}
