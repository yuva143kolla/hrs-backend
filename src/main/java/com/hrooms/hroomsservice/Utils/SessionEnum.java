package com.hrooms.hroomsservice.Utils;

public enum SessionEnum {
	AM(1),
    Noon(2), 
    Evening(3);

    public final int orderId;

    private SessionEnum(int orderId) {
        this.orderId = orderId;
    }
    
    public int getOrder()
    {
        return this.orderId;
    }

}
