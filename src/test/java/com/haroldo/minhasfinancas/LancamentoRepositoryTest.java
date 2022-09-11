package com.haroldo.minhasfinancas;
import com.haroldo.minhasfinancas.model.entity.Lancamento;
import com.haroldo.minhasfinancas.model.enums.StatusLancamento;
import com.haroldo.minhasfinancas.model.enums.TipoLancamento;
import com.haroldo.minhasfinancas.model.repository.LancamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @BeforeEach
    void init() {
        lancamentoRepository.deleteAll();
    }

    @Test
    @DisplayName("Criando um lancamento e vendo se foi cadastrado mesmo no banco")
    public void SavingLancamento() {
        Lancamento lancamento = new Lancamento(1L, "Coxinha de frango recheado com catupiry", 9, 2022, new BigDecimal(12.50), LocalDate.of(2022, 9, 10), null, TipoLancamento.DESPESA, StatusLancamento.PENDENTE);
        Lancamento lancamentoRegistrado = lancamentoRepository.save(lancamento);
        assertEquals(lancamentoRepository.findById(lancamentoRegistrado.getId()).get().getDescricao(),lancamento.getDescricao()  );
    }

    @Test
    @DisplayName("Criando um lancamento, atualizando ele e vendo se foi atualizado mesmo no banco")
    public void SavingAndUpdatingLancamento() {
        Lancamento lancamento = new Lancamento(1L, "Coxinha de frango recheado com catupiry", 9, 2022, new BigDecimal(12.50), LocalDate.of(2022, 9, 10), null, TipoLancamento.DESPESA, StatusLancamento.PENDENTE);
        Lancamento lancamentoRegistrado = lancamentoRepository.save(lancamento);
        StatusLancamento oldStatus = lancamentoRegistrado.getStatus();
        LocalDate oldDataCadastro = lancamentoRegistrado.getDataCadastro();
        String oldDescricao = lancamentoRegistrado.getDescricao();
        TipoLancamento oldTipo = lancamentoRegistrado.getTipo();
        lancamento.setStatus(StatusLancamento.EFETIVADO);
        lancamento.setDataCadastro(LocalDate.of(2022, 9, 11));
        lancamento.setTipo(TipoLancamento.RECEITA);
        lancamento.setDescricao("Vi uma pedrinha amarelinha esses dias, era ouro, peguei do bueiro, mas isso é detalhe");
        Lancamento lancamentoRegistradoAtualizado = lancamentoRepository.save(lancamento);
        assertNotEquals(lancamentoRepository.findById(lancamentoRegistradoAtualizado.getId()).get().getStatus(), oldStatus);
        assertNotEquals(lancamentoRepository.findById(lancamentoRegistradoAtualizado.getId()).get().getDataCadastro(), oldDataCadastro);
        assertNotEquals(lancamentoRepository.findById(lancamentoRegistradoAtualizado.getId()).get().getTipo(), oldDescricao);
        assertNotEquals(lancamentoRepository.findById(lancamentoRegistradoAtualizado.getId()).get().getDescricao(), oldTipo);
    }

    @Test
    @DisplayName("Criando um lancamento, deletando e vendo se foi removido do banco")
    public void SavingAndDeletingLancamento() {
        Lancamento lancamento = new Lancamento(1L, "Coxinha de frango recheado com catupiry", 9, 2022, new BigDecimal(12.50), LocalDate.of(2022, 9, 10), null, TipoLancamento.DESPESA, StatusLancamento.PENDENTE);
        Lancamento lancamentoRegistrado = lancamentoRepository.save(lancamento);
        assertEquals(lancamentoRepository.findById(lancamentoRegistrado.getId()).get().getDescricao(), lancamento.getDescricao());
        lancamentoRepository.deleteById(lancamentoRegistrado.getId());
        assertTrue(lancamentoRepository.findById(lancamentoRegistrado.getId()).isEmpty());
    }

}
