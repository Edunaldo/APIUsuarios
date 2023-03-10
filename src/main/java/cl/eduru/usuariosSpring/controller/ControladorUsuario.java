package cl.eduru.usuariosSpring.controller;

import cl.eduru.usuariosSpring.entity.Usuario;
import cl.eduru.usuariosSpring.exceptions.EmailException;
import cl.eduru.usuariosSpring.exceptions.PassException;
import cl.eduru.usuariosSpring.services.ServicioUsuario;
import cl.eduru.usuariosSpring.services.TokenService;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class ControladorUsuario {

    private final ServicioUsuario sUsuario;
    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity token(Authentication authentication) {
        String token = tokenService.generateToken(authentication);

        Map<String, Object> tokenRes = new HashMap<>();
        tokenRes.put("token", token);

        return new ResponseEntity<>(tokenRes,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity crearUsuario(@RequestBody Usuario u) throws EmailException, PassException {
        return new ResponseEntity(sUsuario.guardarUsuario(u), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity listaUsuarios(){
        return new ResponseEntity(sUsuario.obtenerUsuarios(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity actualizarUsuario(@PathVariable Long id,@RequestBody Usuario u) throws EmailException, PassException {
        return new ResponseEntity(sUsuario.modificarUsuario(id,u), HttpStatus.OK);
    }

    @PutMapping("/{id}/camEstado")
    public ResponseEntity cambioEstado(@PathVariable Long id){
        return new ResponseEntity(sUsuario.usuActivo(id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity borraUsuario(@PathVariable Long id){
        boolean res = sUsuario.eliminarUsuario(id);
        if(res == true){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
