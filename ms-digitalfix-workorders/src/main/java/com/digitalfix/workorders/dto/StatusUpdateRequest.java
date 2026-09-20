package com.digitalfix.workorders.dto;

import com.digitalfix.workorders.model.WorkOrderStatus;
import lombok.Data;

@Data
public class StatusUpdateRequest { 
    private WorkOrderStatus status; 
    private String tecnico;
}