package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Fundo;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogo;
import br.frazao.dominio.jogo.Mesa;
import br.frazao.dominio.jogo.Resultado;

public class Truco implements Jogo {

	private static final Integer TOTAL_TENTOS_PARTIDA = 13;

	private Baralho baralho;

	private boolean baralhoVazio = false;

	private List<Mao> maoList;

	private Mesa mesa;

	private Baralho monte;

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

	Mao getMao() {
		return getMao(getMaoList().size() - 1);
	}

	Mao getMao(Integer posicao) {
		return getMaoList().get(posicao);
	}

	private List<Mao> getMaoList() {
		if (maoList == null) {
			maoList = new ArrayList<>();
		}
		return maoList;
	}

	Mesa getMesa() {
		return mesa;
	}

	Baralho getMonte() {
		if (this.monte == null) {
			this.monte = Baralho.criar();
		}
		return this.monte;
	}

	public Map<Set<JogadorTruco>, Integer> getTentos() {
		Map<Set<JogadorTruco>, Integer> result = new TreeMap<>();
		getMesa().getJogadorList().stream().forEach(j -> result.put(((JogadorTruco) j).getTime(), 0));
		return result;
	}

	Integer getTentos(JogadorTruco jogador) {
		return null;
	}

	Integer getTentos(Set<JogadorTruco> jogador) {
		return null;
	}

	Map<Set<JogadorTruco>, Integer> getTentos(Mao mao) {
		return null;
	}

	public Set<JogadorTruco> getVencedor() {
		AtomicReference<Set<JogadorTruco>> vencedor = new AtomicReference<>();
		getTentos().forEach((k, v) -> {
			if (v.compareTo(TOTAL_TENTOS_PARTIDA) >= 0) {
				vencedor.set(k);
			}
		});
		return vencedor.get();
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
		this.mesa = Objects.requireNonNull(mesa);
		JogadorTruco jogadorMao = (JogadorTruco) getMesa().getJogador(0).get();
		do {
			getMaoList().add(new Mao(jogadorMao));
			getMao().jogar(this);
			jogadorMao = (JogadorTruco) getMesa().getJogadorDepois(jogadorMao).get();
		} while (getVencedor() == null);

		return new ResultadoTruco(getMaoList());
	}

}