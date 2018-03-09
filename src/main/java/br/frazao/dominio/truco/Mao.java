package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;

public class Mao {

	private static final List<Naipe> VIRA_CARTA_ORDEM_DECRESCENTE_NAIPE = Arrays.asList(Naipe.PAUS, Naipe.COPAS, Naipe.ESPADAS, Naipe.OUROS);

	private static final List<Numero> VIRA_CARTA_ORDEM_DECRESCENTE_NUMERO = Arrays.asList(Numero.TRES, Numero.DOIS, Numero.AS, Numero.REIS, Numero.VALETE, Numero.DAMA, Numero.SETE, Numero.SEIS, Numero.CINCO, Numero.QUATRO);

	public static Mao criar(JogadorTruco jogadorMao) {
		return new Mao(jogadorMao);
	}

	private Map<SortedSet<Carta>, Integer> cartaPesoMap;

	private Map<Integer, List<JogadaTruco>> jogadaMap;

	private List<Carta> manilhaList;

	private JogadorTruco maoJogadorTruco;

	private Carta viraCarta = Carta.criar(null, null, null);

	private Mao(JogadorTruco jogadorMao) {
		setMaoJogadorTruco(jogadorMao);
	}

	private JogadorTruco getCangou(List<JogadorTruco> vencedorJogadorTrucoList) {
		JogadorTruco result = null;
		AtomicReference<JogadorTruco> jogador = new AtomicReference<JogadorTruco>(null);
		if (vencedorJogadorTrucoList != null && vencedorJogadorTrucoList.size() > 1) {
			vencedorJogadorTrucoList.stream().forEach(j1 -> vencedorJogadorTrucoList.stream().filter(filtro -> !filtro.equals(j1)).forEach(j2 -> {
				if (jogador.get() == null && !j1.getTime().contains(j2)) {
					jogador.set(j2);
				}
			}));
			if (jogador.get() != null) {
				result = jogador.get();
			}
		}
		return result;
	}

	private Integer getCartaPeso(Carta carta) {
		return getCartaPesoMap().entrySet().stream().filter(conjuntoCartas -> conjuntoCartas.getKey().contains(carta)).map(k -> k.getValue()).findFirst().get();
	}

	private Integer getCartaPesoMaior() {
		return getCartaPesoMap().entrySet().stream().max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get().getValue();
	}
	
	private Integer getCartaPesoMenor() {
		return getCartaPesoMap().entrySet().stream().min((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get().getValue();		
	}

	private List<Integer> getCartaPeso(List<Carta> cartas) {
		return cartas.stream().map(this::getCartaPeso).collect(Collectors.toList());
	}

	private Optional<Integer> getCartaPesoMaior(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).max((p1, p2) -> Integer.compare(p1, p2));
	}

	private Map<SortedSet<Carta>, Integer> getCartaPesoMap() {
		if (this.cartaPesoMap == null) {
			this.cartaPesoMap = new TreeMap<>();
		}
		return this.cartaPesoMap;
	}

	private Optional<Integer> getCartaPesoMenor(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).min((p1, p2) -> Integer.compare(p1, p2));
	}

	public JogadaTruco getJogada() {
		return getJogada(getJogadaAtual());
	}

	private JogadaTruco getJogada(Integer jogada) {
		return getJogadaList().get(jogada);
	}

	private Integer getJogadaAtual() {
		return getJogadaList().size() - 1;
	}

	public List<JogadaTruco> getJogadaList() {
		return getJogadaList(getJogadaListAtual().orElse(null));
	}

	private List<JogadaTruco> getJogadaList(Integer jogada) {
		return getJogadaMap().get(jogada);
	}

	private Optional<Integer> getJogadaListAtual() {
		return getJogadaMap().keySet().stream().max((k1, k2) -> k1.compareTo(k2));
	}

	private List<JogadorTruco> getJogadaListVencedor(Integer numeroJogada) {
		List<JogadorTruco> result = null;
		Integer maiorPeso = getCartaPesoMaior(getJogadaList(numeroJogada).stream().map(j -> j.getCarta().get()).collect(Collectors.toList())).get();
		List<JogadaTruco> jogadaTrucoMaiorCartaPeso = getJogadaPorCartaPeso(getJogadaList(numeroJogada), maiorPeso);
		result = jogadaTrucoMaiorCartaPeso.stream().map(j -> j.getJogador()).collect(Collectors.toList());
		return result;
	}

	public Map<Integer, List<JogadaTruco>> getJogadaMap() {
		if (this.jogadaMap == null) {
			setJogadaMap(new TreeMap<>());
		}
		return this.jogadaMap;
	}

	private List<JogadaTruco> getJogadaPorCartaPeso(List<JogadaTruco> jogadaList, Integer peso) {
		return jogadaList.stream().filter(jt -> getCartaPeso(jt.getCarta().get()).equals(peso)).collect(Collectors.toList());
	}

	private List<Carta> getManilhaList() {
		if (this.manilhaList == null) {
			this.manilhaList = new ArrayList<>();
		}
		return this.manilhaList;
	}

	public JogadorTruco getMaoJogadorTruco() {
		return this.maoJogadorTruco;
	}

	private Numero getNumeroSuperior(Numero numero) {
		Integer posicao = VIRA_CARTA_ORDEM_DECRESCENTE_NUMERO.indexOf(numero);
		posicao = posicao == 0 ? VIRA_CARTA_ORDEM_DECRESCENTE_NUMERO.size() - 1 : posicao - 1;
		return VIRA_CARTA_ORDEM_DECRESCENTE_NUMERO.get(posicao);
	}

	private JogadorTruco getProximoJogadorMao() {
		JogadorTruco cangou = null;
		Integer jogadaNumero = getJogadaListAtual().get() - 1;
		List<JogadorTruco> jogadaVencedorAnterior = getJogadaListVencedor(jogadaNumero);
		cangou = getCangou(jogadaVencedorAnterior);
		return cangou == null ? jogadaVencedorAnterior.get(0) : cangou;
	}

	public Optional<Carta> getViraCarta() {
		return Optional.ofNullable(this.viraCarta);
	}

	private boolean isFinalizada() {
		Map<Set<JogadorTruco>, Integer> placar = new LinkedHashMap<>();
		List<JogadorTruco> vencedorList;
		for (Entry<Integer, List<JogadaTruco>> jogadas : getJogadaMap().entrySet()) {
			vencedorList = getJogadaListVencedor(jogadas.getKey());
			if (getCangou(vencedorList) != null) {

			}
		}

		// vencedorList = getJogadaMap().entrySet().stream().map((jogada) ->
		// getJogadaVencedorList(jogada.getKey()));
		// Map<List<Jogador>, Integer>
		// getJogadaMap().
		return false;
	}

	private boolean isManilha(Carta carta) {
		return getManilhaList().contains(carta);
	}

	public Mao jogar(Truco truco) {
		System.out.println(truco.getMaoList().size());

		// necessário para definir manilhas e pesos das cartas do baralho
		Baralho baralhoCopia = Baralho.criar(truco.getBaralho().getCartas());

		// embaralhar
		getMaoJogadorTruco().embaralhar(truco.getBaralho());

		// cortar o baralho
		int corte = truco.getMesa().getJogadorAntes(getMaoJogadorTruco()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * Truco.TOTAL_CARTAS_DISTRIBUIR_MAO + (truco.isViraCarta() ? 1 : 0));
		truco.getMonte().encarta(truco.getBaralho().descarta(truco.getBaralho().getCartas(corte >= 0 ? corte - truco.getBaralho().getCartas().size() : truco.getBaralho().getCartas().size() + corte)).orElse(null));

		// distribuir as cartas
		// aos jogadores
		int distribui = corte < 0 ? -Truco.TOTAL_CARTAS_DISTRIBUIR_MAO : Truco.TOTAL_CARTAS_DISTRIBUIR_MAO;
		truco.getMesa().getJogadorList().stream().forEach(j -> j.getBaralho().encarta(truco.getBaralho().descarta(truco.getBaralho().getCartas(distribui)).get()));

		// verificar se é necessário virar carta
		Carta viraCarta = null;
		if (truco.isViraCarta()) {
			// virar a carta
			viraCarta = truco.getBaralho().descarta(truco.getBaralho().getCartas(corte < 0 ? -1 : 1)).get().get(0);
		}
		setViraCarta(viraCarta, baralhoCopia);

		// joga no monte o resto das cartas
		truco.getMonte().encarta(truco.getBaralho().descarta().orElse(null));

		// captar jogadas
		try {
			JogadorTruco jogador = null;
			for (int c = 0; c < Truco.TOTAL_CARTAS_DISTRIBUIR_MAO; c++) {
				// iniciar a jogada
				getJogadaMap().put(c, new ArrayList<>());

				// definir o próximo jogador
				jogador = jogador == null ? getMaoJogadorTruco() : getProximoJogadorMao();
				JogadorTruco primeiro = jogador;

				// captar as jogadas
				do {
					JogadaTruco jogada = (JogadaTruco) jogador.jogar(truco);
					getJogadaList().add(jogada);
					truco.getMonte().encarta(jogada.getCarta());
				} while ((!primeiro.equals((jogador = (JogadorTruco) truco.getMesa().getJogadorDepois(jogador).get()))));

				if (isFinalizada()) {
					break;
				}
			}
		} catch (RuntimeException e) {

		}

		// recolher todas as cartas
		// pega dos jogadores
		truco.getMesa().getJogadorList().forEach(jogador -> truco.getBaralho().encarta(jogador.getBaralho().descarta().orElse(null)));
		// pega da carta virada
		truco.getMao().ifPresent(m -> m.getViraCarta().ifPresent(cv -> truco.getBaralho().encarta(cv)));
		// pega do monte
		truco.getBaralho().encarta(truco.getMonte().descarta().orElse(null));

		// ordenar
		getMaoJogadorTruco().ordenar(truco.getBaralho());

		return this;
	}

	private void logar(String mensagem, Truco truco) {
		System.out.format("Jogadores %s da mao %d\n", mensagem, truco.getMaoList().size() + 1);
		truco.getMesa().getJogadorList().forEach(j -> System.out.format("[%s total cartas %d] ", j.getNome(), j.getBaralho().getCartas().size()));
		System.out.format("\nMonte %d", truco.getMonte().getCartas().size());
		System.out.format("\nCarta Virada %s", getViraCarta());
		System.out.format("\nBaralho => %d", truco.getBaralho().getCartas().size());
	}

	private void setCartaPesoMap(Map<SortedSet<Carta>, Integer> cartaPesoMap) {
		this.cartaPesoMap = Objects.requireNonNull(cartaPesoMap);
	}

	private void setJogadaMap(Map<Integer, List<JogadaTruco>> jogadaMap) {
		this.jogadaMap = Objects.requireNonNull(jogadaMap);
	}

	private void setManilhaList(List<Carta> manilhaList) {
		this.manilhaList = Objects.requireNonNull(manilhaList);
	}

	private void setMaoJogadorTruco(JogadorTruco maoJogadorTruco) {
		this.maoJogadorTruco = maoJogadorTruco;
	}

	private void setViraCarta(Carta viraCarta, Baralho baralho) {
		if (this.viraCarta != viraCarta) {
			this.viraCarta = viraCarta;

			AtomicInteger peso = new AtomicInteger(0);
			if (getViraCarta().isPresent()) {
				// definir as manilhas
				Numero numeroSuperior = getNumeroSuperior(this.getViraCarta().get().getNumero().get());
				setManilhaList(VIRA_CARTA_ORDEM_DECRESCENTE_NAIPE.stream().map(naipe -> baralho.getCartas(numeroSuperior, naipe).get(0)).collect(Collectors.toList()));
				// definir o peso das manilhas
				setCartaPesoMap(getManilhaList().stream().collect(Collectors.toMap(c -> new TreeSet<>((List<Carta>) Arrays.asList(c)), c -> peso.decrementAndGet())));
				// definir o peso das demais cartas
				getCartaPesoMap().putAll(VIRA_CARTA_ORDEM_DECRESCENTE_NUMERO.stream().filter(n -> !n.equals(numeroSuperior)).map(numero -> new TreeSet<>(baralho.getCartas(numero))).collect(Collectors.toMap(k -> k, k -> peso.decrementAndGet())));
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

}
