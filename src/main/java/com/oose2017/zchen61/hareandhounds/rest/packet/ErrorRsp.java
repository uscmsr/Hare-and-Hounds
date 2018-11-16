package com.oose2017.zchen61.hareandhounds.rest.packet;

/**
 * Response upon error
 */
public class ErrorRsp {
    private String reason;

    public ErrorRsp(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
