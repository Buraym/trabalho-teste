package com.haroldo.minhasfinancas;

import com.haroldo.minhasfinancas.model.entity.Usuario;
import com.haroldo.minhasfinancas.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void init() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Criando um usuario e vendo se foi cadastrado mesmo no banco")
    public void SavingUsuario() {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        usuarioRepository.save(usuario);
        assertEquals(usuarioRepository.findByEmail(usuario.getEmail()).get().getEmail(), usuario.getEmail());
    }

    @Test
    @DisplayName("Criando um usuario, atualizando ele e vendo se foi atualizado mesmo no banco")
    public void SavingAndUpdatingUsuario() {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        String oldEmail = usuario.getEmail();
        usuarioRepository.save(usuario);
        assertEquals(usuarioRepository.findByEmail(usuario.getEmail()).get().getEmail(), oldEmail);
        usuario.setEmail("BrayanWilis@outlook.com");
        String newEmail = usuario.getEmail();
        usuarioRepository.save(usuario);
        assertNotEquals(usuarioRepository.findByEmail(usuario.getEmail()).get().getEmail(), oldEmail);
        assertEquals(usuarioRepository.findByEmail(usuario.getEmail()).get().getEmail(), newEmail);
    }

    @Test
    @DisplayName("Criando um usuario, deletando e vendo se foi removido do banco")
    public void SavingAndDeletingUsuario() {
        Usuario usuario = new Usuario(1L, "Brayan Wilis", "brayanwilis123@gmail.com", "senha123");
        usuarioRepository.save(usuario);
        assertEquals(usuarioRepository.findByEmail(usuario.getEmail()).get().getEmail(), usuario.getEmail());
        usuarioRepository.deleteById(usuarioRepository.findByEmail(usuario.getEmail()).get().getId());
        assertTrue(usuarioRepository.findByEmail(usuario.getEmail()).isEmpty());
    }
}
