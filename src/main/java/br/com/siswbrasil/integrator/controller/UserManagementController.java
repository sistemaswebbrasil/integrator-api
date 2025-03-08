package br.com.siswbrasil.integrator.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;

import br.com.siswbrasil.integrator.service.Auth0UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints para gerenciar usuários no Auth0")
@Slf4j
public class UserManagementController {

    private final Auth0UserService userService;

    @Autowired
    public UserManagementController(Auth0UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários do Auth0")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários")
    })
    public ResponseEntity<List<User>> getAllUsers(
            @Parameter(description = "Número da página (zero-based)") 
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página") 
            @RequestParam(name = "perPage", defaultValue = "10") int perPage) {
        try {
            return ResponseEntity.ok(userService.getAllUsers(page, perPage));
        } catch (Auth0Exception e) {
            log.error("Erro ao buscar todos os usuários", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico do Auth0 pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID do usuário no Auth0") 
            @PathVariable String id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (APIException e) {
            if (e.getStatusCode() == 404) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao buscar usuário por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Auth0Exception e) {
            log.error("Erro ao buscar usuário por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no Auth0")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de usuário inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro ao criar usuário")
    })
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> userData) {
        try {
            // Validar campos obrigatórios
            if (userData.get("email") == null || userData.get("password") == null) {
                return ResponseEntity.badRequest().build();
            }
            
            User user = userService.createUser(
                userData.get("email"),
                userData.get("password"),
                userData.get("connection") != null ? userData.get("connection") : "Username-Password-Authentication"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (APIException e) {
            log.error("Erro ao criar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(e.getStatusCode() >= 400 && e.getStatusCode() < 500 ? 
                    e.getStatusCode() : HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Auth0Exception e) {
            log.error("Erro ao criar usuário", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza informações de um usuário existente no Auth0")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário")
    })
    public ResponseEntity<?> updateUser(
            @Parameter(description = "ID do usuário no Auth0") 
            @PathVariable String id, 
            @RequestBody Map<String, Object> userData) {
        try {
            userService.updateUser(id, userData);
            return ResponseEntity.ok().build();
        } catch (APIException e) {
            if (e.getStatusCode() == 404) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao atualizar usuário: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Auth0Exception e) {
            log.error("Erro ao atualizar usuário: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário do Auth0")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro ao excluir usuário")
    })
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID do usuário no Auth0") 
            @PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (APIException e) {
            if (e.getStatusCode() == 404) {
                return ResponseEntity.notFound().build();
            }
            log.error("Erro ao excluir usuário: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Auth0Exception e) {
            log.error("Erro ao excluir usuário: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}