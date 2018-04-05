package com.cxd.rtcroom;

import com.cxd.rtcroom.dto.JSONResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ws03089
 */
@ControllerAdvice
public class ExceptionAdvice {

    private static Log LOG = LogFactory.getLog(ExceptionAdvice.class);

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Object processException(BizException e) {
        String msg = e.getMessage();
        LOG.error(msg);
        e.printStackTrace();
        return new JSONResult(false, msg);

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception ex) {
        LOG.error(ex.getMessage());

        JSONResult body = new JSONResult(false, ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

}
