package br.com.alura.leilao.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	@Mock
	private PagamentoDao pagamentoDao;

	@Mock
	private Clock clock;
	
	@Captor
	private ArgumentCaptor<Pagamento> captorPagamento;
	
	private GeradorDePagamento service;
	
	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);
		service = new GeradorDePagamento(pagamentoDao, clock);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();

		// Simula uma data fictícia simples
		LocalDate data = LocalDate.of(2022, 9, 12); // SEGUNDA-FEIRA
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		// Realiza o procedimento de teste
		service.gerarPagamento(vencedor);
		
		// O captor nos permite capturar o paramento Pagamento que foi passado como parâmetro
		Mockito.verify(pagamentoDao).salvar(captorPagamento.capture());
		Pagamento pagamento = captorPagamento.getValue();
		
		// Valida o teste
		assertEquals(data.plusDays(1), pagamento.getVencimento());
		assertEquals(vencedor.getValor(), pagamento.getValor());
		assertFalse(pagamento.getPago());
		assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
		assertEquals(leilao, pagamento.getLeilao());
	}
		
	private Leilao leilao() {
        Leilao leilao = new Leilao("Celular",
                        new BigDecimal("500"),
                        new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Ciclano"),
                        new BigDecimal("900"));

        leilao.propoe(lance);
        
        leilao.setLanceVencedor(lance);

        return leilao;

    }
}
