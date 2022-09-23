package leilao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;

class HelloWorldMockito {

	@Test
	void helloTest() {
		LeilaoDao leilaoMock = Mockito.mock(LeilaoDao.class);
		List<Leilao> leiloes = leilaoMock.buscarTodos();
		assertTrue(leiloes.isEmpty());
	}

}
