package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogador;

public class Mao {

	private static final List<Naipe> CARTA_VIRADA_ORDEM_DECRESCENTE_NAIPE = Arrays.asList(Naipe.PAUS, Naipe.COPAS, Naipe.ESPADAS, Naipe.OUROS);

	private static final List<Numero> CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO = Arrays.asList(Numero.TRES, Numero.DOIS, Numero.AS, Numero.REIS, Numero.VALETE, Numero.DAMA, Numero.SETE, Numero.SEIS, Numero.CINCO, Numero.QUATRO);

	private Map<SortedSet<Carta>, Integer> cartaPesoMap;

	private Carta cartaVirada;

	private Map<Integer, List<JogadaTruco>> jogadaMap;

	private Jogador jogadorMao;

	private List<Carta> manilhaList;

	private Truco truco;

	public Mao(JogadorTruco jogadorMao) {
		setJogadorMao(jogadorMao);
	}

	public Integer getCartaPeso(Carta carta) {
		return getCartaPesoMap().entrySet().stream().filter(s -> s.getKey().contains(carta)).findAny().get().getValue();
	}

	public List<Integer> getCartaPeso(List<Carta> cartas) {
		return cartas.stream().map(this::getCartaPeso).collect(Collectors.toList());
	}

	public Optional<Integer> getCartaPesoMaior(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).max((p1, p2) -> Integer.compare(p1, p2));
	}

	Map<SortedSet<Carta>, Integer> getCartaPesoMap() {
		if (this.cartaPesoMap == null) {
			this.cartaPesoMap = new TreeMap<>();
		}
		return this.cartaPesoMap;
	}

	public Optional<Integer> getCartaPesoMenor(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).min((p1, p2) -> Integer.compare(p1, p2));
	}

	public Optional<Carta> getCartaVirada() {
		return Optional.ofNullable(this.cartaVirada);
	}

	private List<JogadaTruco> getJogadaMap() {
		return getJogadaMap(jogadaMap.keySet().stream().max((k1, k2) -> k1.compareTo(k2)));
	}

	private List<JogadaTruco> getJogadaMap(Optional<Integer> jogada) {
		return jogadaMap.get(jogada.get());
	}

	public Jogador getJogadorMao() {
		return this.jogadorMao;
	}

	List<Carta> getManilhaList() {
		if (this.manilhaList == null) {
			this.manilhaList = new ArrayList<>();
		}
		return this.manilhaList;
	}

	private Numero getNumeroSuperior(Numero numero) {
		Integer posicao = CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO.indexOf(numero);
		posicao = posicao == 0 ? CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO.size() - 1 : posicao - 1;
		return CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO.get(posicao);
	}

	Truco getTruco() {
		return truco;
	}

	public Boolean isCangou() {
		return null; // isCangou(this.jogadaList.size() - 1);
	}

	public Boolean isCangou(int numeroJogada) {
		return null; // this.jogadaList.get(this.jogadaList.size() - 1) != null;
	}

	public boolean isManilha(Carta carta) {
		return getManilhaList().contains(carta);
	}

	public Mao jogar(Truco truco) {
		setTruco(truco);

		// logar("ANTES");

		// embaralhar
		getJogadorMao().embaralhar(getTruco().getBaralho());

		// cortar o baralho
		int corte = getTruco().getMesa().getJogadorAntes(getJogadorMao()).get().cortar(getTruco().getBaralho().getCartas().size(), getTruco().getMesa().getJogadorList().size() * Truco.TOTAL_CARTAS_DISTRIBUIR_MAO + (getTruco().isViraCarta() ? 1 : 0));
		System.out.format("\ncorte %d\n", corte);

		// verificar se é necessário virar carta()
		Carta cartaVirada = null;
		if (getTruco().isViraCarta()) {
			cartaVirada = getTruco().getBaralho().descarta(getTruco().getBaralho().getCartas(corte < 0 ? -1 : 1)).get().get(0);
		}
		setCartaVirada(cartaVirada);

		truco.getMonte().encarta(truco.getBaralho().descarta(truco.getBaralho().getCartas(corte >= 0 ? corte - truco.getBaralho().getCartas().size() : truco.getBaralho().getCartas().size() + corte)).get());

		// distribuir
		int distribui = corte < 0 ? -Truco.TOTAL_CARTAS_DISTRIBUIR_MAO : Truco.TOTAL_CARTAS_DISTRIBUIR_MAO;
		getTruco().getMesa().getJogadorList().stream().forEach(j -> j.getBaralho().encarta(getTruco().getBaralho().descarta(getTruco().getBaralho().getCartas(distribui)).get()));
		getTruco().getMonte().encarta(getTruco().getBaralho().descarta(getTruco().getBaralho().getCartas()).get());

		// logar("DEPOIS");

		// captar jogadas
		try {
			Jogador jogador = null;
			for (int c = 0; c < Truco.TOTAL_CARTAS_DISTRIBUIR_MAO; c++) {
				if (jogadaMap == null) {
					jogadaMap = new TreeMap<>();
				}
				jogadaMap.put(c, new ArrayList<>());
				jogador = jogador == null ? getJogadorMao() : vencedorJogada(c - 1, jogador);
				Jogador primeiro = jogador;
				do {
					JogadaTruco jogada = (JogadaTruco) jogador.jogar(getTruco());
					getJogadaMap().add(jogada);
					getTruco().getMonte().encarta(jogada.getCarta());
				} while (primeiro != (jogador = getTruco().getMesa().getJogadorDepois(jogador).get()));
			}
		} catch (RuntimeException e) {

		}

		// recolher todas as cartas
		// pega dos jogadores
		getTruco().getMesa().getJogadorList().forEach(jogador -> getTruco().getBaralho().encarta(jogador.getBaralho().descarta(jogador.getBaralho().getCartas()).orElse(null)));
		// pega do monte
		getTruco().getBaralho().encarta(getTruco().getMonte().descarta(getTruco().getMonte().getCartas()).get());
		// pega da carta virada
		getTruco().getMao().ifPresent(m -> m.getCartaVirada().ifPresent(cv -> getTruco().getBaralho().encarta(cv)));
		// ordenar
		getJogadorMao().ordenar(getTruco().getBaralho());

		return this;
	}

	private Jogador vencedorJogada(int numeroJogada, Jogador jogador) {
		Optional<Carta> carta = getJogadaMap(Optional.of(numeroJogada)).stream().map(JogadaTruco::getCarta).max((c1, c2) -> getCartaPeso(c1.get()).compareTo(getCartaPeso(c2.get()))).get();
		List<JogadaTruco> jogada = getJogadaMap(Optional.of(numeroJogada)).stream().filter(jd -> jd.getCarta().equals(carta)).collect(Collectors.toList());
		return jogada.get(0).getJogador();
	}

	private void logar(String mensagem) {
		System.out.format("Jogadores %s da mao %d\n", mensagem, getTruco().getMaoList().size() + 1);
		getTruco().getMesa().getJogadorList().forEach(j -> System.out.format("[%s total cartas %d] ", j.getNome(), j.getBaralho().getCartas().size()));
		System.out.format("\nMonte %d", getTruco().getMonte().getCartas().size());
		System.out.format("\nCarta Virada %s", getCartaVirada());
		System.out.format("\nBaralho => %d", getTruco().getBaralho().getCartas().size());
	}

	void setTruco(Truco truco) {
		this.truco = truco;
	}

	void setJogadaMap(Map<Integer, List<JogadaTruco>> jogadaMap) {
		this.jogadaMap = Objects.requireNonNull(jogadaMap);
	}

	void setManilhaList(List<Carta> manilhaList) {
		this.manilhaList = Objects.requireNonNull(manilhaList);
	}

	public void setCartaVirada(Carta cartaVirada) {
		if (this.cartaVirada != cartaVirada) {
			this.cartaVirada = cartaVirada;

			AtomicInteger peso = new AtomicInteger(0);
			if (getCartaVirada().isPresent()) {
				// definir as manilhas
				Numero numeroSuperior = getNumeroSuperior(this.getCartaVirada().get().getNumero().get());
				setManilhaList(CARTA_VIRADA_ORDEM_DECRESCENTE_NAIPE.stream().map(naipe -> getTruco().getBaralho().getCartas(numeroSuperior, naipe).get(0)).collect(Collectors.toList()));
				// definir o peso das manilhas
				setCartaPesoMap(getManilhaList().stream().collect(Collectors.toMap(c -> new TreeSet<>((List<Carta>) Arrays.asList(c)), c -> peso.decrementAndGet())));
				// definir o peso das demais cartas
				getCartaPesoMap().putAll(CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO.stream().filter(n -> !n.equals(numeroSuperior)).map(numero -> new TreeSet<>(getTruco().getBaralho().getCartas(numero))).collect(Collectors.toMap(k -> k, k -> peso.decrementAndGet())));
			} else {
				// definir manilhas
				setManilhaList(new ArrayList<>());
				getManilhaList().addAll(getTruco().getBaralho().getCartas(Numero.QUATRO, Naipe.PAUS));
				getManilhaList().addAll(getTruco().getBaralho().getCartas(Numero.SETE, Naipe.COPAS));
				getManilhaList().addAll(getTruco().getBaralho().getCartas(Numero.AS, Naipe.ESPADAS));
				getManilhaList().addAll(getTruco().getBaralho().getCartas(Numero.SETE, Naipe.OUROS));
				if (getTruco().isUsarCoringa()) {
					getManilhaList().addAll(getTruco().getBaralho().getCartas(null, null));
				}
				// definir o peso das manilhas
				setCartaPesoMap(getManilhaList().stream().collect(Collectors.toMap(c -> new TreeSet<>((List<Carta>) Arrays.asList(c)), c -> peso.decrementAndGet())));
				// definir o peso das demais cartas
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.TRES)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.DOIS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.AS, Naipe.PAUS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.AS, Naipe.COPAS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.AS, Naipe.OUROS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.REIS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.VALETE)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.DAMA)), peso.decrementAndGet());
				if (!getTruco().isBaralhoVazio()) {
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.SETE, Naipe.PAUS)), peso.decrementAndGet());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.SETE, Naipe.ESPADAS)), peso.get());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.SEIS)), peso.decrementAndGet());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.CINCO)), peso.decrementAndGet());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.QUATRO, Naipe.COPAS)), peso.decrementAndGet());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.QUATRO, Naipe.ESPADAS)), peso.get());
					getCartaPesoMap().put(new TreeSet<>(getTruco().getBaralho().getCartas(Numero.QUATRO, Naipe.OUROS)), peso.get());
				}
			}
		}
	}

	void setCartaPesoMap(Map<SortedSet<Carta>, Integer> cartaPesoMap) {
		this.cartaPesoMap = cartaPesoMap;
	}

	private void setJogadorMao(Jogador jogadorMao) {
		this.jogadorMao = jogadorMao;
	}

}
