package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogador;

public class Mao {

	private static final List<Naipe> NAIPE_ORDEM_CARTA_VIRADA = Arrays.asList(Naipe.PAUS, Naipe.COPAS, Naipe.ESPADAS, Naipe.OUROS);

	private static final List<Numero> NUMERO_ORDEM_CARTA_VIRADA = Arrays.asList(Numero.TRES, Numero.DOIS, Numero.AS, Numero.REI, Numero.VALETE, Numero.DAMA, Numero.SETE, Numero.SEIS, Numero.CINCO, Numero.QUATRO);

	private static final int TOTAL_CARTAS_DISTRIBUIR_MAO = 3;

	private Map<List<Carta>, Integer> cartaPesoMap;

	private Carta cartaVirada;

	private List<JogadaTruco> jogadaList;

	private List<Carta> manilhaList;

	private Jogador jogadorMao;

	private Truco truco;

	public Mao(JogadorTruco jogadorMao) {
		this.jogadorMao = jogadorMao;
	}

	public Integer getCartaPeso(Carta carta) {
		return this.cartaPesoMap.get(carta);
	}

	public List<Integer> getCartaPeso(List<Carta> cartas) {
		return cartas.stream().map(this::getCartaPeso).collect(Collectors.toList());
	}

	public Optional<Integer> getCartaPesoMaior(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).max((p1, p2) -> Integer.compare(p1, p2));
	}

	public Optional<Integer> getCartaPesoMenor(List<Carta> cartaList) {
		return cartaList.stream().map(this::getCartaPeso).min((p1, p2) -> Integer.compare(p1, p2));
	}

	public Optional<Carta> getCartaVirada() {
		return Optional.ofNullable(this.cartaVirada);
	}

	public JogadaTruco getJogada() {
		return getJogada(this.jogadaList.size() - 1);
	}

	public JogadaTruco getJogada(int numeroJogada) {
		return this.jogadaList.get(this.jogadaList.size() - 1);
	}

	public List<JogadaTruco> getJogadaList() {
		return Collections.unmodifiableList(jogadaList);
	}

	List<Carta> getManilhaList() {
		return manilhaList;
	}

	public Jogador getJogadorMao() {
		return this.jogadorMao;
	}

	private Numero getNumeroSuperior(Numero numero) {
		Integer posicao = NUMERO_ORDEM_CARTA_VIRADA.indexOf(numero);
		posicao = posicao == 0 ? NUMERO_ORDEM_CARTA_VIRADA.size() - 1 : posicao - 1;
		return NUMERO_ORDEM_CARTA_VIRADA.get(posicao);
	}

	Truco getTruco() {
		return truco;
	}

	public boolean isCangou() {
		return isCangou(this.jogadaList.size() - 1);
	}

	public boolean isCangou(int numeroJogada) {
		return this.jogadaList.get(this.jogadaList.size() - 1) != null;
	}

	public boolean isManilha(Carta carta) {
		return this.manilhaList.contains(carta);
	}

	public Mao jogar(Truco truco) {
		this.truco = truco;
		
		// recolher todas as cartas
		truco.getMesa().getJogadorList().forEach(jogador -> truco.getBaralho().encarta(jogador.getBaralho().descarta(jogador.getBaralho().getCartas()).get())); // pega dos jogadores
		truco.getBaralho().encarta(truco.getMonte().descarta(truco.getMonte().getCartas()).get()); // pega do monte
		truco.getMao().getCartaVirada().ifPresent(cartaVirada -> truco.getBaralho().encarta(cartaVirada)); // pega da carta virada
		
		// embaralhar
		truco.getBaralho().embaralha();

		// cortar
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		int corte = truco.getMesa().getJogadorAntes(truco.getMao().getJogadorMao()).get().cortar(truco.getBaralho().getCartas().size(), truco.getMesa().getJogadorList().size() * TOTAL_CARTAS_DISTRIBUIR_MAO); 
		
//		truco.getMonte().encarta(.cortar(truco.getBaralho()));

		// distribuir
		truco.getMesa().getJogadorList().forEach(jogador -> truco.getBaralho().encarta(jogador.getBaralho().getCartas()));

		// captar jogadas

		return this;
	}

	public void setCartaVirada(Carta cartaVirada) {

		if (this.cartaVirada != cartaVirada) {
			this.cartaVirada = cartaVirada;
			this.manilhaList = new ArrayList<>();
			this.cartaPesoMap = new TreeMap<>();

			AtomicInteger peso = new AtomicInteger(0);
			if (getCartaVirada().isPresent()) {
				// definir o peso das manilhas
				Numero numeroSuperior = getNumeroSuperior(this.getCartaVirada().get().getNumero().get());
				NAIPE_ORDEM_CARTA_VIRADA.stream().map(naipe -> {
					this.manilhaList.add(truco.getBaralho().getCartas(numeroSuperior, naipe).get(0));
					return this.manilhaList.get(this.manilhaList.size() - 1);
				}).forEachOrdered(carta -> this.cartaPesoMap.put(Arrays.asList(carta), peso.decrementAndGet()));

				// definir o peso das demais cartas
				NUMERO_ORDEM_CARTA_VIRADA.stream().filter(numero -> !numero.equals(numeroSuperior)).forEachOrdered(numero -> this.cartaPesoMap.put(truco.getBaralho().getCartas(numero), peso.decrementAndGet()));
			} else {
				this.manilhaList.add(truco.getBaralho().getCartas(Numero.QUATRO, Naipe.PAUS).get(0));
				this.manilhaList.add(truco.getBaralho().getCartas(Numero.SETE, Naipe.COPAS).get(0));
				this.manilhaList.add(truco.getBaralho().getCartas(Numero.AS, Naipe.ESPADAS).get(0));
				this.manilhaList.add(truco.getBaralho().getCartas(Numero.SETE, Naipe.OUROS).get(0));

				this.manilhaList.stream().forEachOrdered(carta -> this.cartaPesoMap.put(Arrays.asList(carta), peso.decrementAndGet()));
				if (truco.isUsarCoringa()) {
					this.manilhaList.add(truco.getBaralho().getCartas(null, null).get(0));
					this.cartaPesoMap.put(truco.getBaralho().getCartas(null, null), peso.decrementAndGet());
				}
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.TRES), peso.decrementAndGet());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.DOIS), peso.decrementAndGet());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.AS, Naipe.PAUS), peso.decrementAndGet());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.AS, Naipe.COPAS), peso.get());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.AS, Naipe.OUROS), peso.get());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.REI), peso.decrementAndGet());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.VALETE), peso.decrementAndGet());
				this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.DAMA), peso.decrementAndGet());
				if (!truco.isBaralhoVazio()) {
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.SETE, Naipe.PAUS), peso.decrementAndGet());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.SETE, Naipe.ESPADAS), peso.get());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.SEIS), peso.decrementAndGet());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.CINCO), peso.decrementAndGet());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.QUATRO, Naipe.COPAS), peso.decrementAndGet());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.QUATRO, Naipe.ESPADAS), peso.get());
					this.cartaPesoMap.put(truco.getBaralho().getCartas(Numero.QUATRO, Naipe.OUROS), peso.get());
				}
			}
		}
	}

}
