package fi.dvv.xroad.resttestservice.model;

public record ErrorDto(String errorMessage, int httpStatus) { }
