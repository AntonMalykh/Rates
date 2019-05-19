package io.malykh.anton.core.domain.response

/**
 * Library errors
 */
public sealed class CoreError(message : String): Exception(message){
    /**
     * Error that occurs during obtaining the response
     */
    class ResponseError(message : String): CoreError(message)
}
