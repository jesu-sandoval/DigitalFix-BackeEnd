package com.digitalfix.workorders.repository;

import com.digitalfix.workorders.model.WorkOrder;
import com.digitalfix.workorders.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByClienteId(String clienteId);
    List<WorkOrder> findByEstado(WorkOrderStatus estado);
}