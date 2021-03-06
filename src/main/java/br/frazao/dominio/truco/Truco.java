package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
		Map<Set<JogadorTruco>, Integer> result = new LinkedHashMap<>();
		for (Mao mao : getMaoList()) {
			
		}
		return result;
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
		return this.baralhoVazio;
	}

	public boolean isUsarCoringa() {
		return this.usarCoringa;
	}

	public boolean isViraCarta() {
		return this.viraCarta;
	}

	@Override
	public Resultado jogar(Mesa mesa) {
		setMesa(mesa);

		JogadorTruco jogadorMao = (JogadorTruco) getMesa().getJogador(0).get();
		do {
			getMaoList().add(Mao.criar(jogadorMao));
			getMao().get().jogar(this);
			jogadorMao = (JogadorTruco) getMesa().getJogadorDepois(jogadorMao).get();
		} while (getVencedor() == null);

		System.out.format("\n\n\ntime vencedor [%s]", getVencedor());
		return new ResultadoTruco(null, null, null, null);
	}

	private void setMesa(Mesa mesa) {
		if (Objects.requireNonNull(mesa).getJogadorList().isEmpty()) {
			throw new IllegalStateException();
		}
		this.mesa = mesa;
	}

}