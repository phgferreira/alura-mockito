package br.com.alura.leilao.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

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
	
	@Captor
	private ArgumentCaptor<Pagamento> captorPagamento;
	
	private GeradorDePagamento service;
	
	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);
		service = new GeradorDePagamento(pagamentoDao);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance vencedor = leilao.getLanceVencedor();
		
		service.gerarPagamento(vencedor);
		
		// O captor nos permite capturar o paramento Pagamento que foi passado como parâmetro
		Mockito.verify(pagamentoDao).salvar(captorPagamento.capture());
		Pagamento pagamento = captorPagamento.getValue();
		
		assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
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
