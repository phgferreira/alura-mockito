package br.com.alura.leilao.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {

	private FinalizarLeilaoService service;
	
	@Mock
	private LeilaoDao leilaoDao;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmails;
	
	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
	}
	
	@Test
	void deveriaFinalizarUmLeilao() {
		List<Leilao> leiloes = leiloes();
        // Mockito, quando o método buscarLeiloesExpirados for chamado, devolva essa lista
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        	.thenReturn(leiloes);

        service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		assertTrue(leilao.isFechado());
		assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
		Mockito.verify(leilaoDao).salvar(leilao);
	}

	@Test
	void deveriaEnviarEmailParaVencedorDoLeilao() {
		List<Leilao> leiloes = leiloes();
        // Mockito, quando o método buscarLeiloesExpirados for chamado, devolva essa lista
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        	.thenReturn(leiloes);

        service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		Lance lance = leilao.getLanceVencedor();
		
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lance);

	}

	@Test
	void naoDeveriaEnviarEmailParaVencedorDoLeilaoEmCasoDeErroAoEncerrerrarOLeilao() {
		List<Leilao> leiloes = leiloes();

		// Mockito, quando o método buscarLeiloesExpirados for chamado, devolva essa lista
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
        	.thenReturn(leiloes);

        // Mockito, quando o método salvar do leilaoDao for invocado, lance uma RuntimeException
        Mockito.when(leilaoDao.salvar(Mockito.any()))
	    	.thenThrow(RuntimeException.class);

        // Esse try...catch é para o RuntimeException não interromper o teste
        try {
	        service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(enviadorDeEmails);
        } catch (Exception e) {}
	}
	
	private List<Leilao> leiloes() {
        List<Leilao> lista = new ArrayList<>();

        Leilao leilao = new Leilao("Celular",
                        new BigDecimal("500"),
                        new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"),
                        new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Ciclano"),
                        new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);

        return lista;

    }

}
