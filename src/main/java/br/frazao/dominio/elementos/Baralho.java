package br.frazao.dominio.elementos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Baralho {

	public static Baralho criar() {
		return criar(new ArrayList<>());
	}

	public static Baralho criar(Fundo fundo) {
		return criar(fundo, false);
	}

	public static Baralho criar(Fundo fundo, boolean usarCoringa) {
		List<Carta> cartas = new ArrayList<>();
		if (usarCoringa) {
			cartas.add(Carta.criar(fundo, null, null));
		}
		Stream.of(Naipe.valoresOrdenados()).forEachOrdered(naipe -> Stream.of(Numero.valoresOrdenados()).forEachOrdered(numero -> cartas.add(Carta.criar(fundo, numero, naipe))));
		return criar(cartas);
	}

	public static Baralho criar(List<Carta> cartas) {
		Baralho result = new Baralho();
		return result.encarta(cartas);
	}

	private List<Carta> cartas;

	private Baralho() {
	}

	public Optional<List<Carta>> descarta() {
		return descarta(getCartas());
	}

	public Optional<List<Carta>> descarta(Carta... cartas) {
		return descarta(Arrays.asList(cartas));
	}

	public Optional<List<Carta>> descarta(List<Carta> cartas) {
		List<Carta> result = Objects.requireNonNull(cartas).stream().filter(carta -> carta != null).collect(Collectors.toList());
		return getCartas().removeAll(result) ? Optional.ofNullable(result) : Optional.empty();
	}

	public Optional<Carta> descarta(Optional<Carta> carta) {
		if (carta.isPresent()) {
			if (getCartas().remove(carta.get())) {
				return carta;
			}
		}
		return Optional.empty();
	}

	public Baralho encarta(Carta... cartas) {
		return encarta(Arrays.asList(cartas));
	}

	public Baralho encarta(List<Carta> cartas) {
		if (cartas != null && !cartas.isEmpty()) {
			cartas.stream().forEach(carta -> encarta(Optional.of(carta)));
		}
		return this;
	}

	public Baralho encarta(Optional<Carta> carta) {
		carta.ifPresent(encarta -> {
			if (getCartas().indexOf(encarta) < 0) {
				getCartas().add(encarta);
			}
		});
		return this;
	}

	public Optional<Carta> getCarta(Carta pesquisa) {
		return getCarta(pesquisa.getFundo(), pesquisa.getNumero(), pesquisa.getNaipe());
	}

	public Optional<Carta> getCarta(Fundo fundo, Optional<Numero> numero, Optional<Naipe> naipe) {
		return getCartas().stream().filter(carta -> fundo.equals(carta.getFundo()) && numero.equals(carta.getNumero()) && naipe.equals(carta.getNaipe())).findAny();
	}

	public List<Carta> getCartas() {
		if (this.cartas == null) {
			this.cartas = new ArrayList<>();
		}
		return this.cartas;
	}

	public List<Carta> getCartas(Carta... pesquisa) {
		return getCartas(Arrays.asList(pesquisa));
	}

	public List<Carta> getCartas(Fundo pesquisa) {
		return getCartas().stream().filter(carta -> Objects.requireNonNull(pesquisa).equals(carta.getFundo())).collect(Collectors.toList());
	}

	public List<Carta> getCartas(Integer pesquisa) {
		return Objects.requireNonNull(pesquisa) >= 0 ? getCartasDesce(Math.abs(pesquisa)) : getCartasSobe(-Math.abs(pesquisa));
	}

	public List<Carta> getCartas(List<Carta> pesquisa) {
		return getCartas().stream().filter(carta -> Objects.requireNonNull(pesquisa).contains(carta)).collect(Collectors.toList());
	}

	public List<Carta> getCartas(Naipe pesquisa) {
		return getCartas().stream().filter(carta -> Objects.requireNonNull(pesquisa).equals(carta.getNaipe().orElse(null))).collect(Collectors.toList());
	}

	public List<Carta> getCartas(Numero pesquisa) {
		return getCartas().stream().filter(carta -> Objects.requireNonNull(pesquisa).equals(carta.getNumero().orElse(null))).collect(Collectors.toList());
	}

	public List<Carta> getCartas(Numero numero, Naipe naipe) {
		return getCartas().stream().filter(carta -> Objects.requireNonNull(numero).equals(carta.getNumero().orElse(null)) && Objects.requireNonNull(naipe).equals(carta.getNaipe().orElse(null))).collect(Collectors.toList());
	}

	public List<Carta> getCartasDesce(Integer pesquisa) {
		return getCartas().stream().limit(Math.abs(Objects.requireNonNull(pesquisa))).collect(Collectors.toList());
	}

	public List<Carta> getCartasSobe(Integer pesquisa) {
		return getCartas().stream().skip(getCartas().size() - Math.abs(Objects.requireNonNull(pesquisa))).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "Baralho [cartas=" + cartas + "]";
	}

}
