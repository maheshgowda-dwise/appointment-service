package com.lifetrenz.lths.appointment.common.app.exception;

/**
 * Exception thrown when attempting to book a slot that is already booked
 * 
 * @author System Generated
 */
public class SlotAlreadyBookedException extends ApplicationException {
    
    private static final long serialVersionUID = 1L;
    
    private final Long customerId;
    private final String slotId;
    private final Long startDate;
    
    /**
     * Constructs a new SlotAlreadyBookedException with the specified detail message
     * 
     * @param message the detail message
     */
    public SlotAlreadyBookedException(String message) {
        super(409, message); // HTTP 409 Conflict
        this.customerId = null;
        this.slotId = null;
        this.startDate = null;
    }
    
    /**
     * Constructs a new SlotAlreadyBookedException with detailed slot information
     * 
     * @param message the detail message
     * @param customerId the customer ID attempting to book
     * @param slotId the slot ID that is already booked
     * @param startDate the start date of the slot
     */
    public SlotAlreadyBookedException(String message, Long customerId, String slotId, Long startDate) {
        super(409, message); // HTTP 409 Conflict
        this.customerId = customerId;
        this.slotId = slotId;
        this.startDate = startDate;
    }
    
    /**
     * Gets the customer ID that attempted to book the slot
     * 
     * @return the customer ID, or null if not specified
     */
    public Long getCustomerId() {
        return customerId;
    }
    
    /**
     * Gets the slot ID that was already booked
     * 
     * @return the slot ID, or null if not specified
     */
    public String getSlotId() {
        return slotId;
    }
    
    /**
     * Gets the start date of the slot
     * 
     * @return the start date in epoch format, or null if not specified
     */
    public Long getStartDate() {
        return startDate;
    }
    
    /**
     * Returns a detailed string representation of this exception
     * 
     * @return a string representation including slot details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (customerId != null || slotId != null || startDate != null) {
            sb.append(" [SlotDetails: customerId=").append(customerId)
              .append(", slotId=").append(slotId)
              .append(", startDate=").append(startDate)
              .append("]");
        }
        return sb.toString();
    }
}