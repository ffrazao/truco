package br.frazao.dominio.truco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.frazao.dominio.elementos.Baralho;
import br.frazao.dominio.elementos.Carta;
import br.frazao.dominio.elementos.Naipe;
import br.frazao.dominio.elementos.Numero;
import br.frazao.dominio.jogo.Jogador;

public class Mao {

	private Truco truco;

	private Map<List<Carta>, Integer> cartaPesoMap;

	private static final List<Numero> NUMERO_ORDEM_CARTA_VIRADA = Arrays.asList(Numero.TRES, Numero.DOIS, Numero.AS, Numero.REI, Numero.VALETE, Numero.DAMA, Numero.SETE, Numero.SEIS, Numero.CINCO, Numero.QUATRO);

	private static final List<Naipe> NAIPE_ORDEM_CARTA_VIRADA = Arrays.asList(Naipe.PAUS, Naipe.COPAS, Naipe.ESPADAS, Naipe.OUROS);

	private Numero getNumeroSuperior(Numero numero) {
		Integer posicao = NUMERO_ORDEM_CARTA_VIRADA.indexOf(numero);
		posicao = posicao == 0 ? NUMERO_ORDEM_CARTA_VIRADA.size() - 1 : posicao - 1;
		return NUMERO_ORDEM_CARTA_VIRADA.get(posicao);
	}

	private Carta cartaVirada;

	private List<Carta> manilhasList;

	public boolean isManilha(Carta carta) {
		return this.manilhasList.contains(carta);
	}

	public Integer getCartaPeso(Carta carta) {
		return this.cartaPesoMap.get(carta);
	}

	public List<Carta> getCartaPeso(Integer peso, Baralho baralho) {
		return baralho.getCartas().stream().filter(c -> this.cartaPesoMap.entrySet().stream().filter(map -> peso.equals(map.getValue())).map(m -> m.getKey()).flatMap(List::stream).collect(Collectors.toList()).contains(c)).collect(Collectors.toList());;
	}

	public Optional<Carta> getCartaPorPeso(Integer pesquisa) {
		return getCartas().stream().filter(carta -> carta.getPeso().equals(Objects.requireNonNull(pesquisa))).findAny();
	}

	public Optional<Integer> getPesoMaior() {
		return getCartas().stream().max((c1, c2) -> Integer.compare(c1.getPeso(), c2.getPeso())).map(Carta::getPeso);
	}

	public Optional<Integer> getPesoMenor() {
		return getCartas().stream().min((c1, c2) -> Integer.compare(c1.getPeso(), c2.getPeso())).map(Carta::getPeso);
	}

	public Baralho setPeso(Integer peso, Optional<Carta> carta) {
		carta.ifPresent(c -> getCarta(c).ifPresent(c1 -> c1.setPeso(Objects.requireNonNull(peso))));
		return this;
	}

	public void setCartaVirada(Carta cartaVirada) {
		this.cartaVirada = cartaVirada;
		AtomicInteger peso = new AtomicInteger(0);
		this.manilhasList = new ArrayList<>();
		this.cartaPesoMap = new TreeMap<>();
		if (getCartaVirada().isPresent()) {
			// definir o peso das manilhas
			Numero numeroSuperior = getNumeroSuperior(this.getCartaVirada().get().getNumero().get());
			NAIPE_ORDEM_CARTA_VIRADA.stream().map(naipe -> {
				this.manilhasList.add(truco.getBaralho().getCartas(numeroSuperior, naipe).get(0));
				return this.manilhasList.get(this.manilhasList.size() - 1);
			}).forEachOrdered(carta -> this.cartaPesoMap.put(Arrays.asList(carta), peso.decrementAndGet()));

			// definir o peso das demais cartas
			NUMERO_ORDEM_CARTA_VIRADA.stream().filter(numero -> !numero.equals(numeroSuperior)).forEachOrdered(numero -> this.cartaPesoMap.put(truco.getBaralho().getCartas(numero), peso.decrementAndGet()));
		} else {
			this.manilhasList.add(truco.getBaralho().getCartas(Numero.QUATRO, Naipe.PAUS).get(0));
			this.manilhasList.add(truco.getBaralho().getCartas(Numero.SETE, Naipe.COPAS).get(0));
			this.manilhasList.add(truco.getBaralho().getCartas(Numero.AS, Naipe.ESPADAS).get(0));
			this.manilhasList.add(truco.getBaralho().getCartas(Numero.SETE, Naipe.OUROS).get(0));

			this.manilhasList.stream().forEachOrdered(carta -> this.cartaPesoMap.put(Arrays.asList(carta), peso.decrementAndGet()));
			if (truco.isUsarCoringa()) {
				this.manilhasList.add(truco.getBaralho().getCartas(null, null).get(0));
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

	private List<JogadaTruco> jogadaList;

	private Jogador mao;

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

	public Jogador getMao() {
		return mao;
	}

	public boolean isCangou() {
		return isCangou(this.jogadaList.size() - 1);
	}

	public boolean isCangou(int numeroJogada) {
		return this.jogadaList.get(this.jogadaList.size() - 1) != null;
	}

	public Mao jogar(Truco truco) {
		this.truco = truco;
		return null;
	}

}
