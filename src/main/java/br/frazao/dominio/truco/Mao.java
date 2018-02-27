package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogador;

public class Mao {

	private static final List<Naipe> CARTA_VIRADA_ORDEM_DECRESCENTE_NAIPE = Arrays.asList(Naipe.PAUS, Naipe.COPAS, Naipe.ESPADAS, Naipe.OUROS);

	private static final List<Numero> CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO = Arrays.asList(Numero.TRES, Numero.DOIS, Numero.AS, Numero.REIS, Numero.VALETE, Numero.DAMA, Numero.SETE, Numero.SEIS, Numero.CINCO, Numero.QUATRO);

	private Map<SortedSet<Carta>, Integer> cartaPesoMap;

	private Carta cartaVirada = Carta.criar(null, null, null);

	private Map<Integer, List<JogadaTruco>> jogadaMap;

	private Jogador jogadorMao;

	private List<Carta> manilhaList;

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
		System.out.println(truco.getMaoList().size());
		
		// necessário para definir manilhas e pesos das cartas do baralho
		Baralho baralhoCopia = Baralho.criar(truco.getBaralho().getCartas());

		// embaralhar
		getJogadorMao().embaralhar(truco.getBaralho());

		// cortar o baralho
		int corte = truco.getMesa().getJogadorAntes(getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * Truco.TOTAL_CARTAS_DISTRIBUIR_MAO + (truco.isCartaVirada() ? 1 : 0));
		truco.getMonte().encarta(truco.getBaralho().descarta(truco.getBaralho().getCartas(corte >= 0 ? corte - truco.getBaralho().getCartas().size() : truco.getBaralho().getCartas().size() + corte)).orElse(null));

		// distribuir aos jogadores
		int distribui = corte < 0 ? -Truco.TOTAL_CARTAS_DISTRIBUIR_MAO : Truco.TOTAL_CARTAS_DISTRIBUIR_MAO;
		truco.getMesa().getJogadorList().stream().forEach(j -> j.getBaralho().encarta(truco.getBaralho().descarta(truco.getBaralho().getCartas(distribui)).get()));

		// verificar se é necessário virar carta()
		Carta cartaVirada = null;
		if (truco.isCartaVirada()) {
			cartaVirada = truco.getBaralho().descarta(truco.getBaralho().getCartas(corte < 0 ? -1 : 1)).get().get(0);
		}
		setCartaVirada(cartaVirada, baralhoCopia);

		// joga no monte o resto das cartas
		truco.getMonte().encarta(truco.getBaralho().descarta().orElse(null));

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
					JogadaTruco jogada = (JogadaTruco) jogador.jogar(truco);
					getJogadaMap().add(jogada);
					truco.getMonte().encarta(jogada.getCarta());
				} while (primeiro != (jogador = truco.getMesa().getJogadorDepois(jogador).get()));
			}
		} catch (RuntimeException e) {

		}

		// recolher todas as cartas
		// pega dos jogadores
		truco.getMesa().getJogadorList().forEach(jogador -> truco.getBaralho().encarta(jogador.getBaralho().descarta(jogador.getBaralho().getCartas()).orElse(null)));
		// pega do monte
		truco.getBaralho().encarta(truco.getMonte().descarta().get());
		// pega da carta virada
		truco.getMao().ifPresent(m -> m.getCartaVirada().ifPresent(cv -> truco.getBaralho().encarta(cv)));
		// ordenar
		getJogadorMao().ordenar(truco.getBaralho());

		return this;
	}

	private Jogador vencedorJogada(int numeroJogada, Jogador jogador) {
		Optional<Carta> carta = getJogadaMap(Optional.of(numeroJogada)).stream().map(JogadaTruco::getCarta).max((c1, c2) -> getCartaPeso(c1.get()).compareTo(getCartaPeso(c2.get()))).get();
		List<JogadaTruco> jogada = getJogadaMap(Optional.of(numeroJogada)).stream().filter(jd -> jd.getCarta().equals(carta)).collect(Collectors.toList());
		return jogada.get(0).getJogador();
	}

	private void logar(String mensagem, Truco truco) {
		System.out.format("Jogadores %s da mao %d\n", mensagem, truco.getMaoList().size() + 1);
		truco.getMesa().getJogadorList().forEach(j -> System.out.format("[%s total cartas %d] ", j.getNome(), j.getBaralho().getCartas().size()));
		System.out.format("\nMonte %d", truco.getMonte().getCartas().size());
		System.out.format("\nCarta Virada %s", getCartaVirada());
		System.out.format("\nBaralho => %d", truco.getBaralho().getCartas().size());
	}

	void setJogadaMap(Map<Integer, List<JogadaTruco>> jogadaMap) {
		this.jogadaMap = Objects.requireNonNull(jogadaMap);
	}

	void setManilhaList(List<Carta> manilhaList) {
		this.manilhaList = Objects.requireNonNull(manilhaList);
	}

	public void setCartaVirada(Carta cartaVirada, Baralho baralho) {
		if (this.cartaVirada != cartaVirada) {
			this.cartaVirada = cartaVirada;

			AtomicInteger peso = new AtomicInteger(0);
			if (getCartaVirada().isPresent()) {
				// definir as manilhas
				Numero numeroSuperior = getNumeroSuperior(this.getCartaVirada().get().getNumero().get());
				setManilhaList(CARTA_VIRADA_ORDEM_DECRESCENTE_NAIPE.stream().map(naipe -> baralho.getCartas(numeroSuperior, naipe).get(0)).collect(Collectors.toList()));
				// definir o peso das manilhas
				setCartaPesoMap(getManilhaList().stream().collect(Collectors.toMap(c -> new TreeSet<>((List<Carta>) Arrays.asList(c)), c -> peso.decrementAndGet())));
				// definir o peso das demais cartas
				getCartaPesoMap().putAll(CARTA_VIRADA_ORDEM_DECRESCENTE_NUMERO.stream()
						.filter(n -> !n.equals(numeroSuperior))
						.map(numero -> new TreeSet<>(baralho.getCartas(numero)))
						.collect(Collectors.toMap(k -> k, k -> peso.decrementAndGet())));
			} else {
				// definir manilhas
				setManilhaList(new ArrayList<>());
				getManilhaList().addAll(baralho.getCartas(Numero.QUATRO, Naipe.PAUS));
				getManilhaList().addAll(baralho.getCartas(Numero.SETE, Naipe.COPAS));
				getManilhaList().addAll(baralho.getCartas(Numero.AS, Naipe.ESPADAS));
				getManilhaList().addAll(baralho.getCartas(Numero.SETE, Naipe.OUROS));
				getManilhaList().addAll(baralho.getCartas(null, null));
				// definir o peso das manilhas
				setCartaPesoMap(getManilhaList().stream().collect(Collectors.toMap(c -> new TreeSet<>((List<Carta>) Arrays.asList(c)), c -> peso.decrementAndGet())));
				// definir o peso das demais cartas
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.TRES)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.DOIS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.AS, Naipe.PAUS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.AS, Naipe.COPAS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.AS, Naipe.OUROS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.REIS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.VALETE)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.DAMA)), peso.decrementAndGet());
				// cartas inferiores
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.SETE, Naipe.PAUS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.SETE, Naipe.ESPADAS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.SEIS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.CINCO)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.QUATRO, Naipe.COPAS)), peso.decrementAndGet());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.QUATRO, Naipe.ESPADAS)), peso.get());
				getCartaPesoMap().put(new TreeSet<>(baralho.getCartas(Numero.QUATRO, Naipe.OUROS)), peso.get());
			}
			// ordenar pelo valor
			setCartaPesoMap(getCartaPesoMap().entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new)));
		}
	}

	void setCartaPesoMap(Map<SortedSet<Carta>, Integer> cartaPesoMap) {
		this.cartaPesoMap = cartaPesoMap;
	}

	private void setJogadorMao(Jogador jogadorMao) {
		this.jogadorMao = jogadorMao;
	}

}
