
package com.crocusgames.destinyinventorymanager.AccountInfoObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountMainObject {

    @SerializedName("Response")
    @Expose
    private Response response;
    @SerializedName("ErrorCode")
    @Expose
    private Long errorCode;
    @SerializedName("ThrottleSeconds")
    @Expose
    private Long throttleSeconds;
    @SerializedName("ErrorStatus")
    @Expose
    private String errorStatus;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("MessageData")
    @Expose
    private MessageData messageData;

    /**
     * 
     * @return
     *     The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The Response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * 
     * @return
     *     The errorCode
     */
    public Long getErrorCode() {
        return errorCode;
    }

    /**
     * 
     * @param errorCode
     *     The ErrorCode
     */
    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 
     * @return
     *     The throttleSeconds
     */
    public Long getThrottleSeconds() {
        return throttleSeconds;
    }

    /**
     * 
     * @param throttleSeconds
     *     The ThrottleSeconds
     */
    public void setThrottleSeconds(Long throttleSeconds) {
        this.throttleSeconds = throttleSeconds;
    }

    /**
     * 
     * @return
     *     The errorStatus
     */
    public String getErrorStatus() {
        return errorStatus;
    }

    /**
     * 
     * @param errorStatus
     *     The ErrorStatus
     */
    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The messageData
     */
    public MessageData getMessageData() {
        return messageData;
    }

    /**
     * 
     * @param messageData
     *     The MessageData
     */
    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

}
