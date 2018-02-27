package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

	public static final int TOTAL_CARTAS_DISTRIBUIR_MAO = 3;

	public static final int TOTAL_TENTOS_PARTIDA = 12;

	private Baralho baralho;

	private boolean baralhoVazio = false;

	private List<Mao> maoList;

	private Mesa mesa;

	private Baralho monte;

	private boolean usarCoringa = false;

	private boolean cartaVirada = false;
	
	public Truco(boolean cartaVirada, boolean baralhoVazio) {
		this(cartaVirada, baralhoVazio, false);
	}

	public Truco(boolean cartaVirada, boolean baralhoVazio, boolean usarCoringa) {
		super();
		this.cartaVirada = cartaVirada;
		this.baralhoVazio = baralhoVazio;
		this.usarCoringa = cartaVirada ? false : usarCoringa;
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
				if (isCartaVirada()) {
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

	Optional<Mao> getMao() {
		return getMao(getMaoList().size() - 1);
	}

	Optional<Mao> getMao(Integer posicao) {
		if (getMaoList().isEmpty() || posicao < 0 || posicao >= getMaoList().size()) {
			return Optional.empty();
		}
		return Optional.ofNullable(getMaoList().get(posicao));
	}

	List<Mao> getMaoList() {
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
		Map<Set<JogadorTruco>, Integer> result = new TreeMap<>((t1, t2) -> t1.containsAll(t2) ? 0 : 1);
		getMesa().getJogadorList().stream().forEach(j -> result.put(((JogadorTruco) j).getTime(), 0));
		return result;
	}

	Integer getTentos(JogadorTruco jogador) {
		return null;
	}

	Map<Set<JogadorTruco>, Integer> getTentos(Mao mao) {
		return null;
	}

	Integer getTentos(Set<JogadorTruco> jogador) {
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

	public boolean isCartaVirada() {
		return cartaVirada;
	}

	@Override
	public Resultado jogar(Mesa mesa) {
		setMesa(mesa);

		JogadorTruco jogadorMao = (JogadorTruco) getMesa().getJogador(0).get();
		do {
			getMaoList().add(new Mao(jogadorMao));
			getMao().get().jogar(this);
			jogadorMao = (JogadorTruco) getMesa().getJogadorDepois(jogadorMao).get();
		} while (getVencedor() == null);

		return new ResultadoTruco(getMaoList());
	}

	private void setMesa(Mesa mesa) {
		if (Objects.requireNonNull(mesa).getJogadorList().isEmpty()) {
			throw new IllegalStateException();
		}
		this.mesa = mesa;
	}

}