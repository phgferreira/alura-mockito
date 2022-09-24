package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {

	@Mock
	private PagamentoDao pagamentoDao;
	
	private GeradorDePagamento service;
	
	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);
		service = new GeradorDePagamento(pagamentoDao);
	}
	
	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		
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
