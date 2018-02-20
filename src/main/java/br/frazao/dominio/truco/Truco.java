package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Fundo;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogador;
import br.frazao.dominio.jogo.Jogo;
import br.frazao.dominio.jogo.Mesa;
import br.frazao.dominio.jogo.Resultado;

public class Truco implements Jogo {

	private Baralho baralho;

	private Baralho monte;

	private boolean baralhoVazio = false;

	private List<Mao> maoList;

	private boolean usarCoringa = false;

	private boolean viraCarta = false;

	public Truco(boolean viraCarta, boolean baralhoVazio) {
		this(viraCarta, baralhoVazio, false);
	}

	public Truco(boolean viraCarta, boolean baralhoVazio, boolean usarCoringa) {
		super();
		this.viraCarta = viraCarta;
		this.baralhoVazio = baralhoVazio;
		this.usarCoringa = viraCarta ? false : usarCoringa;
	}

	Baralho getBaralho() {
		if (baralho == null) {
			baralho = Baralho.criar(Fundo.PRETO, usarCoringa);

			// descartar cartas
			baralho.descarta(baralho.getCartas(Numero.DEZ));
			baralho.descarta(baralho.getCartas(Numero.NOVE));
			baralho.descarta(baralho.getCartas(Numero.OITO));
			if (isBaralhoVazio()) {
				baralho.descarta(baralho.getCartas(Numero.SEIS));
				baralho.descarta(baralho.getCartas(Numero.CINCO));
				if (isViraCarta()) {
					baralho.descarta(baralho.getCartas(Numero.SETE));
					baralho.descarta(baralho.getCartas(Numero.QUATRO));
				} else {
					baralho.descarta(baralho.getCartas(Numero.SETE, Naipe.PAUS));
					baralho.descarta(baralho.getCartas(Numero.SETE, Naipe.ESPADAS));
					baralho.descarta(baralho.getCartas(Numero.QUATRO, Naipe.COPAS));
					baralho.descarta(baralho.getCartas(Numero.QUATRO, Naipe.ESPADAS));
					baralho.descarta(baralho.getCartas(Numero.QUATRO, Naipe.OUROS));
				}
			}
		}
		return baralho;
	}

	Baralho getMonte() {
		if (this.monte == null) {
			this.monte = Baralho.criar();
		}
		return this.monte;
	}

	/**
	 * retorna a mão atual
	 * 
	 * @return
	 */
	Mao getMao() {
		return getMaoList().get(getMaoList().size() - 1);
	}

	private List<Mao> getMaoList() {
		if (maoList == null) {
			maoList = new ArrayList<>();
		}
		return maoList;
	}

	public Map<List<Jogador>, Integer> getTentos() {
		return null;
	}

	Integer getTentos(Jogador jogador) {
		return null;
	}

	Integer getTentos(List<Jogador> jogador) {
		return null;
	}

	Map<List<Jogador>, Integer> getTentos(Mao mao) {
		return null;
	}

	public List<Jogador> getVencedor() {
		return null;
	}

	public boolean isBaralhoVazio() {
		return baralhoVazio;
	}

	public boolean isUsarCoringa() {
		return usarCoringa;
	}

	public boolean isViraCarta() {
		return viraCarta;
	}
	
	@Override
	public Resultado jogar(Mesa mesa) {
		do {
			// recolher todas as cartas
			mesa.getJogadorList().forEach(jogador -> getBaralho().encarta(jogador.getBaralho().descarta(jogador.getBaralho().getCartas()).get()));
			getBaralho().encarta(monte.descarta(monte.getCartas()).get());
			getMao().getCartaVirada().ifPresent(carta -> getBaralho().encarta(carta));
			
			// embaralhar
			getBaralho().embaralha();

			// cortar
			monte.encarta(getMao().getMao().cortar(getBaralho()));

			// distribuir
			mesa.getJogadorList().forEach(jogador -> getBaralho().encarta(jogador.getBaralho().getCartas()));

			// captar jogadas

			getMaoList().add(new Mao());

			getMao().jogar(this);
		} while (getVencedor() == null);

		return new ResultadoTruco(getMaoList());
	}

}
