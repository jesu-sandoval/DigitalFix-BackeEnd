package com.digitalfix.notify.Controller;

import com.digitalfix.notify.DTO.QueueRequestDto;
import com.digitalfix.notify.Service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/queues")
public class RabbitAdminController {

    @Autowired
    private RabbitAdminService rabbitAdminService;

    /**POST Crear una cola con validacion de entrada */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCola(@Valid @RequestBody QueueRequestDto dto) {
        boolean creada = rabbitAdminService.crearCola(dto.getNombre(), dto.isDurable());
        return new ResponseEntity<>(
                Map.of(
                        "mensaje", "Cola declarada exitosamente en RabbitMQ",
                        "cola", dto.getNombre(),
                        "durable", dto.isDurable()
                ),
                HttpStatus.CREATED
        );
    }

    /**DELETE Eliminar una cola existente */
    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, Object>> eliminarCola(@PathVariable("name") String name) {
        boolean eliminada = rabbitAdminService.eliminarCola(name);
        return ResponseEntity.ok(
                Map.of(
                        "mensaje", eliminada ? "Cola eliminada con exito" : "La cola no existia o ya fue eliminada",
                        "cola", name,
                        "eliminada", eliminada
                )
        );
    }

    /**GET Consultar estado y mensajes en la cola */
    @GetMapping("/{name}")
    public ResponseEntity<?> consultarCola(@PathVariable("name") String name) {
        Properties info = rabbitAdminService.obtenerInfoCola(name);
        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "La cola no fue encontrada en RabbitMQ", "cola", name));
        }
        return ResponseEntity.ok(
                Map.of(
                        "cola", name,
                        "mensajes", info.get("QUEUE_MESSAGE_COUNT"),
                        "consumidores", info.get("QUEUE_CONSUMER_COUNT")
                )
        );
    }
}
