package com.haroldo.minhasfinancas;

import com.google.gson.Gson;
import com.haroldo.minhasfinancas.api.dto.UsuarioDTO;
import com.haroldo.minhasfinancas.api.dto.UsuarioLoginDTO;
import com.haroldo.minhasfinancas.exception.RegraNegocioException;
import com.haroldo.minhasfinancas.model.entity.Usuario;
import com.haroldo.minhasfinancas.model.repository.UsuarioRepository;
import com.haroldo.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UsuarioServiceImpl usuarioServiceImpl;

    @MockBean
    UsuarioRepository usuarioRepository;

    @BeforeEach
    void init() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void ShouldSaveUsuarioService() throws Exception {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        Usuario registeredUsuario = usuarioServiceImpl.salvarUsuario(usuario);
        assertEquals(registeredUsuario.getEmail(), usuario.getEmail());
    }

    @Test
    public void ShouldSaveUsuarioResource() throws Exception {
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Brayan Wilis").email("brayanwilis123@gmail.com").senha("senha123")))).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void ShouldNotSaveUsuarioWithSameEmailAlreadyRegisteredService() throws Exception {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        usuarioServiceImpl.salvarUsuario(usuario);
        Usuario usuarioWithSameEmail = new Usuario(2L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        RegraNegocioException thrown = assertThrows(RegraNegocioException.class, () -> {
            usuarioServiceImpl.validarEmail(usuario.getEmail());
        });
        assertEquals("Já existe um usuário cadastrado com este email.", thrown.getMessage());
    }

    @Test
    public void ShouldNotSaveUsuarioWithSameEmailAlreadyRegisteredResource() throws Exception {
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Brayan Wilis").email("brayanwilis123@gmail.com").senha("senha123")))).andExpect(MockMvcResultMatchers.status().isCreated());
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Pedrinho").email("brayanwilis123@gmail.com").senha("pedrinho123")))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void ShouldNotSaveUsuarioAndThrowErrorWhenNotFindingEmailRegisteredService() throws Exception {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        usuarioServiceImpl.salvarUsuario(usuario);
        RegraNegocioException thrown = assertThrows(RegraNegocioException.class, () -> {
            usuarioServiceImpl.autenticar("pedrinho@gmail.com", "senha123");
        });
        assertEquals("Usuário não encontrado para o email informado.", thrown.getMessage());
    }

    @Test
    public void ShouldNotSaveUsuarioAndThrowErrorWhenNotFindingEmailRegisteredResource() throws Exception {
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Brayan Wilis").email("brayanwilis123@gmail.com").senha("senha123")))).andExpect(MockMvcResultMatchers.status().isCreated());
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Pedrinho").email("pedrinho@gmail.com").senha("senha123")))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void ShouldSaveUsuarioAndThrowErrorWhenPasswordDoesNotAuthenticateService() throws Exception {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        usuarioServiceImpl.salvarUsuario(usuario);
        RegraNegocioException thrown = assertThrows(RegraNegocioException.class, () -> {
            usuarioServiceImpl.autenticar(usuario.getEmail(), "senha");
        });
        assertEquals("Senha inválida.", thrown.getMessage());
    }

    @Test
    public void ShouldSaveUsuarioAndThrowErrorWhenPasswordDoesNotAuthenticateResource() throws Exception {
        this.mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioDTO.builder().nome("Brayan Wilis").email("brayanwilis123@gmail.com").senha("senha123")))).andExpect(MockMvcResultMatchers.status().isCreated());
        this.mockMvc.perform(post("/api/usuarios/autenticar").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(UsuarioLoginDTO.builder().email("brayanwilis123@gmail.com").senha("senha")))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
