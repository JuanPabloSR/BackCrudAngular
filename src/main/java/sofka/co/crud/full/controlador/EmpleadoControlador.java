package sofka.co.crud.full.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sofka.co.crud.full.excepciones.ResourceNotFoundException;
import sofka.co.crud.full.modelo.Empleado;
import sofka.co.crud.full.repositorio.EmpleadoRepositorio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200")

public class EmpleadoControlador {

    @Autowired
    private EmpleadoRepositorio repositorio;

    // Listar empleados
    @GetMapping("/empleados")
    public List<Empleado> listarEmpleados() {
        return repositorio.findAll();
    }

    //agregar
    @PostMapping("/empleados")
    public Empleado guardarEmpleado(@RequestBody Empleado empleado) {
        try {
            return repositorio.save(empleado);
        }
        catch (Exception error){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"BadRequest 400");
        }

    }

    // buscar un empleado
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id){
        Empleado empleado = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));
        return ResponseEntity.ok(empleado);
    }

    // actualizar empleado
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id,@RequestBody Empleado detallesEmpleado){
        Empleado empleado = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));

        empleado.setNombre(detallesEmpleado.getNombre());
        empleado.setApellido(detallesEmpleado.getApellido());
        empleado.setEmail(detallesEmpleado.getEmail());

        Empleado empleadoActualizado = repositorio.save(empleado);
        return ResponseEntity.ok(empleadoActualizado);
    }

    //eliminar un empleado
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String,Boolean>> eliminarEmpleado(@PathVariable Long id){
        Empleado empleado = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el empleado con el ID : " + id));

        repositorio.delete(empleado);
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminar",Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}

   
