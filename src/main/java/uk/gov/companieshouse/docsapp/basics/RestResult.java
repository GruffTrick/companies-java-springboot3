package uk.gov.companieshouse.docsapp.basics;

import java.util.Date;

public class RestResult<R> {
    public enum Status {
        SUCCESS,
        FAILURE
    }

    public RestResult() {
        this(Status.SUCCESS, null, null);
    }

    public RestResult(Exception exception) {
        this(Status.FAILURE, null, exception);
    }

    public RestResult(R data) {
        this(Status.SUCCESS, data, null);
    }

    private RestResult(Status status, R data, Exception exception) {
        this.status = status;
        this.data = data;
        this.error = exception;
        this.timestamp = new Date();
    }

    private final Status status;
    private final R data;
    private final Exception error;
    private final Date timestamp;

    public Status getStatus() {
        return status;
    }

    public R getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
