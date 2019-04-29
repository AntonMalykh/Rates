package io.malykh.anton.core.domain.response

public sealed class CoreError(message : String): Exception(message){
    class ResponseError(message : String): CoreError(message)
}
